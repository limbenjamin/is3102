/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockUnit;
import IslandFurniture.EJB.Entities.StorageArea;
import IslandFurniture.EJB.Entities.StorageBin;
import IslandFurniture.EJB.SupplyChain.ManageGoodsIssuedLocal;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMonitoringLocal;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMovementLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
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
public class InventoryTransferMovementStockManagedBean implements Serializable {

    private Long plantId;
    private Long storageBinId;
    private Long stockId;
    private Long stockTakeQuantity;
    private Long stockUnitId;
    private Long stockUnitQuantity;
    private Long storageAreaId;
    private String batchNumber;

    private boolean ifStockUnitMovementListEmpty;

    private String username;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<StockUnit> stockUnitList;
    private List<StockUnit> stockUnitMovementList;
    private List<StockUnit> stockUnitMovementAnotherList;

    private StorageBin storageBin;
    private StockUnit stockUnit;
    private StockUnit stockUnitOld;
    private StockUnit anotherStockUnit;
    private Stock stock;
    private Staff staff;
    private Plant plant;

    private Calendar commitDate;

    @EJB
    public ManageGoodsIssuedLocal mgrl;
    @EJB
    public ManageInventoryMonitoringLocal miml;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageInventoryMovementLocal msul;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        System.out.println("Init");
        stockId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockId");

        try {
            if (stockId == null) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("inventorytransfer.xhtml");
            }
        } catch (IOException ex) {

        }

        stock = miml.getStock(stockId);
        stockUnitList = mgrl.viewStockUnitById(plant, stock);
        stockUnitMovementList = msul.viewStockUnitMovement(plant, stock);
        storageAreaList = miml.viewStorageArea(plant);
        stockTakeQuantity = null;
        ifStockUnitMovementListEmpty = stockUnitMovementList.isEmpty();
    }

    public boolean ifBatchNoEmpty(String batchNo) {
        if (batchNo.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void updateBatchNumber(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", event.getComponent().getAttributes().get("stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("batchNumber", event.getComponent().getAttributes().get("batchNumber"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        batchNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("batchNumber");
        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");

        msul.updateBatchNumber(stockUnitId, batchNumber);

        stockUnitMovementList = msul.viewStockUnitMovement(plant, stock);
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementstock.xhtml");

    }

    public void onStorageAreaChange() {
        if (storageAreaId != null) {
            storageBinList = miml.viewStorageBin(storageAreaId);
        }
    }

    public String editStockTakeQuantity(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", event.getComponent().getAttributes().get("stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockTakeQuantity", event.getComponent().getAttributes().get("stockTakeQuantity"));
        stockId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockId");
        stock = miml.getStock(stockId);
        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
        stockTakeQuantity = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockTakeQuantity");
        miml.editStockUnitQuantity(stockUnitId, stockTakeQuantity);
        stockTakeQuantity = null;
        stockUnitList = mgrl.viewStockUnitById(plant, stock);
        return "inventorymonitoring_ststock";
    }

    public void addStockUnitTemp(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", event.getComponent().getAttributes().get("stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitQuantity", event.getComponent().getAttributes().get("stockUnitQuantity"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", event.getComponent().getAttributes().get("storageBinId"));

        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
        stockUnit = mgrl.getStockUnit(stockUnitId);
        stockUnitQuantity = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitQuantity");
        storageBinId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("storageBinId");
        storageBin = msul.getStorageBin(storageBinId);

        if (stockUnitQuantity > stockUnit.getQty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "The quantity indicated has to be lesser than or equal to the current Stock Unit's quantity. Moving of stock was unsuccessful.", ""));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", stockUnit.getLocation().getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementstock.xhtml");
        } else {
            msul.createStockUnitMovement1(stockUnit.getStock(), stockUnitId, stockUnit.getBatchNo(), stockUnitQuantity, stockUnit.getLocation(), storageBin);
            msul.editStockUnitQuantity(stockUnitId, stockUnit.getQty() - stockUnitQuantity);
            FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementstock.xhtml");
        }
    }

    public void deleteStockUnitTemp(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", event.getComponent().getAttributes().get("stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnit", event.getComponent().getAttributes().get("stockUnit"));
        stockUnit = (StockUnit) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnit");
        msul.editStockUnitQuantity(stockUnit.getCommitStockUnitId(), msul.getStockUnit(stockUnit.getCommitStockUnitId()).getQty() + stockUnit.getQty());
        msul.deleteStockUnit(stockUnit.getId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementstock.xhtml");
    }

    public void confirmStockUnit(ActionEvent event) throws IOException, Exception {
        for (StockUnit g : stockUnitMovementList) {

            stockUnitMovementAnotherList = msul.viewStockUnitMovementCheck(g.getPendingLocation(), g.getStock(), g.getBatchNo());

            if (!stockUnitMovementAnotherList.isEmpty()) {
                System.out.println("The stockUnitMovementAnotherList is not empty");
                anotherStockUnit = stockUnitMovementAnotherList.get(0);
                System.out.println("There is a stockUnit: " + anotherStockUnit);
                msul.editStockUnitQuantity(anotherStockUnit.getId(), anotherStockUnit.getQty() + g.getQty());
                System.out.println("The quantity is now updated from " + anotherStockUnit.getQty() + "to " + g.getQty());
                msul.deleteStockUnit(g.getId());
            } else {
                msul.confirmStockUnitMovement(g.getId());
            }

            // Start: To check if Quantity = 0
            stockUnitOld = miml.getStockUnit(g.getCommitStockUnitId());
            if (stockUnitOld.getQty() == 0) {
                msul.deleteStockUnit(stockUnitOld.getId());
                // End
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Stock movement has been completed successfully", ""));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", event.getComponent().getAttributes().get("stockId"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_movementstock.xhtml");
    }

    public List<StockUnit> getStockUnitMovementAnotherList() {
        return stockUnitMovementAnotherList;
    }

    public void setStockUnitMovementAnotherList(List<StockUnit> stockUnitMovementAnotherList) {
        this.stockUnitMovementAnotherList = stockUnitMovementAnotherList;
    }

    public StockUnit getAnotherStockUnit() {
        return anotherStockUnit;
    }

    public void setAnotherStockUnit(StockUnit anotherStockUnit) {
        this.anotherStockUnit = anotherStockUnit;
    }

    public StockUnit getStockUnitOld() {
        return stockUnitOld;
    }

    public void setStockUnitOld(StockUnit stockUnitOld) {
        this.stockUnitOld = stockUnitOld;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
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

    public Long getStockTakeQuantity() {
        return stockTakeQuantity;
    }

    public void setStockTakeQuantity(Long stockTakeQuantity) {
        this.stockTakeQuantity = stockTakeQuantity;
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

    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
    }

    public boolean isIfStockUnitMovementListEmpty() {
        return ifStockUnitMovementListEmpty;
    }

    public void setIfStockUnitMovementListEmpty(boolean ifStockUnitMovementListEmpty) {
        this.ifStockUnitMovementListEmpty = ifStockUnitMovementListEmpty;
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

    public List<StockUnit> getStockUnitMovementList() {
        return stockUnitMovementList;
    }

    public void setStockUnitMovementList(List<StockUnit> stockUnitMovementList) {
        this.stockUnitMovementList = stockUnitMovementList;
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

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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

    public Calendar getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Calendar commitDate) {
        this.commitDate = commitDate;
    }

    public ManageGoodsIssuedLocal getMgrl() {
        return mgrl;
    }

    public void setMgrl(ManageGoodsIssuedLocal mgrl) {
        this.mgrl = mgrl;
    }

    public ManageInventoryMonitoringLocal getMiml() {
        return miml;
    }

    public void setMiml(ManageInventoryMonitoringLocal miml) {
        this.miml = miml;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageInventoryMovementLocal getMsul() {
        return msul;
    }

    public void setMsul(ManageInventoryMovementLocal msul) {
        this.msul = msul;
    }

}
