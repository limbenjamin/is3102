/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.IngredientProcurementManagerLocal;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientPurchaseOrder;
import IslandFurniture.Entities.IngredientPurchaseOrderDetail;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class IngredientConfirmedPOManagedBean implements Serializable {

    private String username;
    private Long purchaseOrderId;
    private Long purchaseOrderDetailId;    
    private Long supplierId;
    private Long ingredientId;    
    private IngredientPurchaseOrder purchaseOrder;    
    private IngredientSupplier ingredSupplier;
    private Staff staff;
    private Store store;
    private Integer numberOfLots;
    private PurchaseOrderStatus status;    
    
    private Calendar orderDate;
    private String orderDateString;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");    
    private List<Ingredient> ingredientList;
    private List<IngredientPurchaseOrder> purchaseOrderList;
    private IngredientPurchaseOrderDetail purchaseOrderDetail;
    private List<IngredientPurchaseOrderDetail> purchaseOrderDetailList;    
    
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public IngredientProcurementManagerLocal ipml;
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);

        this.purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("POid");
        try {
            if(purchaseOrderId == null) {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect("ingredientpo.xhtml"); 
            }else {
                purchaseOrder = ipml.getPurchaseOrder(purchaseOrderId);
                if (staff.getPlant() instanceof Store) {
                    store = (Store) staff.getPlant();
                }
                System.out.println("@Init PurchaseOrderConfirmedManagedBean:  this is the docomentid " + purchaseOrderId);
                ingredientList = ipml.viewIngredSupplierProcuredStocks(purchaseOrderId, store);
                purchaseOrderDetailList = ipml.viewIngredPurchaseOrderDetails(purchaseOrderId);

                if (purchaseOrder.getOrderDate() != null) {
                    orderDateString = df.format(purchaseOrder.getOrderDate().getTime());
                }

                System.out.println("loaded some lists");
                System.out.println("Init");
            }
        } catch(IOException ex) {
            
        }        
    }
    
    // get corresponding lot size of a procured stock
    public Integer getStockLotSize(Ingredient ingredient) {
        return ipml.getLotSize(ingredient, store);
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

    public Long getPurchaseOrderDetailId() {
        return purchaseOrderDetailId;
    }

    public void setPurchaseOrderDetailId(Long purchaseOrderDetailId) {
        this.purchaseOrderDetailId = purchaseOrderDetailId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public IngredientPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(IngredientPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public IngredientSupplier getIngredSupplier() {
        return ingredSupplier;
    }

    public void setIngredSupplier(IngredientSupplier ingredSupplier) {
        this.ingredSupplier = ingredSupplier;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Integer getNumberOfLots() {
        return numberOfLots;
    }

    public void setNumberOfLots(Integer numberOfLots) {
        this.numberOfLots = numberOfLots;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDateString() {
        return orderDateString;
    }

    public void setOrderDateString(String orderDateString) {
        this.orderDateString = orderDateString;
    }

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<IngredientPurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<IngredientPurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public IngredientPurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(IngredientPurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public List<IngredientPurchaseOrderDetail> getPurchaseOrderDetailList() {
        return purchaseOrderDetailList;
    }

    public void setPurchaseOrderDetailList(List<IngredientPurchaseOrderDetail> purchaseOrderDetailList) {
        this.purchaseOrderDetailList = purchaseOrderDetailList;
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
