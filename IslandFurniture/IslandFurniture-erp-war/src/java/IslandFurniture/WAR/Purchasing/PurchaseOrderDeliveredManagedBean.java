/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Purchasing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Supplier;
import IslandFurniture.EJB.Purchasing.ManagePurchaseOrderLocal;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
import IslandFurniture.StaticClasses.SendEmailByPost;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class PurchaseOrderDeliveredManagedBean {

    private String username;

    private Long purchaseOrderId;
    private Long purchaseOrderDetailId;
    private Long supplierId;
    private Long procuredStockId;
    private Long plantId;

    private Calendar orderDate;
    private PurchaseOrderStatus status;
    private ProcuredStockPurchaseOrder purchaseOrder;
    private ProcuredStockPurchaseOrderDetail purchaseOrderDetail;
    private List<ProcuredStockPurchaseOrderDetail> purchaseOrderDetailList;
    private Staff staff;
    private Supplier supplier;
    private Plant plant;
    private ManufacturingFacility mf;
    private String orderDateString;
    private int quantity;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManagePurchaseOrderLocal mpol;
    @EJB
    public SupplierManagerLocal sml;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);

        this.purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("POid");
        try {
            if(purchaseOrderId == null) {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect("purchaseorder.xhtml"); 
            }else {
                purchaseOrder = mpol.getPurchaseOrder(purchaseOrderId);
                if (staff.getPlant() instanceof ManufacturingFacility) {
                    mf = (ManufacturingFacility) staff.getPlant();
                }
                System.out.println("@Init PurchaseOrderDeliveredManagedBean:  this is the docomentid " + purchaseOrderId);
                purchaseOrderDetailList = mpol.viewPurchaseOrderDetails(purchaseOrderId);

                if (purchaseOrder.getOrderDate() != null) {
                    orderDateString = df.format(purchaseOrder.getOrderDate().getTime());
                }

                System.out.println("loaded some lists");
                System.out.println("Init");
            }
        } catch(IOException ex) {
            
        }        

    }
    
     // pay purchase order
    public String payPurchaseOrder() throws ParseException {
        status = PurchaseOrderStatus.getPurchaseOrderStatus(3);
        
        mpol.updatePurchaseOrder(purchaseOrderId, status, Calendar.getInstance());
        sendEmail(supplier);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Purchase Order Paid!", ""));
        return "purchaseorder";

    }
    
    public void sendEmail(Supplier recipient) {
        String title = "[Incoming Payment for Purchase Order: #" + purchaseOrderId + "] from Island Furniture "
                + mf.getName();
        String orderContent = mf.getName() + " Manufacturing Facility has billed the required payment for the following: " + "\r\n ";

        Iterator<ProcuredStockPurchaseOrderDetail> iterator = purchaseOrderDetailList.iterator();
        while (iterator.hasNext()) {
            ProcuredStockPurchaseOrderDetail current = iterator.next();
            orderContent = orderContent + current.getProcuredStock().getName();
            orderContent = orderContent + " x " + current.getQuantity() + "\r\n ";
        }
        try {
            // send notification to supplier: using zi xuan's email address as a placeholder currently
            SendEmailByPost.sendEmail("manufacturing", "aura.chrome@gmail.com",
                    title, orderContent);
        } catch (Exception ex) {
            Logger.getLogger(PurchaseOrderManaged2Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public ManufacturingFacility getMf() {
        return mf;
    }

    public void setMf(ManufacturingFacility mf) {
        this.mf = mf;
    }

    public String getOrderDateString() {
        return orderDateString;
    }

    public void setOrderDateString(String orderDateString) {
        this.orderDateString = orderDateString;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public SupplierManagerLocal getSml() {
        return sml;
    }

    public void setSml(SupplierManagerLocal sml) {
        this.sml = sml;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getProcuredStockId() {
        return procuredStockId;
    }

    public void setProcuredStockId(Long procuredStockId) {
        this.procuredStockId = procuredStockId;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Long getPurchaseOrderDetailId() {
        return purchaseOrderDetailId;
    }

    public void setPurchaseOrderDetailId(Long purchaseOrderDetailId) {
        this.purchaseOrderDetailId = purchaseOrderDetailId;
    }

    public Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public List<ProcuredStockPurchaseOrderDetail> getPurchaseOrderDetailList() {
        return purchaseOrderDetailList;
    }

    public void setPurchaseOrderDetailList(List<ProcuredStockPurchaseOrderDetail> purchaseOrderDetailList) {
        this.purchaseOrderDetailList = purchaseOrderDetailList;
    }

    public ProcuredStockPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(ProcuredStockPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public ManagePurchaseOrderLocal getMpol() {
        return mpol;
    }

    public void setMgrl(ManagePurchaseOrderLocal mpol) {
        this.mpol = mpol;
    }

    public ProcuredStockPurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(ProcuredStockPurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }
    
}
