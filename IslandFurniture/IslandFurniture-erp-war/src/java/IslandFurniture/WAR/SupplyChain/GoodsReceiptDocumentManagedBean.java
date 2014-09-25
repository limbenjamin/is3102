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
import IslandFurniture.EJB.SupplyChain.ManageInventoryMonitoringLocal;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMovementLocal;
import IslandFurniture.EJB.SupplyChain.ManageStorageLocationLocal;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
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
    private Long storageAreaId;
    private Long purchaseOrderId;

    private boolean ifGoodsReceiptDocumentDetailListEmpty;

    private String receiptDateString;
    private String deliveryNote;
    private Date receiptDateType;

    private String username;
    private String deliverynote;

    private Calendar postingDate;
    private Calendar receiptDate;
    private Calendar receiptDateCal;

    private Stock stock;
    private Integer quantity;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private List<GoodsReceiptDocument> goodsReceiptDocumentList;
    private List<GoodsReceiptDocumentDetail> goodsReceiptDocumentDetailList;
    private List<Stock> stockList;
    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<PurchaseOrder> purchaseOrderList;

    private GoodsReceiptDocument goodsReceiptDocument;
    private Staff staff;
    private Plant plant;
    private StorageBin storageBin;
    private PurchaseOrder purchaseOrder;

    @EJB
    public ManageGoodsReceiptLocal mgrl;
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

        this.goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");

        if (this.goodsReceiptDocumentId == null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            this.goodsReceiptDocumentId = new Long(request.getParameter("createGRDD:GRDid"));
        } else {
            goodsReceiptDocument = mgrl.getGoodsReceiptDocument(goodsReceiptDocumentId);
        }

        goodsReceiptDocument = mgrl.getGoodsReceiptDocument(goodsReceiptDocumentId);
        storageAreaList = miml.viewStorageAreaReceiving(plant);
        goodsReceiptDocumentDetailList = mgrl.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
        ifGoodsReceiptDocumentDetailListEmpty = goodsReceiptDocumentDetailList.isEmpty();
        goodsReceiptDocumentList = mgrl.viewGoodsReceiptDocumentIndividual(goodsReceiptDocument);
        purchaseOrderList = mgrl.viewPurchaseOrder(plant);
        stockList = mgrl.viewStock();
        if (goodsReceiptDocument.getReceiptDate() != null) {
            receiptDateString = df.format(goodsReceiptDocument.getReceiptDate().getTime());
        }
        System.out.println("Init");
    }

    public void onStorageAreaChange(AjaxBehaviorEvent event) {
        if (storageAreaId != null) {
            storageBinList = miml.viewStorageBin(storageAreaId);
        }
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

    public String editGoodsReceiptDocument(ActionEvent event) throws ParseException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        GoodsReceiptDocument grd = (GoodsReceiptDocument) event.getComponent().getAttributes().get("grd");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("date", event.getComponent().getAttributes().get("date"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("purchaseOrderId", event.getComponent().getAttributes().get("purchaseOrderId"));
        purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("purchaseOrderId");

        if (purchaseOrderId == null) {
            purchaseOrder = null;
        } else {
            purchaseOrder = mgrl.getPurchaseOrder(purchaseOrderId);
        }

        receiptDateString = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("date");

        if (receiptDateString.isEmpty()) {
            receiptDateCal = null;
        } else {
            receiptDateType = new SimpleDateFormat("yyyy-MM-dd").parse(receiptDateString);
            Calendar receiptDateCal = Calendar.getInstance();
            Date date = receiptDateType;
            receiptDateCal.setTime(date);
        }
        
        deliveryNote = grd.getDeliveryNote();
        
        if (deliveryNote.isEmpty()) {
            deliveryNote = null;
        }
  
        mgrl.editGoodsReceiptDocument(grd.getId(), receiptDateCal, purchaseOrder, deliveryNote);
        return "goodsreceiptdocument";
    }

    public String editGoodsReceiptDocumentDetail(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
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

    public void addGoodsReceiptDocumentStockUnit(ActionEvent event) throws IOException {
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

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        postingDate = cal;

        mgrl.createGoodsReceiptDocumentStockUnit(goodsReceiptDocumentId, postingDate);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocumentposted.xhtml");

    }

    public Calendar getReceiptDateCal() {
        return receiptDateCal;
    }

    public void setReceiptDateCal(Calendar receiptDateCal) {
        this.receiptDateCal = receiptDateCal;
    }
    
    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }
    
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public ManageInventoryMonitoringLocal getMiml() {
        return miml;
    }

    public void setMiml(ManageInventoryMonitoringLocal miml) {
        this.miml = miml;
    }

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
    }

    public boolean isIfGoodsReceiptDocumentDetailListEmpty() {
        return ifGoodsReceiptDocumentDetailListEmpty;
    }

    public void setIfGoodsReceiptDocumentDetailListEmpty(boolean ifGoodsReceiptDocumentDetailListEmpty) {
        this.ifGoodsReceiptDocumentDetailListEmpty = ifGoodsReceiptDocumentDetailListEmpty;
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

    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
    }

    public String getReceiptDateString() {
        return receiptDateString;
    }

    public void setReceiptDateString(String receiptDateString) {
        this.receiptDateString = receiptDateString;
    }

    public Date getReceiptDateType() {
        return receiptDateType;
    }

    public void setReceiptDateType(Date receiptDateType) {
        this.receiptDateType = receiptDateType;
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

    public Calendar getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Calendar receiptDate) {
        this.receiptDate = receiptDate;
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
