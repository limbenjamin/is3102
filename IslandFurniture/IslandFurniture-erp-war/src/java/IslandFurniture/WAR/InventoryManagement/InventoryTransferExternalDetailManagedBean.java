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
import IslandFurniture.Entities.ReplenishmentTransferOrder;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
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
public class InventoryTransferExternalDetailManagedBean implements Serializable {

    private Long storageAreaId;
    private Long stockId;
    private Integer quantity;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<StockUnit> stockUnitList;
    private List<ReplenishmentTransferOrder> replenishmentTransferOrderListRequestList;
    private List<ReplenishmentTransferOrder> replenishmentTransferOrderListFulfilledList;

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

    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
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

    public List<StockUnit> getStockUnitList() {
        return stockUnitList;
    }

    public void setStockUnitList(List<StockUnit> stockUnitList) {
        this.stockUnitList = stockUnitList;
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
