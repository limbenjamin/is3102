/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Purchasing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountInformationBean;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.PurchaseOrderDetail;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Supplier;
import IslandFurniture.EJB.Purchasing.ManagePurchaseOrderLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
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
    
    private PurchaseOrder purchaseOrder;
    private List<PurchaseOrder> purchaseOrderList;
    private PurchaseOrderDetail purchaseOrderDetail;
    private List<PurchaseOrderDetail> purchaseOrderDetailList;    
    private Staff staff;
    private Supplier supplier;
    private Plant plant;
    private List<ProcuredStock> procuredStockList;
    
    @EJB
    private ManageUserAccountInformationBean staffBean; 
    @EJB
    private ManagePurchaseOrderLocal mpol;
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);

        this.purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("POid");
        if (purchaseOrderId != null) {
            purchaseOrder = mpol.getPurchaseOrder(purchaseOrderId);
        }
        System.out.println("@Init PurchaseOrderManaged2Bean:  this is the docomentid" + purchaseOrderId);
        procuredStockList = mpol.viewProcuredStocks();
        purchaseOrderDetailList = mpol.viewPurchaseOrderDetails();
        
        System.out.println("Init");
    }    
    
    public String addPurchaseOrderDetail(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", event.getComponent().getAttributes().get("POid"));
        purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("POid");
        System.out.println("addGoodsReceiptDocumentDetail: " + purchaseOrderId);
        procuredStockId = Long.parseLong("34");
        mpol.createPurchaseOrderDetail(purchaseOrderId, procuredStockId);
        System.out.println("addPurchaseOrderDetail: Created! with " + purchaseOrderId);
        return "purchaseorder2";
    }    
    
    public String editPurchaseOrderDetail(ActionEvent event) throws IOException {
        PurchaseOrderDetail pod = (PurchaseOrderDetail) event.getComponent().getAttributes().get("PODid");
        System.out.println("This is the pod.getId(): " + pod.getId());
        System.out.println("This is the pod.getQuantity(): " + pod.getQuantity());
        System.out.println("This is the pod.getReceivedStock().getId(): " + pod.getProcuredStock().getId());

        mpol.editPurchaseOrderDetail(pod.getId(), pod.getProcuredStock().getId(), pod.getQuantity());
        return "purchaseorder2";
    }    
    
    public String deletePurchaseOrderDetail(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", event.getComponent().getAttributes().get("POid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("PODid", event.getComponent().getAttributes().get("PODid"));
        purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("POid");
        purchaseOrderDetailId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("PODid");
        mpol.deletePurchaseOrderDetail(purchaseOrderDetailId);
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
}
