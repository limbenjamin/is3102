package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockUnit;
import IslandFurniture.EJB.Entities.StorageArea;
import IslandFurniture.EJB.Entities.StorageBin;
import IslandFurniture.EJB.SupplyChain.ManageGoodsIssuedLocal;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMonitoringLocal;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMovementLocal;
import IslandFurniture.EJB.SupplyChain.ManageStorageLocationLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private String username;
    private String deliverynote;

    private Calendar postingDate;
    private Calendar issuedDate;

    private Stock stock;
    private Integer quantity;

    private List<GoodsIssuedDocument> goodsIssuedDocumentList;
    private List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetailList;
    private List<StockUnit> stockUnitList;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;

    private GoodsIssuedDocument goodsIssuedDocument;
    private Staff staff;
    private Plant plant;
    private StorageBin storageBin;

    @EJB
    public ManageGoodsIssuedLocal mgrl;

    @EJB
    public ManageInventoryMovementLocal msul;

    @EJB
    public ManageStorageLocationLocal msll;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @EJB
    public ManageInventoryMonitoringLocal miml;

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

//      Ask Ben = To ask on how to display current date
//      issuedDateString = goodsIssuedDocument.getPostingDate().getTime().toString();
        System.out.println("GoodsIssuedDocumentId: " + goodsIssuedDocumentId);
        goodsIssuedDocument = mgrl.getGoodsIssuedDocument(goodsIssuedDocumentId);
        storageBinList = mgrl.viewStorageBin(plant);
        goodsIssuedDocumentDetailList = mgrl.viewGoodsIssuedDocumentDetail(goodsIssuedDocument);
        goodsIssuedDocumentList = mgrl.viewGoodsIssuedDocumentIndividual(goodsIssuedDocument);
        stockUnitList = miml.viewStockUnit(plant);
        System.out.println("Init");
    }

    public String krefresh() {
        try {
            init();
        } catch (Exception ex) {
        }
        return "";
    }

    public void addGoodsIssuedDocumentDetail(ActionEvent event) {
        System.out.println(goodsIssuedDocumentId == null);

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        mgrl.createGoodsIssuedDocumentDetail(goodsIssuedDocumentId, Long.parseLong(request.getParameter("createGRDD:stockId")), Integer.parseInt(request.getParameter("createGRDD:stockQuantity")));
        goodsIssuedDocumentDetailList = mgrl.viewGoodsIssuedDocumentDetail(goodsIssuedDocument);
    }

    public String editGoodsIssuedDocument(ActionEvent event) throws ParseException {
        GoodsIssuedDocument grd = (GoodsIssuedDocument) event.getComponent().getAttributes().get("grd");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("date", event.getComponent().getAttributes().get("date"));
        issuedDateString = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("date");
        issuedDateType = new SimpleDateFormat("yyyy-MM-dd").parse(issuedDateString);
        Calendar issuedDateCal = Calendar.getInstance();
        Date date = issuedDateType;
        issuedDateCal.setTime(date);
        mgrl.editGoodsIssuedDocument(grd.getId(), issuedDateCal);
        return "goodsissueddocument";
    }

    public String editGoodsIssuedDocumentDetail(ActionEvent event) throws IOException {
        GoodsIssuedDocumentDetail sa = (GoodsIssuedDocumentDetail) event.getComponent().getAttributes().get("grddId");
        mgrl.editGoodsIssuedDocumentDetail(sa.getId(), sa.getStock().getId(), sa.getQuantity());
        return "goodsissueddocument";
    }

    public String deleteGoodsIssuedDocumentDetail(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("grddId", event.getComponent().getAttributes().get("grddId"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsIssuedDocumentDetailId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("grddId");
        mgrl.deleteGoodsIssuedDocumentDetail(goodsIssuedDocumentDetailId);
        goodsIssuedDocumentDetailList = mgrl.viewGoodsIssuedDocumentDetail(goodsIssuedDocument);
        return "goodsissueddocument";
    }

    public void deleteGoodsIssuedDocumentStockUnit(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("grddId", event.getComponent().getAttributes().get("grddId"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsIssuedDocumentDetailId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("grddId");
        for (GoodsIssuedDocumentDetail g : goodsIssuedDocumentDetailList) {
            msul.deleteStockUnitQty(stockId, plantId);
        }

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        postingDate = cal;

        mgrl.createGoodsIssuedDocumentStockUnit(goodsIssuedDocumentId, postingDate);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentposted.xhtml");

    }

    public void viewStockUnit() throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        stockId = Long.parseLong(request.getParameter("createGRDD:stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockId);
        goodsIssuedDocumentId = Long.parseLong(request.getParameter("createGRDD:GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        System.out.println("It went here!");
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentcommit.xhtml");
//        return "goodsissueddocumentcommit?faces-redirect=true";
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

    public StorageBin getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(StorageBin storageBin) {
        this.storageBin = storageBin;
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

    public ManageInventoryMonitoringLocal getMiml() {
        return miml;
    }

    public void setMiml(ManageInventoryMonitoringLocal miml) {
        this.miml = miml;
    }

}
