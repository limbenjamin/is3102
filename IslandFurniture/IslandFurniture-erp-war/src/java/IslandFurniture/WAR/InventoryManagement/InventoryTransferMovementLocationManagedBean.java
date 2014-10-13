/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class InventoryTransferMovementLocationManagedBean implements Serializable {

    private Long plantId;
    private Long storageBinId;
    private Long stockId;
    private Long stockUnitId;
    private Long stockUnitQuantity;
    private String username;
    private Long storageAreaId;
    private String batchNumber;

    private List<StorageArea> storageAreaList;
    private List<StorageBin> storageBinList;
    private List<StockUnit> stockUnitList;
    private List<StockUnit> stockUnitMovementList;
    private List<StockUnit> stockUnitMovementAnotherList;
    private List<Stock> stockList;
    private List<StockUnit> stockUnitListOfTheStockOnly;

    private StorageBin storageBin;
    private Stock stock;
    private StockUnit stockUnit;
    private StockUnit stockUnitOld;
    private StockUnit anotherStockUnit;
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
        storageBinId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("storageBinId");

        try {
            if (storageBinId == null) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("inventorytransfer.xhtml");
            }
        } catch (IOException ex) {
        }

        storageBin = storageBean.getStorageBin(storageBinId);
        storageAreaList = storageBean.viewStorageArea(plant);
        stockUnitList = transferBean.viewStockUnitByStorageBin(plant, storageBin);
        stockUnitMovementList = transferBean.viewStockUnitMovementbyStorageBin(storageBin);
    }

//  Function: Boolean for rendering the Batch Number Field - To not allow editing after Batch Number has been set    
    public boolean ifBatchNoEmpty(String batchNo) {
        if (batchNo.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

//  Function: To display Storage Bins in the particular Storage Area -- For AJAX    
    public void onStorageAreaChange() {
        if (storageAreaId != null) {
            storageBinList = storageBean.viewStorageBinsOfAStorageArea(storageAreaId);
            storageBinList.remove(storageBin);
        }
    }

//  Function: To display Stock Unit of the particular Stock only -- For AJAX    
    public void onStockChange() {
        if (stockId != null) {
            stockUnitListOfTheStockOnly = transferBean.viewStockUnitByStockId(stockId, storageBinId);
        }
    }

//  Function: To update the Batch Number    
    public void updateBatchNumber(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", event.getComponent().getAttributes().get("storageBinId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("batchNumber", event.getComponent().getAttributes().get("batchNumber"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        batchNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("batchNumber");
        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
        transferBean.updateBatchNumber(stockUnitId, batchNumber);
        stockUnitList = transferBean.viewStockUnitByStorageBin(plant, storageBin);
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementlocation.xhtml");
    }

//  Function: To create Stock Units that are of Pending Movement        
    public void addStockUnitPendingMovement(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", event.getComponent().getAttributes().get("stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitQuantity", event.getComponent().getAttributes().get("stockUnitQuantity"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", event.getComponent().getAttributes().get("storageBinId"));

        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
        stockUnit = transferBean.getStockUnit(stockUnitId);  
        stockUnitQuantity = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitQuantity");
        storageBinId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("storageBinId");
        storageBin = storageBean.getStorageBin(storageBinId);

        if (stockUnitQuantity > stockUnit.getQty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "The quantity indicated has to be lesser than or equal to the current Stock Unit's quantity. Moving of stock was unsuccessful.", ""));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", stockUnit.getLocation().getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementlocation.xhtml");
        } else if (stockUnit.getBatchNo() == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "The Stock Unit's batch number is empty. Please update it. Moving of stock was unsuccessful.", ""));
             FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", stockUnit.getLocation().getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementlocation.xhtml");
        
        } else {
            transferBean.createStockUnitMovement1(stockUnit.getStock(), stockUnitId, stockUnit.getBatchNo(), stockUnitQuantity, stockUnit.getLocation(), storageBin);
            transferBean.editStockUnitQuantity(stockUnitId, stockUnit.getQty() - stockUnitQuantity);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", stockUnit.getLocation().getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementlocation.xhtml");
        }
    }

//  Function: To delete the movement of a Stock Unit; usually the movement is currently pending    
    public void deleteStockUnitMovement(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", event.getComponent().getAttributes().get("stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnit", event.getComponent().getAttributes().get("stockUnit"));
        stockUnit = (StockUnit) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnit");
        transferBean.editStockUnitQuantity(stockUnit.getCommitStockUnitId(), transferBean.getStockUnit(stockUnit.getCommitStockUnitId()).getQty() + stockUnit.getQty());
        transferBean.deleteStockUnit(stockUnit.getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", stockUnit.getLocation().getId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementlocation.xhtml");
    }

//  Function: To confirm the movement of a Stock Unit  
    public void confirmStockUnitMovement(ActionEvent event) throws IOException, Exception {
        for (StockUnit g : stockUnitMovementList) {
            stockUnitMovementAnotherList = transferBean.viewStockUnitMovementCheck(g.getPendingLocation(), g.getStock(), g.getBatchNo());
            if (!stockUnitMovementAnotherList.isEmpty()) {
                System.out.println("The stockUnitMovementAnotherList is not empty");
                anotherStockUnit = stockUnitMovementAnotherList.get(0);
                System.out.println("There is a stockUnit: " + anotherStockUnit);
                transferBean.editStockUnitQuantity(anotherStockUnit.getId(), anotherStockUnit.getQty() + g.getQty());
                System.out.println("The quantity is now updated from " + anotherStockUnit.getQty() + "to " + g.getQty());
                transferBean.deleteStockUnit(g.getId());
            } else {
                transferBean.confirmStockUnitMovement(g.getId());
            }

            // Start: To check if Quantity = 0
            stockUnitOld = transferBean.getStockUnit(g.getCommitStockUnitId());
            if (stockUnitOld.getQty() == 0) {
                transferBean.deleteStockUnit(stockUnitOld.getId());
                // End
            }
        }

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Stock movement has been completed successfully", ""));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", storageBinId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementlocation.xhtml");
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

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStockUnitId() {
        return stockUnitId;
    }

    public void setStockUnitId(Long stockUnitId) {
        this.stockUnitId = stockUnitId;
    }

    public Long getStockUnitQuantity() {
        return stockUnitQuantity;
    }

    public void setStockUnitQuantity(Long stockUnitQuantity) {
        this.stockUnitQuantity = stockUnitQuantity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public List<StorageArea> getStorageAreaList() {
        return storageAreaList;
    }

    public void setStorageAreaList(List<StorageArea> storageAreaList) {
        this.storageAreaList = storageAreaList;
    }

    public List<StorageBin> getStorageBinList() {
        return storageBinList;
    }

    public void setStorageBinList(List<StorageBin> storageBinList) {
        this.storageBinList = storageBinList;
    }

    public List<StockUnit> getStockUnitList() {
        return stockUnitList;
    }

    public void setStockUnitList(List<StockUnit> stockUnitList) {
        this.stockUnitList = stockUnitList;
    }

    public List<StockUnit> getStockUnitMovementList() {
        return stockUnitMovementList;
    }

    public void setStockUnitMovementList(List<StockUnit> stockUnitMovementList) {
        this.stockUnitMovementList = stockUnitMovementList;
    }

    public List<StockUnit> getStockUnitMovementAnotherList() {
        return stockUnitMovementAnotherList;
    }

    public void setStockUnitMovementAnotherList(List<StockUnit> stockUnitMovementAnotherList) {
        this.stockUnitMovementAnotherList = stockUnitMovementAnotherList;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public List<StockUnit> getStockUnitListOfTheStockOnly() {
        return stockUnitListOfTheStockOnly;
    }

    public void setStockUnitListOfTheStockOnly(List<StockUnit> stockUnitListOfTheStockOnly) {
        this.stockUnitListOfTheStockOnly = stockUnitListOfTheStockOnly;
    }

    public StorageBin getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(StorageBin storageBin) {
        this.storageBin = storageBin;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }

    public StockUnit getStockUnitOld() {
        return stockUnitOld;
    }

    public void setStockUnitOld(StockUnit stockUnitOld) {
        this.stockUnitOld = stockUnitOld;
    }

    public StockUnit getAnotherStockUnit() {
        return anotherStockUnit;
    }

    public void setAnotherStockUnit(StockUnit anotherStockUnit) {
        this.anotherStockUnit = anotherStockUnit;
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

//    Function: To update Stock Take Quantity    
//    public String editStockTakeQuantity(ActionEvent event) {
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", event.getComponent().getAttributes().get("storageBinId"));
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockTakeQuantity", event.getComponent().getAttributes().get("stockTakeQuantity"));
//        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
//        stockTakeQuantity = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockTakeQuantity");
//        miml.editStockUnitQuantity(stockUnitId, stockTakeQuantity);
//        stockUnitList = miml.viewStockUnit(plant);
//        stockTakeQuantity = null;
//        return "inventorymonitoring_stlocation";
//    }
