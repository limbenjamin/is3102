/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockUnit;
import IslandFurniture.EJB.Entities.StorageBin;
import IslandFurniture.EJB.SupplyChain.ManageGoodsIssuedLocal;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMovementLocal;
import IslandFurniture.EJB.SupplyChain.ManageStorageLocationLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
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
    private List<StockUnit> stockUnitByIdList2;
    private List<StockUnit> stockUnitByIdAndGRDList;

    private GoodsIssuedDocument goodsIssuedDocument;
    private StorageBin storageBin;
    private Stock stock;
    private StockUnit stockUnit;
    private Staff staff;
    private Plant plant;

    @EJB
    public ManageGoodsIssuedLocal mgrl;

    @EJB
    public ManageInventoryMovementLocal msul;

    @EJB
    public ManageStorageLocationLocal msll;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        this.goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");

        if (this.goodsIssuedDocumentId == null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            this.goodsIssuedDocumentId = new Long(request.getParameter("createGRDD:GRDid"));
        } else {
            goodsIssuedDocument = mgrl.getGoodsIssuedDocument(goodsIssuedDocumentId);
        }

        System.out.println("GoodsIssuedDocumentId: " + goodsIssuedDocumentId);
        goodsIssuedDocument = mgrl.getGoodsIssuedDocument(goodsIssuedDocumentId);
        stockId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockId");
        stock = mgrl.getStock(stockId);

        stockUnitByIdList = mgrl.viewStockUnitById(plant, stock);
        stockUnitByIdList2 = mgrl.viewStockUnitById2(plant, stock, goodsIssuedDocument);
        stockUnitByIdAndGRDList = mgrl.viewStockUnitByIdAndGrdId(stock, goodsIssuedDocument);

        System.out.println("Init");
    }

    public void addGoodsIssuedDocumentStockUnit(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitId", event.getComponent().getAttributes().get("stockUnitId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnitQuantity", event.getComponent().getAttributes().get("stockUnitQuantity"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        stockUnitId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitId");
        stockUnitQuantity = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnitQuantity");
        goodsIssuedDocument = mgrl.getGoodsIssuedDocument(goodsIssuedDocumentId);
        stockUnit = mgrl.getStockUnit(stockUnitId);

        msul.createStockUnit2(stockUnit.getStock(), stockUnitId, stockUnit.getBatchNo(), stockUnitQuantity, stockUnit.getLocation(), goodsIssuedDocument);
        msul.editStockUnitQuantity(stockUnitId, stockUnit.getQty() - stockUnitQuantity);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockUnit.getStock().getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentcommit.xhtml");
    }

    public void deleteGoodsIssuedDocumentStockUnit(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnit", event.getComponent().getAttributes().get("stockUnit"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        stockUnit = (StockUnit) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnit");
        msul.editStockUnitQuantity(stockUnit.getCommitStockUnitId(), msul.getStockUnit(stockUnit.getCommitStockUnitId()).getQty() + stockUnit.getQty());
        msul.deleteStockUnit(stockUnit.getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockUnit.getStock().getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentcommit.xhtml");
    }

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

    public List<StockUnit> getStockUnitByIdList2() {
        return stockUnitByIdList2;
    }

    public void setStockUnitByIdList2(List<StockUnit> stockUnitByIdList2) {
        this.stockUnitByIdList2 = stockUnitByIdList2;
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

    public ManageGoodsIssuedLocal getMgrl() {
        return mgrl;
    }

    public void setMgrl(ManageGoodsIssuedLocal mgrl) {
        this.mgrl = mgrl;
    }

    public ManageInventoryMovementLocal getMsul() {
        return msul;
    }

    public void setMsul(ManageInventoryMovementLocal msul) {
        this.msul = msul;
    }

    public ManageStorageLocationLocal getMsll() {
        return msll;
    }

    public void setMsll(ManageStorageLocationLocal msll) {
        this.msll = msll;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

}
