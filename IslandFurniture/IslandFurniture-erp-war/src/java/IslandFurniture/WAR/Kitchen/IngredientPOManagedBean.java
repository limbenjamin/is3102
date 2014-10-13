/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.IngredientProcurementManagerLocal;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
import IslandFurniture.Entities.IngredientPurchaseOrder;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class IngredientPOManagedBean implements Serializable{

    private String username;

    private Long purchaseOrderId;
    private Long supplierId;

    private IngredientPurchaseOrder purchaseOrder;
    private List<IngredientPurchaseOrder> plannedOrderList;
    private List<IngredientPurchaseOrder> confirmedOrderList;
    private List<IngredientPurchaseOrder> deliveredOrderList;
    private List<IngredientPurchaseOrder> paidOrderList;
    private List<IngredientSupplier> supplierList;
    
    private Staff staff;
    private IngredientSupplier supplier;
    private Store store;
    private String orderDateString = null;    
    
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public IngredientProcurementManagerLocal ipml;
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);

        if (staff.getPlant() instanceof Store) {
            store = (Store) staff.getPlant();
            plannedOrderList = ipml.viewPlannedIngredPurchaseOrders(store);
            confirmedOrderList = ipml.viewConfirmedIngredPurchaseOrders(store);
            deliveredOrderList = ipml.viewDeliveredIngredPurchaseOrders(store);
            paidOrderList = ipml.viewPaidIngredPurchaseOrders(store);
            supplierList = ipml.viewIngredContractedSuppliers(store);
            System.out.println("Init");
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }    
    
    public void purchaseOrderDetailActionListener(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", event.getComponent().getAttributes().get("POid"));
    }

    public void deletePurchaseOrder(ActionEvent event) {
        ipml.deleteIngredPurchaseOrder(new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("POid")));
        plannedOrderList = ipml.viewPlannedIngredPurchaseOrders(store);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient purchase order has been sucessfully deleted", ""));
    }    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public IngredientPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(IngredientPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public List<IngredientPurchaseOrder> getPlannedOrderList() {
        return plannedOrderList;
    }

    public void setPlannedOrderList(List<IngredientPurchaseOrder> plannedOrderList) {
        this.plannedOrderList = plannedOrderList;
    }

    public List<IngredientPurchaseOrder> getConfirmedOrderList() {
        return confirmedOrderList;
    }

    public void setConfirmedOrderList(List<IngredientPurchaseOrder> confirmedOrderList) {
        this.confirmedOrderList = confirmedOrderList;
    }

    public List<IngredientPurchaseOrder> getDeliveredOrderList() {
        return deliveredOrderList;
    }

    public void setDeliveredOrderList(List<IngredientPurchaseOrder> deliveredOrderList) {
        this.deliveredOrderList = deliveredOrderList;
    }

    public List<IngredientPurchaseOrder> getPaidOrderList() {
        return paidOrderList;
    }

    public void setPaidOrderList(List<IngredientPurchaseOrder> paidOrderList) {
        this.paidOrderList = paidOrderList;
    }

    public List<IngredientSupplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<IngredientSupplier> supplierList) {
        this.supplierList = supplierList;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public IngredientSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(IngredientSupplier supplier) {
        this.supplier = supplier;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getOrderDateString() {
        return orderDateString;
    }

    public void setOrderDateString(String orderDateString) {
        this.orderDateString = orderDateString;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public IngredientProcurementManagerLocal getIpml() {
        return ipml;
    }

    public void setIpml(IngredientProcurementManagerLocal ipml) {
        this.ipml = ipml;
    }
    
}
