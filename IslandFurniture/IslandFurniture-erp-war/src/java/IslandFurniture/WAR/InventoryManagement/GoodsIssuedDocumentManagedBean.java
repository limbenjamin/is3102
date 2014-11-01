package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.EJB.InventoryManagement.ManageGoodsIssuedLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorageLocationLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class GoodsIssuedDocumentManagedBean implements Serializable {

    private Long plantId;
    private Long goodsIssuedDocumentId;
    private Long goodsIssuedDocumentDetailId;
    private Long stockId;
    private Long storageBinId;
    private Long storageAreaid;
    private Long stockUnitId;

    private String issuedDateString;
    private Date issuedDateType;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private String username;
    private String deliverynote;
    private String plantType;
    private String plantType2;
    private String plantTypePosted;

    private Calendar postingDate;
    private Calendar issuedDate;
    private Calendar issuedDateCal;

    private Stock stock;
    private Integer quantity;

    private List<GoodsIssuedDocument> goodsIssuedDocumentList;
    private List<GoodsIssuedDocument> goodsIssuedDocumentListPosted;
    private List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetailList;
    private List<StockUnit> stockUnitList;
    private List<StockUnit> stockUnitMainList;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<Plant> plantList;

    private GoodsIssuedDocument goodsIssuedDocument;
    private Staff staff;
    private Plant plant;
    private Plant plantSendTo;
    private Plant plantSentTo2;
    private StorageBin storageBin;
    private StockUnit stockUnit;
    private StockUnit stockUnitOld;

    @EJB
    public ManageGoodsIssuedLocal issuedBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    public ManageStorageLocationLocal storageBean;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private ManageOrganizationalHierarchyBeanRemote orgBean;

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
        storageBinList = storageBean.viewStorageBin(plant);
        goodsIssuedDocumentDetailList = issuedBean.viewGoodsIssuedDocumentDetail(goodsIssuedDocument);
        goodsIssuedDocumentList = issuedBean.viewGoodsIssuedDocumentIndividual(goodsIssuedDocument);
        goodsIssuedDocumentListPosted = issuedBean.viewGoodsIssuedDocumentIndividual(goodsIssuedDocument);
        stockUnitList = transferBean.viewStockUnitDistinctName(plant);
        stockUnitMainList = issuedBean.viewStockUnitPendingMovementAtGoodsIssuedDocument(goodsIssuedDocument);
        if (goodsIssuedDocument.getIssuedDate() != null) {
            issuedDateString = df.format(goodsIssuedDocument.getIssuedDate().getTime());
        }

        if (goodsIssuedDocument.getDeliverTo() != null) {
            plantId = goodsIssuedDocument.getDeliverTo().getId();
        }

        if (goodsIssuedDocument.isConfirm() == true) {
            for (GoodsIssuedDocument d : goodsIssuedDocumentList) {

                plantType2 = d.getDeliverTo().getClass().getSimpleName();
                if (plantType2.equals("ManufacturingFacility")) {
                    plantType2 = "MFG";
                } else if (plantType2.equals("CountryOffice")) {
                    plantType2 = "CO";
                } else if (plantType2.equals("GlobalHQ")) {
                    plantType2 = ""; //no need cos global HQ global HQ looks ugly
                }

                plantTypePosted = d.getDeliverTo().getName() + " (" + plantType2 + ")";
            }
        }

        plantList = orgBean.displayPlant();
        plantList.remove(plant);
        for (Plant l : plantList) {
            if (l.getClass().getSimpleName().equals("GlobalHQ")) {
                plantList.remove(l);
                break;
            }
        }

        for (Plant g : plantList) {

            plantType = g.getClass().getSimpleName();
            if (plantType.equals("ManufacturingFacility")) {
                plantType = "MFG";
            } else if (plantType.equals("CountryOffice")) {
                plantType = "CO";
            } else if (plantType.equals("GlobalHQ")) {
                plantType = ""; //no need cos global HQ global HQ looks ugly
            }

            g.setName(g.getName() + " " + plantType);
        }
    }

//  Function: To edit Goods Issued Document
    public String editGoodsIssuedDocument(ActionEvent event) throws ParseException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        GoodsIssuedDocument grd = (GoodsIssuedDocument) event.getComponent().getAttributes().get("grd");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("plantId", event.getComponent().getAttributes().get("plantId"));
        plantId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("plantId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("date", event.getComponent().getAttributes().get("date"));
        issuedDateString = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("date");

        if (plantId == null) {
            plantSendTo = null;
        } else {
            plantSendTo = orgBean.getPlantById(plantId);
        }

        if (issuedDateString.isEmpty()) {
            issuedDateCal = null;
        } else {
            issuedDateType = new SimpleDateFormat("yyyy-MM-dd").parse(issuedDateString);
            issuedDateCal = Calendar.getInstance();
            Date date = issuedDateType;
            issuedDateCal.setTime(date);
        }

        issuedBean.editGoodsIssuedDocument(grd.getId(), issuedDateCal, plantSendTo);
        return "goodsissueddocument";
    }

//  Function: To delete Goods Issued Document Detail
    public String deleteGoodsIssuedDocumentDetail(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("grddId", event.getComponent().getAttributes().get("grddId"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsIssuedDocumentDetailId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("grddId");
        issuedBean.deleteGoodsIssuedDocumentDetail(goodsIssuedDocumentDetailId);
        goodsIssuedDocumentDetailList = issuedBean.viewGoodsIssuedDocumentDetail(goodsIssuedDocument);
        return "goodsissueddocument";
    }

//  Function: To delete Stock Unit pending at Goods Issued Document; usually movememt are pending    
    public void deleteStockUnitPendingAtGoodsIssuedDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnit", event.getComponent().getAttributes().get("stockUnit"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        stockUnit = (StockUnit) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnit");
        transferBean.editStockUnitQuantity(stockUnit.getCommitStockUnitId(), transferBean.getStockUnit(stockUnit.getCommitStockUnitId()).getQty() + stockUnit.getQty());
        transferBean.deleteStockUnit(stockUnit.getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockUnit.getStock().getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocument.xhtml");
    }

//  Function: View Goods Issued Document Stock Unit Availability    
    public void viewStockUnitAvailability() throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        stockId = Long.parseLong(request.getParameter("createGRDD:stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockId);
        goodsIssuedDocumentId = Long.parseLong(request.getParameter("createGRDD:GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentcommit.xhtml");
    }

//  Function: To post the Goods Issued Document    
    public void postGoodsIssuedDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsIssuedDocument = issuedBean.getGoodsIssuedDocument(goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        
        if (goodsIssuedDocument.getIssuedDate() == null && goodsIssuedDocument.getDeliverTo() == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "The 'Issued Date' and 'Issued To' fields need to be updated before the Posting of Goods Issued Document. Posting was unsuccessful.", ""));
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocument.xhtml");
        } else if (goodsIssuedDocument.getIssuedDate() == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "The 'Issued Date' field needs to be updated before the Posting of Goods Issued Document. Posting was unsuccessful.", ""));
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocument.xhtml");
        } else if (goodsIssuedDocument.getDeliverTo() == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "The 'Issued To' field needs to be updated before the Posting of Goods Issued Document. Posting was unsuccessful.", ""));
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocument.xhtml");
        } else {

            Calendar cal = Calendar.getInstance();
            Date date = new Date();
            cal.setTime(date);
            postingDate = cal;

            for (StockUnit g : stockUnitMainList) {
                issuedBean.createGoodsIssuedDocumentDetail(goodsIssuedDocumentId, g.getStock().getId(), g.getQty());
                issuedBean.postGoodsIssuedDocument(goodsIssuedDocumentId, postingDate);
                transferBean.deleteStockUnit(g.getId());

                // Start: To check if Quantity = 0
                stockUnitOld = transferBean.getStockUnit(g.getCommitStockUnitId());
                if (stockUnitOld.getQty() == 0) {
                    transferBean.deleteStockUnit(stockUnitOld.getId());
                }
                // End
            }

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "The Goods Issued Document was successfully created", ""));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentposted.xhtml");
        }
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

    public Long getGoodsIssuedDocumentDetailId() {
        return goodsIssuedDocumentDetailId;
    }

    public void setGoodsIssuedDocumentDetailId(Long goodsIssuedDocumentDetailId) {
        this.goodsIssuedDocumentDetailId = goodsIssuedDocumentDetailId;
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

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
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

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public String getPlantType2() {
        return plantType2;
    }

    public void setPlantType2(String plantType2) {
        this.plantType2 = plantType2;
    }

    public String getPlantTypePosted() {
        return plantTypePosted;
    }

    public void setPlantTypePosted(String plantTypePosted) {
        this.plantTypePosted = plantTypePosted;
    }

    public Calendar getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Calendar postingDate) {
        this.postingDate = postingDate;
    }

    public Calendar getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Calendar issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Calendar getIssuedDateCal() {
        return issuedDateCal;
    }

    public void setIssuedDateCal(Calendar issuedDateCal) {
        this.issuedDateCal = issuedDateCal;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<GoodsIssuedDocument> getGoodsIssuedDocumentList() {
        return goodsIssuedDocumentList;
    }

    public void setGoodsIssuedDocumentList(List<GoodsIssuedDocument> goodsIssuedDocumentList) {
        this.goodsIssuedDocumentList = goodsIssuedDocumentList;
    }

    public List<GoodsIssuedDocument> getGoodsIssuedDocumentListPosted() {
        return goodsIssuedDocumentListPosted;
    }

    public void setGoodsIssuedDocumentListPosted(List<GoodsIssuedDocument> goodsIssuedDocumentListPosted) {
        this.goodsIssuedDocumentListPosted = goodsIssuedDocumentListPosted;
    }

    public List<GoodsIssuedDocumentDetail> getGoodsIssuedDocumentDetailList() {
        return goodsIssuedDocumentDetailList;
    }

    public void setGoodsIssuedDocumentDetailList(List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetailList) {
        this.goodsIssuedDocumentDetailList = goodsIssuedDocumentDetailList;
    }

    public List<StockUnit> getStockUnitList() {
        return stockUnitList;
    }

    public void setStockUnitList(List<StockUnit> stockUnitList) {
        this.stockUnitList = stockUnitList;
    }

    public List<StockUnit> getStockUnitMainList() {
        return stockUnitMainList;
    }

    public void setStockUnitMainList(List<StockUnit> stockUnitMainList) {
        this.stockUnitMainList = stockUnitMainList;
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

    public List<Plant> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<Plant> plantList) {
        this.plantList = plantList;
    }

    public GoodsIssuedDocument getGoodsIssuedDocument() {
        return goodsIssuedDocument;
    }

    public void setGoodsIssuedDocument(GoodsIssuedDocument goodsIssuedDocument) {
        this.goodsIssuedDocument = goodsIssuedDocument;
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

    public Plant getPlantSendTo() {
        return plantSendTo;
    }

    public void setPlantSendTo(Plant plantSendTo) {
        this.plantSendTo = plantSendTo;
    }

    public Plant getPlantSentTo2() {
        return plantSentTo2;
    }

    public void setPlantSentTo2(Plant plantSentTo2) {
        this.plantSentTo2 = plantSentTo2;
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

    public StockUnit getStockUnitOld() {
        return stockUnitOld;
    }

    public void setStockUnitOld(StockUnit stockUnitOld) {
        this.stockUnitOld = stockUnitOld;
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

    public ManageStorageLocationLocal getStorageBean() {
        return storageBean;
    }

    public void setStorageBean(ManageStorageLocationLocal storageBean) {
        this.storageBean = storageBean;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageOrganizationalHierarchyBeanRemote getOrgBean() {
        return orgBean;
    }

    public void setOrgBean(ManageOrganizationalHierarchyBeanRemote orgBean) {
        this.orgBean = orgBean;
    }

  

}
