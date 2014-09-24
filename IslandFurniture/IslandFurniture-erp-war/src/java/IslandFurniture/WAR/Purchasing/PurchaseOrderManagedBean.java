/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Purchasing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.PurchaseOrder;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class PurchaseOrderManagedBean implements Serializable {

    private String username;

    private Long purchaseOrderId;
    private Long supplierId;
    private Long plantId;

    private Calendar orderDate;
    private String status;
    private PurchaseOrder purchaseOrder;
    private List<PurchaseOrder> plannedOrderList;
    private List<PurchaseOrder> confirmedOrderList;
    private List<Supplier> supplierList;
    private List<Plant> plantList;
    private Staff staff;
    private Supplier supplier;
    private ManufacturingFacility mf;
    private Plant staffPlant;
    private String orderDateString = null;

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
        staffPlant = staff.getPlant();
        if (staffPlant instanceof ManufacturingFacility) {
            mf = (ManufacturingFacility) staffPlant;
        }
        plannedOrderList = mpol.viewPlannedPurchaseOrders(staffPlant);
        confirmedOrderList = mpol.viewConfirmedPurchaseOrders(staffPlant);
        supplierList = mpol.viewContractedSuppliers(mf);
        plantList = mpol.viewPlants();
        System.out.println("Init");
    }

    public String addPurchaseOrder() throws ParseException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        supplierId = Long.parseLong(request.getParameter("createPurchaseOrder:supplierId"));
        supplier = sml.getSupplier(supplierId);
        plantId = staff.getPlant().getId();
        orderDateString = request.getParameter("createPurchaseOrder:orderDateString");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date) formatter.parse(orderDateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        orderDate = cal;
        purchaseOrder = mpol.createNewPurchaseOrder("planned", supplier, plantId, orderDate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", purchaseOrder.getId());
        return "purchaseorder2?faces-redirect=true";
    }

    /*public String addNewPurchaseOrder() throws ParseException {
     HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();        
     supplierId = Long.parseLong(request.getParameter("createPurchaseOrder:supplierId"));
     supplier = sml.getSupplier(supplierId);
     plantId = Long.parseLong(request.getParameter("createPurchaseOrder:plantId"));
     orderDateString = request.getParameter("createPurchaseOrder:orderDateString");
     DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
     Date date = (Date)formatter.parse(orderDateString); 
     Calendar cal=Calendar.getInstance();
     cal.setTime(date);
     orderDate = cal;        
     mpol.createNewPurchaseOrder("planned", supplier, plantId, orderDate);
     return "purchaseorder";
     }*/
    public void purchaseOrderDetailActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", event.getComponent().getAttributes().get("POid"));
        purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("POid");
        System.out.println("this is the purchase order id at MAIN Mgd Bean" + purchaseOrderId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("purchaseorder2.xhtml");
    }

    public String deletePurchaseOrder() {
        System.out.println("deleting some order");
        purchaseOrderId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("POid"));
        mpol.deletePurchaseOrder(purchaseOrderId);
        plannedOrderList = mpol.viewPlannedPurchaseOrders(staffPlant);
        return "purchaseOrder";
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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

    public List<PurchaseOrder> getPlannedOrderList() {
        return plannedOrderList;
    }

    public void setPlannedOrderList(List<PurchaseOrder> plannedOrderList) {
        this.plannedOrderList = plannedOrderList;
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

    public ManagePurchaseOrderLocal getMpol() {
        return mpol;
    }

    public void setMgrl(ManagePurchaseOrderLocal mpol) {
        this.mpol = mpol;
    }

    public List<PurchaseOrder> getConfirmedOrderList() {
        return confirmedOrderList;
    }

    public void setConfirmedOrderList(List<PurchaseOrder> confirmedOrderList) {
        this.confirmedOrderList = confirmedOrderList;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public List<Plant> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<Plant> plantList) {
        this.plantList = plantList;
    }

    public SupplierManagerLocal getSml() {
        return sml;
    }

    public void setSml(SupplierManagerLocal sml) {
        this.sml = sml;
    }

}
