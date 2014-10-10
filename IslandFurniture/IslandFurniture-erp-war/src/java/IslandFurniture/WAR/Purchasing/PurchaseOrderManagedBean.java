/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Purchasing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.PurchaseOrder;
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

    private PurchaseOrder purchaseOrder;
    private List<PurchaseOrder> plannedOrderList;
    private List<PurchaseOrder> confirmedOrderList;
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

    public void addPurchaseOrder() throws ParseException {
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

}
