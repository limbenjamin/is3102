/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Purchasing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.ProcuredStockSupplier;
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

    private ProcuredStockPurchaseOrder purchaseOrder;
    private List<ProcuredStockPurchaseOrder> plannedOrderList;
    private List<ProcuredStockPurchaseOrder> confirmedOrderList;
    private List<ProcuredStockPurchaseOrder> deliveredOrderList;
    private List<ProcuredStockPurchaseOrder> paidOrderList;
    private List<ProcuredStockSupplier> supplierList;
    
    private Staff staff;
    private ProcuredStockSupplier supplier;
    private ManufacturingFacility mf;
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

        if (staff.getPlant() instanceof ManufacturingFacility) {
            mf = (ManufacturingFacility) staff.getPlant();
            plannedOrderList = mpol.viewPlannedPurchaseOrders(mf);
            confirmedOrderList = mpol.viewConfirmedPurchaseOrders(mf);
            deliveredOrderList = mpol.viewDeliveredPurchaseOrders(mf);
            paidOrderList = mpol.viewPaidPurchaseOrders(mf);
            supplierList = mpol.viewContractedSuppliers(mf);
            System.out.println("Init");
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }

    public void addPurchaseOrder() throws ParseException, Exception {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        supplierId = Long.parseLong(request.getParameter("createPurchaseOrder:supplierId"));
        supplier = sml.getSupplier(supplierId);
        orderDateString = request.getParameter("createPurchaseOrder:orderDateString");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date) formatter.parse(orderDateString);
        Calendar orderDate = Calendar.getInstance();
        orderDate.setTime(date);
        purchaseOrder = mpol.createNewPurchaseOrder(PurchaseOrderStatus.PLANNED, supplier, mf, mf, orderDate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", purchaseOrder.getId());
    }

    public void purchaseOrderDetailActionListener(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", event.getComponent().getAttributes().get("POid"));
    }

    public void deletePurchaseOrder(ActionEvent event) {
        mpol.deletePurchaseOrder(new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("POid")));
        plannedOrderList = mpol.viewPlannedPurchaseOrders(mf);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Purchase order has been sucessfully deleted", ""));
    }
    

    public ProcuredStockSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ProcuredStockSupplier supplier) {
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

    public List<ProcuredStockPurchaseOrder> getPlannedOrderList() {
        return plannedOrderList;
    }

    public void setPlannedOrderList(List<ProcuredStockPurchaseOrder> plannedOrderList) {
        this.plannedOrderList = plannedOrderList;
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

    public ManagePurchaseOrderLocal getMpol() {
        return mpol;
    }

    public void setMgrl(ManagePurchaseOrderLocal mpol) {
        this.mpol = mpol;
    }

    public List<ProcuredStockPurchaseOrder> getConfirmedOrderList() {
        return confirmedOrderList;
    }

    public void setConfirmedOrderList(List<ProcuredStockPurchaseOrder> confirmedOrderList) {
        this.confirmedOrderList = confirmedOrderList;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public List<ProcuredStockSupplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<ProcuredStockSupplier> supplierList) {
        this.supplierList = supplierList;
    }

    public SupplierManagerLocal getSml() {
        return sml;
    }

    public void setSml(SupplierManagerLocal sml) {
        this.sml = sml;
    }

    public List<ProcuredStockPurchaseOrder> getDeliveredOrderList() {
        return deliveredOrderList;
    }

    public void setDeliveredOrderList(List<ProcuredStockPurchaseOrder> deliveredOrderList) {
        this.deliveredOrderList = deliveredOrderList;
    }

    public List<ProcuredStockPurchaseOrder> getPaidOrderList() {
        return paidOrderList;
    }

    public void setPaidOrderList(List<ProcuredStockPurchaseOrder> paidOrderList) {
        this.paidOrderList = paidOrderList;
    }

}
