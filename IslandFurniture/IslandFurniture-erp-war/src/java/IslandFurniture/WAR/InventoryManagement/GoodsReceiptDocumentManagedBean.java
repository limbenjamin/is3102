package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.GoodsReceiptDocument;
import IslandFurniture.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.Entities.Material;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.EJB.InventoryManagement.ManageGoodsReceiptLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryMonitoringLocal;
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

    private String receiptDateString;
    private String deliveryNote;
    private String username;
    private Date receiptDateType;

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
    private List<ProcuredStockPurchaseOrder> purchaseOrderList;
    private List<ProcuredStockPurchaseOrderDetail> purchaseOrderDetailList;

    private GoodsReceiptDocument goodsReceiptDocument;
    private Staff staff;
    private Plant plant;
    private StorageBin storageBin;
    private ProcuredStockPurchaseOrder purchaseOrder;

    @EJB
    public ManageGoodsReceiptLocal receiptBean;
    @EJB
    public ManageInventoryTransferLocal mitl;
    @EJB
    public ManageStorageLocationLocal storageBean;
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

        try {
            if (goodsReceiptDocumentId == null) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("goodsreceipt.xhtml");
            }
        } catch (IOException ex) {}

        goodsReceiptDocument = receiptBean.getGoodsReceiptDocument(goodsReceiptDocumentId);
        goodsReceiptDocument = receiptBean.getGoodsReceiptDocument(goodsReceiptDocumentId);
        storageAreaList = storageBean.viewStorageBinsAtReceivingOnly(plant);
        goodsReceiptDocumentDetailList = receiptBean.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
        goodsReceiptDocumentList = receiptBean.viewGoodsReceiptDocumentIndividual(goodsReceiptDocument);
        purchaseOrderList = receiptBean.viewPurchaseOrder(plant);
        stockList = mitl.viewStock();
        if (goodsReceiptDocument.getReceiptDate() != null) {
            receiptDateString = df.format(goodsReceiptDocument.getReceiptDate().getTime());
        }
        System.out.println("Init");
        goodsReceiptDocumentDetailList = receiptBean.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
    }

    public void onStorageAreaChange(AjaxBehaviorEvent event) {
        if (storageAreaId != null) {
            storageBinList = storageBean.viewStorageBinsOfAStorageArea(storageAreaId);
        }
    }

//  Function: To create Goods Receipt Document Detail    
    public String addGoodsReceiptDocumentDetail(ActionEvent event) throws IOException {
        System.out.println(goodsReceiptDocumentId == null);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        stock = receiptBean.getStock(Long.parseLong(request.getParameter("createGRDD:stockId")));
        quantity = Integer.parseInt(request.getParameter("createGRDD:stockQuantity"));

        if (!goodsReceiptDocumentDetailList.isEmpty()) {
            for (GoodsReceiptDocumentDetail g : receiptBean.viewGoodsReceiptDocumentDetail(goodsReceiptDocument)) {
                if (g.getReceivedStock().equals(stock)) {

                    receiptBean.editGoodsReceiptDocumentDetailQtyWhenSameStockIdIsAdded(g.getId(), g.getQuantity() + quantity);
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());

                    return "goodsreceiptdocument";
                }
            }
        }

        receiptBean.createGoodsReceiptDocumentDetail(goodsReceiptDocumentId, stock.getId(), quantity);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());
        goodsReceiptDocumentDetailList = receiptBean.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
        return "goodsreceiptdocument";
    }

//  Function: To create Goods Receipt Document Detail from Purchase Order    
    public void addGoodsReceiptDocumentDetailFromPO(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("POid", event.getComponent().getAttributes().get("POid"));
        purchaseOrderId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("POid");
        purchaseOrder = receiptBean.getPurchaseOrder(purchaseOrderId);

        for (ProcuredStockPurchaseOrderDetail p : receiptBean.viewPurchaseOrderDetail(purchaseOrder)) {
            receiptBean.createGoodsReceiptDocumentDetail(goodsReceiptDocument.getId(), p.getProcuredStock().getId(), p.getQuantity());
        }

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        receiptDate = cal;

        receiptBean.setGoodsReceiptDocumentToThePurchaseOrder(goodsReceiptDocumentId, purchaseOrder, receiptDate);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());
        goodsReceiptDocumentList = receiptBean.viewGoodsReceiptDocumentIndividual(goodsReceiptDocument);
        goodsReceiptDocumentDetailList = receiptBean.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
    }

//  Function: To edit a Goods Receipt Document    
    public String editGoodsReceiptDocument(ActionEvent event) throws ParseException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        GoodsReceiptDocument grd = (GoodsReceiptDocument) event.getComponent().getAttributes().get("grd");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("date", event.getComponent().getAttributes().get("date"));
        receiptDateString = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("date");

        if (receiptDateString.isEmpty()) {
            receiptDateCal = null;
        } else {
            receiptDateType = new SimpleDateFormat("yyyy-MM-dd").parse(receiptDateString);
            receiptDateCal = Calendar.getInstance();
            Date date = receiptDateType;
            receiptDateCal.setTime(date);
        }

        deliveryNote = grd.getDeliveryNote();
        if (deliveryNote.isEmpty()) {
            deliveryNote = null;
        }

        receiptBean.editGoodsReceiptDocument(grd.getId(), receiptDateCal, deliveryNote);
        return "goodsreceiptdocument";
    }

//  Function: To edit a Goods Receipt Document Detail    
    public String editGoodsReceiptDocumentDetail(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        GoodsReceiptDocumentDetail sa = (GoodsReceiptDocumentDetail) event.getComponent().getAttributes().get("grddId");
        receiptBean.editGoodsReceiptDocumentDetail(sa.getId(), sa.getReceivedStock().getId(), sa.getQuantity());
        return "goodsreceiptdocument";
    }

//  Function: To delete a Goods Receipt Document Detail    
    public String deleteGoodsReceiptDocumentDetail(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("grddId", event.getComponent().getAttributes().get("grddId"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsReceiptDocumentDetailId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("grddId");
        receiptBean.deleteGoodsReceiptDocumentDetail(goodsReceiptDocumentDetailId);
        goodsReceiptDocumentDetailList = receiptBean.viewGoodsReceiptDocumentDetail(goodsReceiptDocument);
        return "goodsreceiptdocument";
    }

//  Function: To post a Goods Receipt Document    
    public void addStockUnitfromGoodsReceiptDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("grddId", event.getComponent().getAttributes().get("grddId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("storageBinId", event.getComponent().getAttributes().get("storageBinId"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsReceiptDocument = receiptBean.getGoodsReceiptDocument(goodsReceiptDocumentId);
        goodsReceiptDocumentDetailId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("grddId");
        storageBinId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("storageBinId");

        if (goodsReceiptDocument.getReceiptDate() == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "The 'Receipt Date' field needs to be updated before the Posting of Goods Receipt Document. Posting was unsuccessful.", ""));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocumentId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocument.xhtml");
        } else {
            storageBin = storageBean.getStorageBin(storageBinId);
            for (GoodsReceiptDocumentDetail g : goodsReceiptDocumentDetailList) {
                if (g.getReceivedStock() instanceof Material) {
                    mitl.createStockUnit(g.getReceivedStock(), "0", Long.parseLong(g.getQuantity().toString()), storageBin);
                } else {
                    mitl.createStockUnit(g.getReceivedStock(), null, Long.parseLong(g.getQuantity().toString()), storageBin);
                }
            }

            Calendar cal = Calendar.getInstance();
            Date date = new Date();
            cal.setTime(date);
            postingDate = cal;

            receiptBean.createStockUnitsFromGoodsReceiptDocument(goodsReceiptDocumentId, postingDate);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "The Goods Receipt Document was successfully created", ""));

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
            goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocumentposted.xhtml");
        }
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

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getReceiptDateString() {
        return receiptDateString;
    }

    public void setReceiptDateString(String receiptDateString) {
        this.receiptDateString = receiptDateString;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getReceiptDateType() {
        return receiptDateType;
    }

    public void setReceiptDateType(Date receiptDateType) {
        this.receiptDateType = receiptDateType;
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

    public Calendar getReceiptDateCal() {
        return receiptDateCal;
    }

    public void setReceiptDateCal(Calendar receiptDateCal) {
        this.receiptDateCal = receiptDateCal;
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

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
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

    public List<ProcuredStockPurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<ProcuredStockPurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public List<ProcuredStockPurchaseOrderDetail> getPurchaseOrderDetailList() {
        return purchaseOrderDetailList;
    }

    public void setPurchaseOrderDetailList(List<ProcuredStockPurchaseOrderDetail> purchaseOrderDetailList) {
        this.purchaseOrderDetailList = purchaseOrderDetailList;
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

    public ProcuredStockPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(ProcuredStockPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public ManageGoodsReceiptLocal getMgrl() {
        return receiptBean;
    }

    public void setMgrl(ManageGoodsReceiptLocal mgrl) {
        this.receiptBean = mgrl;
    }

    public ManageInventoryTransferLocal getMitl() {
        return mitl;
    }

    public void setMitl(ManageInventoryTransferLocal mitl) {
        this.mitl = mitl;
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

    public ManageInventoryMonitoringLocal getMiml() {
        return miml;
    }

    public void setMiml(ManageInventoryMonitoringLocal miml) {
        this.miml = miml;
    }

    
  
}
