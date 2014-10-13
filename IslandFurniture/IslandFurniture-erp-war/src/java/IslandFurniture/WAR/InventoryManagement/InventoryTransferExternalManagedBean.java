/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorageLocationLocal;
import IslandFurniture.Entities.ExternalTransferOrder;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;


/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class InventoryTransferExternalManagedBean implements Serializable {

    private Long storageAreaId;
    private Long stockId;
    private Integer quantity;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<StockUnit> stockUnitList;
    private List<ExternalTransferOrder> externalTransferOrderListRequestList;
    private List<ExternalTransferOrder> externalTransferOrderListFulfilledList;

    private ExternalTransferOrder externalTransferOrder;

    private String username;
    private Staff staff;
    private Plant plant;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    public ManageStorageLocationLocal storageBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
//        storageAreaList = storageBean.viewStorageArea(plant);
        stockUnitList = transferBean.viewStockUnitDistinctName(plant);
        externalTransferOrderListRequestList = transferBean.viewExternalTransferOrderRequested(plant);
        externalTransferOrderListFulfilledList = transferBean.viewExternalTransferOrderFulfilled(plant);
    }

//  Function: To create a External Tranfer Order
    public void createExternalTransferOrder(ActionEvent event) throws IOException {
        externalTransferOrder = transferBean.createExternalTransferOrder(plant);
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_externaldetail.xhtml?id=" + externalTransferOrder.getId().toString());
    }

//  Function: To delete a External Transfer Order  
    public void deleteExternalTransferOrder(ActionEvent event) throws IOException {
//        transferBean.deleteExternalTransferOrder((Long) event.getComponent().getAttributes().get("toId"));
    }

    
    
    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<StorageBin> getStorageBinList() {
        return storageBinList;
    }

    public void setStorageBinList(List<StorageBin> storageBinList) {
        this.storageBinList = storageBinList;
    }

    public List<StorageArea> getStorageAreaList() {
        return storageAreaList;
    }

    public void setStorageAreaList(List<StorageArea> storageAreaList) {
        this.storageAreaList = storageAreaList;
    }

    public List<StockUnit> getStockUnitList() {
        return stockUnitList;
    }

    public void setStockUnitList(List<StockUnit> stockUnitList) {
        this.stockUnitList = stockUnitList;
    }

    public List<ExternalTransferOrder> getExternalTransferOrderListRequestList() {
        return externalTransferOrderListRequestList;
    }

    public void setExternalTransferOrderListRequestList(List<ExternalTransferOrder> externalTransferOrderListRequestList) {
        this.externalTransferOrderListRequestList = externalTransferOrderListRequestList;
    }

    public List<ExternalTransferOrder> getExternalTransferOrderListFulfilledList() {
        return externalTransferOrderListFulfilledList;
    }

    public void setExternalTransferOrderListFulfilledList(List<ExternalTransferOrder> externalTransferOrderListFulfilledList) {
        this.externalTransferOrderListFulfilledList = externalTransferOrderListFulfilledList;
    }

    public ExternalTransferOrder getExternalTransferOrder() {
        return externalTransferOrder;
    }

    public void setExternalTransferOrder(ExternalTransferOrder externalTransferOrder) {
        this.externalTransferOrder = externalTransferOrder;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageInventoryTransferLocal getTransferBean() {
        return transferBean;
    }

    public void setTransferBean(ManageInventoryTransferLocal transferBean) {
        this.transferBean = transferBean;
    }

    public ManageStorageLocationLocal getStorageBean() {
        return storageBean;
    }

    public void setStorageBean(ManageStorageLocationLocal storageBean) {
        this.storageBean = storageBean;
    }

}
