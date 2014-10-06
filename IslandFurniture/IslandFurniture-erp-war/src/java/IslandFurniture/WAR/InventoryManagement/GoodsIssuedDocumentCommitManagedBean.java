/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.EJB.InventoryManagement.ManageGoodsIssuedLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
public class GoodsIssuedDocumentCommitManagedBean implements Serializable {

    private Long plantId;
    private Long goodsIssuedDocumentId;
    private Long stockId;
    private Long storageBinId;
    private Long storageAreaid;
    private Long stockUnitId;
    private Long oldStockUnitId;

    private String issuedDateString;
    private Date issuedDateType;

    private String username;
    private String deliverynote;

    private Calendar commitDate;
    private Calendar issuedDate;

    private Long stockUnitQuantity;

    private List<StockUnit> stockUnitByIdList;
    private List<StockUnit> stockUnitListPendingMovementAtGIDForAParticularStock;
    private List<StockUnit> stockUnitByIdAndGRDList;

    private GoodsIssuedDocument goodsIssuedDocument;
    private StorageBin storageBin;
    private Stock stock;
    private StockUnit stockUnit;
    private Staff staff;
    private Plant plant;

    @EJB
    public ManageGoodsIssuedLocal issuedBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        this.goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");

        try {
            if (goodsIssuedDocumentId == null) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("goodsissued.xhtml");
            }
        } catch (IOException ex) {
        }
        goodsIssuedDocument = issuedBean.getGoodsIssuedDocument(goodsIssuedDocumentId);
        goodsIssuedDocument = issuedBean.getGoodsIssuedDocument(goodsIssuedDocumentId);
        stockId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockId");
        stock = transferBean.getStock(stockId);
        stockUnitByIdList = transferBean.viewStockUnitsOfAStock(plant, stock);
        stockUnitListPendingMovementAtGIDForAParticularStock = issuedBean.viewStockUnitPendingMovementAtGIDForAParticularStock(stock, goodsIssuedDocument);
        stockUnitByIdAndGRDList = issuedBean.viewStockUnitByStockandGID(stock, goodsIssuedDocument);

    }

//  Function: To create Stock Unit pending movement at Goods Issued Document    
    public void addStockUnitPendingAtGoodsIssuedDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitQuantity", event.getComponent().getAttributes().get("stockUnitQuantity"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
        stockUnitQuantity = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitQuantity");
        goodsIssuedDocument = issuedBean.getGoodsIssuedDocument(goodsIssuedDocumentId);
        stockUnit = transferBean.getStockUnit(stockUnitId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockUnit.getStock().getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);

        if (stockUnitQuantity > stockUnit.getQty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "The quantity indicated has to be lesser than or equal to the current Stock Unit's quantity. Moving of stock was unsuccessful.", ""));
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentcommit.xhtml");
        } else {
            transferBean.createStockUnit2(stockUnit.getStock(), stockUnitId, stockUnit.getBatchNo(), stockUnitQuantity, stockUnit.getLocation(), goodsIssuedDocument);
            transferBean.editStockUnitQuantity(stockUnitId, stockUnit.getQty() - stockUnitQuantity);
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentcommit.xhtml");
        }
    }

//  Function: To delete Stock Unit pending at the Goods Issued Document    
    public void deleteStockUnitPendingAtGoodsIssuedDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnit", event.getComponent().getAttributes().get("stockUnit"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        stockUnit = (StockUnit) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnit");
        transferBean.editStockUnitQuantity(stockUnit.getCommitStockUnitId(), transferBean.getStockUnit(stockUnit.getCommitStockUnitId()).getQty() + stockUnit.getQty());
        transferBean.deleteStockUnit(stockUnit.getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockUnit.getStock().getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentcommit.xhtml");
    }

// Function: To continue at Goods Issued Document    
    public void continueWithGoodsIssueDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocument.xhtml");
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Long getGoodsIssuedDocumentId() {
        return goodsIssuedDocumentId;
    }

    public void setGoodsIssuedDocumentId(Long goodsIssuedDocumentId) {
        this.goodsIssuedDocumentId = goodsIssuedDocumentId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Long storageBinId) {
        this.storageBinId = storageBinId;
    }

    public Long getStorageAreaid() {
        return storageAreaid;
    }

    public void setStorageAreaid(Long storageAreaid) {
        this.storageAreaid = storageAreaid;
    }

    public Long getStockUnitId() {
        return stockUnitId;
    }

    public void setStockUnitId(Long stockUnitId) {
        this.stockUnitId = stockUnitId;
    }

    public Long getOldStockUnitId() {
        return oldStockUnitId;
    }

    public void setOldStockUnitId(Long oldStockUnitId) {
        this.oldStockUnitId = oldStockUnitId;
    }

    public String getIssuedDateString() {
        return issuedDateString;
    }

    public void setIssuedDateString(String issuedDateString) {
        this.issuedDateString = issuedDateString;
    }

    public Date getIssuedDateType() {
        return issuedDateType;
    }

    public void setIssuedDateType(Date issuedDateType) {
        this.issuedDateType = issuedDateType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeliverynote() {
        return deliverynote;
    }

    public void setDeliverynote(String deliverynote) {
        this.deliverynote = deliverynote;
    }

    public Calendar getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Calendar commitDate) {
        this.commitDate = commitDate;
    }

    public Calendar getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Calendar issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Long getStockUnitQuantity() {
        return stockUnitQuantity;
    }

    public void setStockUnitQuantity(Long stockUnitQuantity) {
        this.stockUnitQuantity = stockUnitQuantity;
    }

    public List<StockUnit> getStockUnitByIdList() {
        return stockUnitByIdList;
    }

    public void setStockUnitByIdList(List<StockUnit> stockUnitByIdList) {
        this.stockUnitByIdList = stockUnitByIdList;
    }

    public List<StockUnit> getStockUnitListPendingMovementAtGIDForAParticularStock() {
        return stockUnitListPendingMovementAtGIDForAParticularStock;
    }

    public void setStockUnitListPendingMovementAtGIDForAParticularStock(List<StockUnit> stockUnitListPendingMovementAtGIDForAParticularStock) {
        this.stockUnitListPendingMovementAtGIDForAParticularStock = stockUnitListPendingMovementAtGIDForAParticularStock;
    }

    public List<StockUnit> getStockUnitByIdAndGRDList() {
        return stockUnitByIdAndGRDList;
    }

    public void setStockUnitByIdAndGRDList(List<StockUnit> stockUnitByIdAndGRDList) {
        this.stockUnitByIdAndGRDList = stockUnitByIdAndGRDList;
    }

    public GoodsIssuedDocument getGoodsIssuedDocument() {
        return goodsIssuedDocument;
    }

    public void setGoodsIssuedDocument(GoodsIssuedDocument goodsIssuedDocument) {
        this.goodsIssuedDocument = goodsIssuedDocument;
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

    public ManageGoodsIssuedLocal getIssuedBean() {
        return issuedBean;
    }

    public void setIssuedBean(ManageGoodsIssuedLocal issuedBean) {
        this.issuedBean = issuedBean;
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

}
