package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StorageArea;
import IslandFurniture.EJB.Entities.StorageBin;
import IslandFurniture.EJB.SupplyChain.ManageGoodsReceiptLocal;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMovementLocal;
import IslandFurniture.EJB.SupplyChain.ManageStorageLocationLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
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
public class GoodsReceiptDocumentManagedBean implements Serializable {

    private Long plantId;
    private Long goodsReceiptDocumentId;
    private Long goodsReceiptDocumentDetailId;
    private Long stockId;
    private Long storageBinId;
    private Long storageAreaid;

    private String username;
    private String deliverynote;

    private Calendar postingDate;
    private Calendar documentDate;

    private Stock stock;
    private Integer quantity;

    private List<GoodsReceiptDocument> goodsReceiptDocumentList;
    private List<GoodsReceiptDocumentDetail> goodsReceiptDocumentDetailList;
    private List<Stock> stockList;
    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;

    private GoodsReceiptDocument goodsReceiptDocument;
    private Staff staff;
    private Plant plant;
    private StorageBin storageBin;

    @EJB
    public ManageGoodsReceiptLocal mgrl;

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

        this.goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");

        if (this.goodsReceiptDocumentId == null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            this.goodsReceiptDocumentId = new Long(request.getParameter("createGRDD:GRDid"));
        }

        if (goodsReceiptDocumentId != null) {
            goodsReceiptDocument = mgrl.getGoodsReceiptDocument(goodsReceiptDocumentId);
        }

        System.out.println("GoodsReceiptDocumentId: " + goodsReceiptDocumentId);

        goodsReceiptDocument = mgrl.getGoodsReceiptDocument(goodsReceiptDocumentId);
        storageBinList = mgrl.viewStorageBin(plant);
        goodsReceiptDocumentDetailList = mgrl.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
        stockList = mgrl.viewStock();

        System.out.println("Init");
    }

    public String krefresh() {
        try {
            init();
        } catch (Exception ex) {
        }
        return "";
    }

    public void addGoodsReceiptDocumentDetail(ActionEvent event) {
        System.out.println(goodsReceiptDocumentId == null);

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        mgrl.createGoodsReceiptDocumentDetail(goodsReceiptDocumentId, Long.parseLong(request.getParameter("createGRDD:stockId")), Integer.parseInt(request.getParameter("createGRDD:stockQuantity")));
        goodsReceiptDocumentDetailList = mgrl.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
    }

    public String editGoodsReceiptDocument(ActionEvent event) throws IOException {
         System.out.println("It went here!");
        GoodsReceiptDocument grd = (GoodsReceiptDocument) event.getComponent().getAttributes().get("grd");
        mgrl.editGoodsReceiptDocument(grd.getId(), grd.getReceiptDate(), null, grd.getDeliveryNote());
       
        return "goodsreceiptdocument";
    }

    public String editGoodsReceiptDocumentDetail(ActionEvent event) throws IOException {
        GoodsReceiptDocumentDetail sa = (GoodsReceiptDocumentDetail) event.getComponent().getAttributes().get("grddId");
        mgrl.editGoodsReceiptDocumentDetail(sa.getId(), sa.getReceivedStock().getId(), sa.getQuantity());
        return "goodsreceiptdocument";
    }

    public String deleteGoodsReceiptDocumentDetail(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("grddId", event.getComponent().getAttributes().get("grddId"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsReceiptDocumentDetailId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("grddId");
        mgrl.deleteGoodsReceiptDocumentDetail(goodsReceiptDocumentDetailId);
        goodsReceiptDocumentDetailList = mgrl.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
        return "goodsreceiptdocument";
    }

    public String addGoodsReceiptDocumentStockUnit(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("grddId", event.getComponent().getAttributes().get("grddId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", event.getComponent().getAttributes().get("storageBinId"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsReceiptDocumentDetailId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("grddId");
        storageBinId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("storageBinId");
        System.out.println("This is the storageBinId: " + storageBinId);
        storageBin = msll.getStorageBin(storageBinId);
        for (GoodsReceiptDocumentDetail g : goodsReceiptDocumentDetailList) {
            msul.createStockUnit(g.getReceivedStock(), null, Long.parseLong(g.getQuantity().toString()), storageBin);
        }
        mgrl.createGoodsReceiptDocumentStockUnit(goodsReceiptDocumentId);
        return "goodsreceipt";
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Long getGoodsReceiptDocumentId() {
        return goodsReceiptDocumentId;
    }

    public void setGoodsReceiptDocumentId(Long goodsReceiptDocumentId) {
        this.goodsReceiptDocumentId = goodsReceiptDocumentId;
    }

    public Long getGoodsReceiptDocumentDetailId() {
        return goodsReceiptDocumentDetailId;
    }

    public void setGoodsReceiptDocumentDetailId(Long goodsReceiptDocumentDetailId) {
        this.goodsReceiptDocumentDetailId = goodsReceiptDocumentDetailId;
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

    public Calendar getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Calendar documentDate) {
        this.documentDate = documentDate;
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

    public List<GoodsReceiptDocument> getGoodsReceiptDocumentList() {
        return goodsReceiptDocumentList;
    }

    public void setGoodsReceiptDocumentList(List<GoodsReceiptDocument> goodsReceiptDocumentList) {
        this.goodsReceiptDocumentList = goodsReceiptDocumentList;
    }

    public List<GoodsReceiptDocumentDetail> getGoodsReceiptDocumentDetailList() {
        return goodsReceiptDocumentDetailList;
    }

    public void setGoodsReceiptDocumentDetailList(List<GoodsReceiptDocumentDetail> goodsReceiptDocumentDetailList) {
        this.goodsReceiptDocumentDetailList = goodsReceiptDocumentDetailList;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
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

    public GoodsReceiptDocument getGoodsReceiptDocument() {
        return goodsReceiptDocument;
    }

    public void setGoodsReceiptDocument(GoodsReceiptDocument goodsReceiptDocument) {
        this.goodsReceiptDocument = goodsReceiptDocument;
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

    public ManageGoodsReceiptLocal getMgrl() {
        return mgrl;
    }

    public void setMgrl(ManageGoodsReceiptLocal mgrl) {
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
