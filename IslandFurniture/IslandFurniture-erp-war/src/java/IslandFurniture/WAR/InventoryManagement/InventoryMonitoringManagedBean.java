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
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class InventoryMonitoringManagedBean implements Serializable {

    private Long storageBinId;
    private Long storageAreaId;
    private Long stockUnitId;
    private Long stockId;

    private String username;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<StockUnit> stockUnitList;

    private Staff staff;
    private Plant plant;

    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
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
        System.out.println("Init");
    }

//  Function: To display Storage Bins in the particular Storage Area -- For AJAX    
    public void onStorageAreaChange() {
        if (storageAreaId != null) {
            storageBinList = storageBean.viewStorageBinsOfAStorageArea(storageAreaId);
        }
    }

//  Function: To view Stock Units stored in the Storage Bin
    public String viewStorageLocation() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", storageBinId);
        return "inventorymonitoring_location?faces-redirect=true";
    }

//  Function: To conduct Stock Take of Stock Units stored in the Storage Bin    
    public String viewStockTakebyLocation() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", storageBinId);
        return "inventorymonitoring_stlocation?faces-redirect=true";
    }

//  Function: To view Stock Units of a particular Stock
    public String viewStock() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockId);
        return "inventorymonitoring_stock?faces-redirect=true";
    }

//  Function: To conduct Stock Take of a particular Stock   
    public String viewStockTakebyStock() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockId);
        return "inventorymonitoring_ststock?faces-redirect=true";
    }

    public Long getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Long storageBinId) {
        this.storageBinId = storageBinId;
    }

    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
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

    public ManageInventoryTransferLocal getTransferBean() {
        return transferBean;
    }

    public void setTransferBean(ManageInventoryTransferLocal transferBean) {
        this.transferBean = transferBean;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageStorageLocationLocal getStorageBean() {
        return storageBean;
    }

    public void setStorageBean(ManageStorageLocationLocal storageBean) {
        this.storageBean = storageBean;
    }

}
