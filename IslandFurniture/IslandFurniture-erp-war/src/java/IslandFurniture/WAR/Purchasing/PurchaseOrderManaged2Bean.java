/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Purchasing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.PurchaseOrderDetail;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Supplier;
import IslandFurniture.EJB.Purchasing.ManagePurchaseOrderLocal;
import IslandFurniture.EJB.SalesPlanning.SupplierManagerLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
public class PurchaseOrderManaged2Bean implements Serializable{

    private String username;
    
    private Long purchaseOrderId;
    private Long purchaseOrderDetailId;
    private Long supplierId;
    private Long procuredStockId;
    private Long plantId;
    
    private Calendar orderDate;
    private String status;
    private PurchaseOrder purchaseOrder;
    private List<PurchaseOrder> purchaseOrderList;
    private PurchaseOrderDetail purchaseOrderDetail;
    private List<PurchaseOrderDetail> purchaseOrderDetailList;    
    private Staff staff;
    private Supplier supplier;
    private Plant plant;
    private ManufacturingFacility mf;
    private List<Plant> plantList;
    private List<ProcuredStock> procuredStockList;
    private String orderDateString = null;
    private int quantity;
    
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
        if (purchaseOrderId != null) {
            purchaseOrder = mpol.getPurchaseOrder(purchaseOrderId);
        }
        if (staff.getPlant() instanceof ManufacturingFacility) {
            mf = (ManufacturingFacility) staff.getPlant();
        }
        System.out.println("@Init PurchaseOrderManaged2Bean:  this is the docomentid " + purchaseOrderId);
        procuredStockList = mpol.viewSupplierProcuredStocks(purchaseOrderId, mf);
        purchaseOrderDetailList = mpol.viewPurchaseOrderDetails(purchaseOrderId);
        System.out.println("loaded some lists");
        System.out.println("Init");
    }    
    
    public String addStock() throws ParseException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();        
        procuredStockId = Long.parseLong(request.getParameter("createPODetail:procuredStockId"));
        quantity = Integer.parseInt(request.getParameter("createPODetail:quantity"));
        mpol.createNewPurchaseOrderDetail(purchaseOrderId, procuredStockId, quantity);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", purchaseOrderId);
        
        this.purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("POid");
        if (purchaseOrderId != null) {
            purchaseOrder = mpol.getPurchaseOrder(purchaseOrderId);
        }
        System.out.println("@Init PurchaseOrderManaged2Bean:  this is the docomentid " + purchaseOrderId);
        purchaseOrderDetailList = mpol.viewPurchaseOrderDetails(purchaseOrderId);        
        
        return "purchaseorder2";
    }
    
    /* old update purchase order method
    public String updatePurchaseOrder() throws ParseException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        status = request.getParameter("updatePurchaseOrder:status");
        plantId = Long.parseLong(request.getParameter("updatePurchaseOrder:plantId"));
        orderDateString = request.getParameter("updatePurchaseOrder:orderDateString");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = (Date)formatter.parse(orderDateString); 
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        orderDate = cal;        
        mpol.updatePurchaseOrder(purchaseOrderId, "planned", plantId, orderDate);
        System.out.println("updated purchase order" + purchaseOrderId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", purchaseOrderId);
        return "purchaseorder2?faces-redirect=true";
    }*/
    
    // new update purchase order method
    public String updatePurchaseOrder() throws ParseException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        status = request.getParameter("updatePurchaseOrder:status");
        orderDateString = request.getParameter("updatePurchaseOrder:orderDateString");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = (Date)formatter.parse(orderDateString); 
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        orderDate = cal;        
        mpol.updatePurchaseOrder(purchaseOrderId, status, orderDate);
        System.out.println("updated purchase order" + purchaseOrderId);
        if (status.equals("confirmed")) {
            return "purchaseorder";
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", purchaseOrderId);
            return "purchaseorder2?faces-redirect=true";
        }
    }     
    
/**    public String updateStock(ActionEvent event) throws ParseException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Long podId = (Long) event.getComponent().getAttributes().get("PODid");
        purchaseOrderDetail = mpol.getPurchaseOrderDetail(podId);
        String psId = request.getParameter("viewPODetail:procuredStockId");
        procuredStockId = Long.valueOf(psId);
        quantity = Integer.parseInt(request.getParameter("viewPODetail:quantity"));
        mpol.updatePurchaseOrderDetail(purchaseOrderDetail, procuredStockId, quantity);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", purchaseOrderId);
        return "purchaseorder2?faces-redirect=true";
    }**/
    
    public String editStock(ActionEvent event) throws IOException {
        PurchaseOrderDetail pod = (PurchaseOrderDetail) event.getComponent().getAttributes().get("PODid");
        System.out.println("Purchase Order Detail Id is: " + pod.getId().toString());
        mpol.updatePurchaseOrderDetail(pod, pod.getProcuredStock().getId(), pod.getQuantity());
        purchaseOrderDetailList = mpol.viewPurchaseOrderDetails(purchaseOrderId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", purchaseOrderId);
        return "purchaseorder2?faces-redirect=true";
    }    
    
    public String deletePurchaseOrderDetail() {
        purchaseOrderDetailId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("PODid"));
        mpol.deletePurchaseOrderDetail(purchaseOrderDetailId);
        purchaseOrderDetailList = mpol.viewPurchaseOrderDetails(purchaseOrderId);
        return "purchaseorder2";
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
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }    
    
    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }
    
    public List<PurchaseOrderDetail> getPurchaseOrderDetailList() {
        return purchaseOrderDetailList;
    }

    public void setPurchaseOrderDetailList(List<PurchaseOrderDetail> purchaseOrderDetailList) {
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
    
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
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

    public PurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }


}
