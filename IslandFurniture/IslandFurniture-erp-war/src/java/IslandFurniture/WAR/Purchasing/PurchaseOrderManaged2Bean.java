/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Purchasing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Exceptions.DuplicateEntryException;
import IslandFurniture.EJB.Purchasing.ManagePurchaseOrderLocal;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import IslandFurniture.StaticClasses.SendEmailByPost;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class PurchaseOrderManaged2Bean implements Serializable {

    private String username;

    private Long purchaseOrderId;
    private Long purchaseOrderDetailId;
    private Long supplierId;
    private Long procuredStockId;
    private Long plantId;

    private Calendar orderDate;
    private PurchaseOrderStatus status;
    private ProcuredStockPurchaseOrder purchaseOrder;
    private List<ProcuredStockPurchaseOrder> purchaseOrderList;
    private ProcuredStockPurchaseOrderDetail purchaseOrderDetail;
    private List<ProcuredStockPurchaseOrderDetail> purchaseOrderDetailList;
    private Staff staff;
    private ProcuredStockSupplier supplier;
    private Plant plant;
    private ManufacturingFacility mf;
    private List<Plant> plantList;
    private List<ProcuredStock> procuredStockList;
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

        if (purchaseOrderId != null || !(staff.getPlant() instanceof ManufacturingFacility)) {
            purchaseOrder = mpol.getPurchaseOrder(purchaseOrderId);
            mf = (ManufacturingFacility) staff.getPlant();

            procuredStockList = mpol.viewSupplierProcuredStocks(purchaseOrderId, mf);
            purchaseOrderDetailList = mpol.viewPurchaseOrderDetails(purchaseOrderId);
            supplier = purchaseOrder.getSupplier();
            if (purchaseOrder.getOrderDate() != null) {
                orderDateString = df.format(purchaseOrder.getOrderDate().getTime());
            }

            System.out.println("loaded some lists");
            System.out.println("Init");
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("purchaseorder.xhtml");
            } catch (IOException ex) {

            }
        }
    }

    public void addStock() throws ParseException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        procuredStockId = Long.parseLong(request.getParameter("createPODetail:procuredStockId"));
        quantity = Integer.parseInt(request.getParameter("createPODetail:quantity"));
        try {
            mpol.createNewPurchaseOrderDetail(purchaseOrderId, procuredStockId, quantity);
            purchaseOrderDetailList = mpol.viewPurchaseOrderDetails(purchaseOrderId);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Stock added successfully!", ""));
        } catch (DuplicateEntryException ex) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ""));
        } finally {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", purchaseOrderId);
        }
    }
    
     // confirm purchase order
    public String confirmPurchaseOrder() throws ParseException {
        status = PurchaseOrderStatus.getPurchaseOrderStatus(1);
        
        mpol.updatePurchaseOrder(purchaseOrderId, status, Calendar.getInstance());
        sendEmail(supplier);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Purchase Order Confirmed!", ""));
        return "purchaseorder";

    }    
    

    public String editStock(ActionEvent event) throws IOException {
        ProcuredStockPurchaseOrderDetail pod = (ProcuredStockPurchaseOrderDetail) event.getComponent().getAttributes().get("PODid");
        System.out.println("Purchase Order Detail Id is: " + pod.getId().toString());

        mpol.updatePurchaseOrderDetail(pod);
        purchaseOrder = mpol.getPurchaseOrder(purchaseOrderId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful!", ""));

        return "purchaseorder2";
    }

    public String deletePurchaseOrderDetail() {
        purchaseOrderDetailId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("PODid"));

        mpol.deletePurchaseOrderDetail(purchaseOrderDetailId);
        purchaseOrderDetailList = mpol.viewPurchaseOrderDetails(purchaseOrderId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete Successful!", ""));

        return "purchaseorder2";
    }

    public void sendEmail(ProcuredStockSupplier recipient) {
        String title = "[Confirmed Purchase Order: #" + purchaseOrderId + "] from Island Furniture "
                + mf.getName();
        String orderContent = mf.getName() + " Manufacturing Facility would like to order the following: " + "\r\n ";

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

    public List<ProcuredStockPurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<ProcuredStockPurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public List<ProcuredStockPurchaseOrderDetail> getPurchaseOrderDetailList() {
        return purchaseOrderDetailList;
    }

    public void setPurchaseOrderDetailList(List<ProcuredStockPurchaseOrderDetail> purchaseOrderDetailList) {
        this.purchaseOrderDetailList = purchaseOrderDetailList;
    }

    public List<ProcuredStock> getProcuredStockList() {
        return procuredStockList;
    }

    public void setProcuredStockList(List<ProcuredStock> procuredStockList) {
        this.procuredStockList = procuredStockList;
    }

    public List<Plant> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<Plant> plantList) {
        this.plantList = plantList;
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

    public ProcuredStockSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ProcuredStockSupplier supplier) {
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

}
