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

    private Calendar postingDate;
    private Calendar issuedDate;

    private Stock stock;
    private Integer quantity;

    private List<GoodsIssuedDocument> goodsIssuedDocumentList;
    private List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetailList;
    private List<StockUnit> stockUnitList;
    private List<StockUnit> stockUnitMainList;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<Plant> plantList;

    private GoodsIssuedDocument goodsIssuedDocument;
    private Staff staff;
    private Plant plant;
    private StorageBin storageBin;
    private StockUnit stockUnit;

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

        System.out.println("GoodsIssuedDocumentId: " + goodsIssuedDocumentId);
        goodsIssuedDocument = mgrl.getGoodsIssuedDocument(goodsIssuedDocumentId);
        storageBinList = mgrl.viewStorageBin(plant);
        goodsIssuedDocumentDetailList = mgrl.viewGoodsIssuedDocumentDetail(goodsIssuedDocument);
        goodsIssuedDocumentList = mgrl.viewGoodsIssuedDocumentIndividual(goodsIssuedDocument);
        stockUnitList = mgrl.viewStockUnit(plant);
        stockUnitMainList = mgrl.viewStockUnitByIdMain(plant, goodsIssuedDocument);
        if (goodsIssuedDocument.getIssuedDate() != null) {
            issuedDateString = df.format(goodsIssuedDocument.getIssuedDate().getTime());
        }
        System.out.println("Init");
    }

    public String krefresh() {
        try {
            init();
        } catch (Exception ex) {
        }
        return "";
    }

    public String editGoodsIssuedDocument(ActionEvent event) throws ParseException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
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
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockUnit", event.getComponent().getAttributes().get("stockUnit"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        stockUnit = (StockUnit) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("stockUnit");
        msul.editStockUnitQuantity(stockUnit.getCommitStockUnitId(), msul.getStockUnit(stockUnit.getCommitStockUnitId()).getQty() + stockUnit.getQty());
        msul.deleteStockUnit(stockUnit.getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockUnit.getStock().getId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocument.xhtml");
    }

    public void viewStockUnit() throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        stockId = Long.parseLong(request.getParameter("createGRDD:stockId"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("stockId", stockId);
        goodsIssuedDocumentId = Long.parseLong(request.getParameter("createGRDD:GRDid"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentcommit.xhtml");
    }

    public void addGoodsIssuedDocumentStockUnit(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        postingDate = cal;

        for (StockUnit g : stockUnitMainList) {
            mgrl.createGoodsIssuedDocumentDetail(goodsIssuedDocumentId, g.getStock().getId(), g.getQty());
            mgrl.editGoodsIssuedDocument2(goodsIssuedDocumentId, postingDate);
            msul.deleteStockUnit(g.getId());
        }

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentposted.xhtml");
    }

  }
