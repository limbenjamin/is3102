/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorageLocationLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryLocal;
import IslandFurniture.Entities.ReplenishmentTransferOrder;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.Entities.StorefrontInventory;
import IslandFurniture.Entities.StorefrontInventoryPK;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
public class InventoryTransferReplenishManagedBean implements Serializable {

    private Long storageBinId;
    private Long stockId;
    private Long stockUnitId;
    private Integer quantity;
    private Long rtoId;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<StorefrontInventory> stockList;
    private List<StockUnit> stockUnitList;
    private List<ReplenishmentTransferOrder> replenishmentTransferOrderListRequestList;
    private List<ReplenishmentTransferOrder> replenishmentTransferOrderListFulfilledList;

    private StockUnit stockUnitOld;

    private String username;
    private Staff staff;
    private Plant plant;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    public ManageStorageLocationLocal storageBean;
    @EJB
    public ManageStorefrontInventoryLocal storefrontInventoryBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
//        storageAreaList = storageBean.viewStorageArea(plant);
        stockList = storefrontInventoryBean.viewStorefrontInventory(plant);
        replenishmentTransferOrderListRequestList = transferBean.viewReplenishmentTransferOrderRequested(plant);
        replenishmentTransferOrderListFulfilledList = transferBean.viewReplenishmentTransferOrderFulfilled(plant);
    }

////  Function: To display Storage Bins in the particular Storage Area -- For AJAX    
//    public void onStorageAreaChange() {
//        if (storageAreaId != null) {
//            storageBinList = storageBean.viewStorageBinsOfAStorageArea(storageAreaId);
//        }
//    }
//  Function: To create a Replenishment Tranfer Order
    public void createReplenishmentTransferOrder(ActionEvent event) throws IOException {
        if (!replenishmentTransferOrderListRequestList.isEmpty()) {
            for (ReplenishmentTransferOrder r : replenishmentTransferOrderListRequestList) {
                List<ReplenishmentTransferOrder> checkList = transferBean.viewReplenishmentTransferOrderRequestedForAParticularStock(plant, transferBean.getStock(stockId));
                if (!checkList.isEmpty()) {
                    ReplenishmentTransferOrder to = checkList.get(0);
                    transferBean.editReplenishmentTransferOrderQuantity(to.getId(), quantity + to.getQty());
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "The quantity for the existing Transfer Order for a " + to.getStock().getName() + " has been updated to reflect the increase of quantity.", ""));
                    return;
                }
            }
        }
        transferBean.createReplenishmentTransferOrder(plant, transferBean.getStock(stockId), quantity);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Transfer Order for a " + transferBean.getStock(stockId).getName() + " has been created.", ""));
    }

//  Function: To delete a Replenishment Transfer Order  
    public void deleteReplenishmentTransferOrder(ActionEvent event) throws IOException {
        transferBean.deleteReplenishmentTransferOrder((Long) event.getComponent().getAttributes().get("toId"));
    }

//  Function: To update a Replenishment Transfer Order  
    public void updateReplenishmentTransferOrder(ActionEvent event) throws IOException {
        ReplenishmentTransferOrder rto = transferBean.getReplenishmentTransferOrder(rtoId);
        StorefrontInventory si = storefrontInventoryBean.getStorefrontInventory(plant, rto.getStock().getId());
        transferBean.editStockUnitQuantity(stockUnitId, transferBean.getStockUnit(stockUnitId).getQty() - rto.getQty());
        storefrontInventoryBean.editStorefrontInventoryQty(si, rto.getQty() + si.getQty());
        transferBean.editReplenishmentTransferOrderStatusToRequestFulfilled(rto);
        
        // Start: To check if Quantity = 0
        stockUnitOld = transferBean.getStockUnit(stockUnitId);
        if (stockUnitOld.getQty() == 0) {
            transferBean.deleteStockUnit(stockUnitOld.getId());
        }
        // End
    }
    
    public void createReplenishmentTransferOrderforPOS(ActionEvent event) throws IOException {
        transferBean.createReplenishmentTransferOrder(plant, transferBean.getStock(stockId), quantity);
    }
 
    public StockUnit getStockUnitOld() {
        return stockUnitOld;
    }

    public void setStockUnitOld(StockUnit stockUnitOld) {
        this.stockUnitOld = stockUnitOld;
    }

//  Function: To display Storage Bins of the particular Stock only -- For AJAX    
    public void onStockChange() {
        if (rtoId != null) {
            storageBinList = storageBean.viewStorageBinsOfAStock(plant, transferBean.getReplenishmentTransferOrder(rtoId).getStock().getId());
        }
    }

//  Function: To display Stock Unit of the particular Stock only -- For AJAX    
    public void onStorageBinChange() {
        if (storageBinId != null) {
            stockUnitList = transferBean.viewStockUnitByStockId(transferBean.getReplenishmentTransferOrder(rtoId).getStock().getId(), storageBinId);
        }
    }

    public Long getRtoId() {
        return rtoId;
    }

    public void setRtoId(Long rtoId) {
        this.rtoId = rtoId;
    }

    public Long getStockUnitId() {
        return stockUnitId;
    }

    public void setStockUnitId(Long stockUnitId) {
        this.stockUnitId = stockUnitId;
    }

    public List<StockUnit> getStockUnitList() {
        return stockUnitList;
    }

    public void setStockUnitList(List<StockUnit> stockUnitList) {
        this.stockUnitList = stockUnitList;
    }

    public Long getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Long storageBinId) {
        this.storageBinId = storageBinId;
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

    public List<StorefrontInventory> getStockList() {
        return stockList;
    }

    public void setStockList(List<StorefrontInventory> stockList) {
        this.stockList = stockList;
    }

    public ManageStorefrontInventoryLocal getStorefrontInventoryBean() {
        return storefrontInventoryBean;
    }

    public void setStorefrontInventoryBean(ManageStorefrontInventoryLocal storefrontInventoryBean) {
        this.storefrontInventoryBean = storefrontInventoryBean;
    }

    public List<ReplenishmentTransferOrder> getReplenishmentTransferOrderListRequestList() {
        return replenishmentTransferOrderListRequestList;
    }

    public void setReplenishmentTransferOrderListRequestList(List<ReplenishmentTransferOrder> replenishmentTransferOrderListRequestList) {
        this.replenishmentTransferOrderListRequestList = replenishmentTransferOrderListRequestList;
    }

    public List<ReplenishmentTransferOrder> getReplenishmentTransferOrderListFulfilledList() {
        return replenishmentTransferOrderListFulfilledList;
    }

    public void setReplenishmentTransferOrderListFulfilledList(List<ReplenishmentTransferOrder> replenishmentTransferOrderListFulfilledList) {
        this.replenishmentTransferOrderListFulfilledList = replenishmentTransferOrderListFulfilledList;
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
