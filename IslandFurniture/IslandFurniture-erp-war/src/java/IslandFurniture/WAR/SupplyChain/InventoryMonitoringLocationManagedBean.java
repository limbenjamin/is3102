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
import IslandFurniture.EJB.Entities.StorageBin;
import IslandFurniture.EJB.SupplyChain.ManageGoodsIssuedLocal;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMonitoringLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
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
public class InventoryMonitoringLocationManagedBean implements Serializable {

    private Long plantId;
    private Long storageBinId;
    private Long stockId;
    private Long stockTakeQuantity;
    private Long stockUnitId;

    private String username;

    private List<StorageBin> storageBinList;
    private List<StockUnit> stockUnitList;
    private List<StockUnit> stockUnitBinList;
    private boolean ifStockUnitBinListEmpty;

    private StorageBin storageBin;
    private Stock stock;
    private Staff staff;
    private Plant plant;

    @EJB
    public ManageGoodsIssuedLocal mgrl;
    @EJB
    public ManageInventoryMonitoringLocal miml;
    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        stockUnitList = miml.viewStockUnit(plant);
        System.out.println("Init");
        storageBinId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("storageBinId");
        storageBin = miml.getStorageBin(storageBinId);
        stockUnitBinList = miml.viewStockUnitBin(storageBin);
        ifStockUnitBinListEmpty = stockUnitBinList.isEmpty();

    }

    public String editStockTakeQuantity(ActionEvent event) {
        StockUnit su = (StockUnit) event.getComponent().getAttributes().get("su");

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", event.getComponent().getAttributes().get("storageBinId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockTakeQuantity", event.getComponent().getAttributes().get("stockTakeQuantity"));
        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
        stockTakeQuantity = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockTakeQuantity");

        System.out.println("the stock take quantity is: " + stockTakeQuantity);

        miml.editStockUnitQuantity(stockUnitId, stockTakeQuantity);
        stockUnitBinList = miml.viewStockUnitBin(storageBin);
        stockTakeQuantity = null;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The quantity has been updated for Stock Unit Number: " + stockUnitId, ""));
        return "inventorymonitoring_stlocation";
    }

    public boolean isIfStockUnitBinListEmpty() {
        return ifStockUnitBinListEmpty;
    }

    public void setIfStockUnitBinListEmpty(boolean ifStockUnitBinListEmpty) {
        this.ifStockUnitBinListEmpty = ifStockUnitBinListEmpty;
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

    public List<StockUnit> getStockUnitList() {
        return stockUnitList;
    }

    public void setStockUnitList(List<StockUnit> stockUnitList) {
        this.stockUnitList = stockUnitList;
    }

    public List<StockUnit> getStockUnitBinList() {
        return stockUnitBinList;
    }

    public void setStockUnitBinList(List<StockUnit> stockUnitBinList) {
        this.stockUnitBinList = stockUnitBinList;
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

}
