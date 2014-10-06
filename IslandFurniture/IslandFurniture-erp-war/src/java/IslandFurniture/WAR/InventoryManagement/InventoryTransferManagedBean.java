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
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorageLocationLocal;
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
public class InventoryTransferManagedBean implements Serializable {

    private Long plantId;
    private Long storageBinId;
    private Long stockUnitId;
    private Long stockId;
    private Long storageAreaId;
    private boolean ifStockUnitMovemementAllListEmpty;

    private String username;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<StockUnit> stockUnitList;
    private List<StockUnit> stockUnitMovemementAllList;

    private StorageBin storageBin;
    private StockUnit stockUnit;
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
        storageAreaList = storageBean.viewStorageArea(plant);
        stockUnitList = transferBean.viewStockUnitDistinctName(plant);
        stockUnitMovemementAllList = transferBean.viewStockUnitMovementAll(plant);
        ifStockUnitMovemementAllListEmpty = stockUnitMovemementAllList.isEmpty();
    }

//  Function: To display Storage Bins in the particular Storage Area -- For AJAX    
    public void onStorageAreaChange() {
        if (storageAreaId != null) {
            storageBinList = storageBean.viewStorageBinsOfAStorageArea(storageAreaId);
        }
    }

//  Function: To delete the Stock Unit that are of pending movement status, ie. not confirmed
    public void deletePendingMovementofStockUnit(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnit", event.getComponent().getAttributes().get("stockUnit"));
        stockUnit = (StockUnit) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnit");
        transferBean.editStockUnitQuantity(stockUnit.getCommitStockUnitId(), transferBean.getStockUnit(stockUnit.getCommitStockUnitId()).getQty() + stockUnit.getQty());
        transferBean.deleteStockUnit(stockUnit.getId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer.xhtml");
    }

//  Function: To confirm the movememt of a Stock Unit that is currently pending    
    public void confirmStockUnitMovement(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
        stockUnit = transferBean.getStockUnit(stockUnitId);
        transferBean.confirmStockUnitMovement(stockUnitId);

        StockUnit stockUnitOld = transferBean.getStockUnit(stockUnit.getCommitStockUnitId());
        if (stockUnitOld.getQty() == 0) {
            transferBean.deleteStockUnit(stockUnitOld.getId());
        }

        stockUnitMovemementAllList = transferBean.viewStockUnitMovementAll(plant);
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer.xhtml");
    }

//  Function: To view Stock Unit by Location
    public String viewStockUnitbyLocation() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", storageBinId);
        return "inventorytransfer_movementlocation?faces-redirect=true";
    }
    
//  Function: To view Stock Unit by Stock
    public String viewStockUnitbyStock() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockId);
        return "inventorytransfer_movementstock?faces-redirect=true";
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Long getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Long storageBinId) {
        this.storageBinId = storageBinId;
    }

    public Long getStockUnitId() {
        return stockUnitId;
    }

    public void setStockUnitId(Long stockUnitId) {
        this.stockUnitId = stockUnitId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
    }

    public boolean isIfStockUnitMovemementAllListEmpty() {
        return ifStockUnitMovemementAllListEmpty;
    }

    public void setIfStockUnitMovemementAllListEmpty(boolean ifStockUnitMovemementAllListEmpty) {
        this.ifStockUnitMovemementAllListEmpty = ifStockUnitMovemementAllListEmpty;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<StockUnit> getStockUnitMovemementAllList() {
        return stockUnitMovemementAllList;
    }

    public void setStockUnitMovemementAllList(List<StockUnit> stockUnitMovemementAllList) {
        this.stockUnitMovemementAllList = stockUnitMovemementAllList;
    }

    public StorageBin getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(StorageBin storageBin) {
        this.storageBin = storageBin;
    }

    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
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

    public ManageInventoryTransferLocal getMsul() {
        return transferBean;
    }

    public void setMsul(ManageInventoryTransferLocal msul) {
        this.transferBean = msul;
    }

    public ManageStorageLocationLocal getStorageBean() {
        return storageBean;
    }

    public void setStorageBean(ManageStorageLocationLocal storageBean) {
        this.storageBean = storageBean;
    }

}
