/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageGoodsIssuedLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorageLocationLocal;
import IslandFurniture.Entities.ExternalTransferOrder;
import IslandFurniture.Entities.ExternalTransferOrderDetail;
import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class InventoryTransferExternalDetailManagedBean implements Serializable {

    private Long storageAreaId;
    private Long storageBinId;
    private Long stockId;
    private Long plantId;
    private Integer quantity;
    private Long id;
    private int check;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<Stock> stockList;
    private List<ExternalTransferOrderDetail> externalTransferOrderDetailList;
    private List<Plant> plantList;

    private String username;
    private String externalDateString;
    private String plantType;
    private String dateString;
    private String plantTypeRequest;
    private String plantTypeFulfill;
    private Date dateType;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar postingDate;
    private Calendar dateCal;

    private Staff staff;
    private Plant plant;
    private Stock stock;
    private StorageBin storageBin;
    private GoodsIssuedDocument goodsIssuedDocument;
    private StockUnit stockUnitOld;

    private ExternalTransferOrder externalTransferOrder;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    public ManageStorageLocationLocal storageBean;
    @EJB
    private ManageOrganizationalHierarchyBeanLocal orgBean;
    @EJB
    public ManageGoodsIssuedLocal issuedBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        storageBinList = storageBean.viewStorageBinsAtShippingOnly(plant);
        stockList = transferBean.viewStock();
        try {
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            session.setAttribute("transferorderexternalid", id);
        } catch (Exception e) {
            id = (Long) session.getAttribute("transferorderexternalid");
        }
        externalTransferOrder = transferBean.getExternalTransferOrder(id);
        externalTransferOrderDetailList = transferBean.viewExternalTransferOrderDetail(id);

        // start: To display date properly
        if (externalTransferOrder.getTransferDate() != null) {
            dateString = df.format(externalTransferOrder.getTransferDate().getTime());
        }
        // end: To display date properly
        // start: To display plant list properly
        plantList = orgBean.displayPlantbyInstanceOf(plant);
        plantList.remove(plant);
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
        // end: To display plant list properly
        // start: To display fulfilled plant properly
        if (externalTransferOrder.getFulfillingPlant() != null) {
            plantId = externalTransferOrder.getFulfillingPlant().getId();
        }
        // end: To display fulfilled plant properly
        // start: To display plant name properly

        if (externalTransferOrder.getRequestingPlant() != null) {
            String plantType2 = externalTransferOrder.getRequestingPlant().getClass().getSimpleName();
            if (plantType2.equals("ManufacturingFacility")) {
                plantType2 = "MFG";
            } else if (plantType2.equals("CountryOffice")) {
                plantType2 = "CO";
            } else if (plantType2.equals("GlobalHQ")) {
                plantType2 = ""; //no need cos global HQ global HQ looks ugly
            }
            plantTypeRequest = externalTransferOrder.getRequestingPlant().getName() + " (" + plantType2 + ")";
        }

        if (externalTransferOrder.getFulfillingPlant() != null) {
            String plantType3 = externalTransferOrder.getFulfillingPlant().getClass().getSimpleName();
            if (plantType3.equals("ManufacturingFacility")) {
                plantType3 = "MFG";
            } else if (plantType3.equals("CountryOffice")) {
                plantType3 = "CO";
            } else if (plantType3.equals("GlobalHQ")) {
                plantType3 = ""; //no need cos global HQ global HQ looks ugly
            }
            plantTypeFulfill = externalTransferOrder.getFulfillingPlant().getName() + " (" + plantType3 + ")";
        }

        // end: To display plant name properly
    }

//  Function: To create a External Transfer Order Detail (Request)
    public void addExternalTransferOrderDetail(ActionEvent event) {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
        stock = transferBean.getStock(stockId);
        if (!externalTransferOrderDetailList.isEmpty()) {
            for (ExternalTransferOrderDetail e : externalTransferOrderDetailList) {
                if (e.getStock().equals(stock)) {
                    transferBean.editExternalTransferOrderDetailQtyWhenSameStockIdIsAdded(e.getId(), e.getQty() + quantity);
                    externalTransferOrderDetailList = transferBean.viewExternalTransferOrderDetail(id);
                    return;
                }
            }
        }
        transferBean.createExternalTransferOrderDetail(id, stockId, quantity);
        externalTransferOrderDetailList = transferBean.viewExternalTransferOrderDetail(id);
    }

//  Function: To edit a External Transfer Order (Request)    
    public void editExternalTransferOrder(ActionEvent event) throws ParseException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
        dateType = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        dateCal = Calendar.getInstance();
        Date date = dateType;
        dateCal.setTime(date);

        System.out.println("Plant id is" + plantId);

        transferBean.editExternalTransferOrder(externalTransferOrder, dateCal, orgBean.getPlantById(plantId));
    }

//  Function: To edit a External Transfer Order Detail (Request)    
    public void editExternalTransferOrderDetail(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
        ExternalTransferOrderDetail tod = (ExternalTransferOrderDetail) event.getComponent().getAttributes().get("tod");
        transferBean.editExternalTransferOrderDetail(tod, (Long) event.getComponent().getAttributes().get("stockId"));
        externalTransferOrderDetailList = transferBean.viewExternalTransferOrderDetail(id);
    }

//  Function: To edit a External Transfer Order Detail (Request) 
    public void deleteExternalTransferOrderDetail(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
        ExternalTransferOrderDetail externalTransferOrderDetail = (ExternalTransferOrderDetail) event.getComponent().getAttributes().get("tod");
        transferBean.deleteExternaTransferOrderDetail(externalTransferOrderDetail);
        externalTransferOrderDetailList = transferBean.viewExternalTransferOrderDetail(id);
    }

//  Function: To edit a External Transfer Order (Request) Status to Posted
    public void updateExternalTransferOrderToPosted(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
        transferBean.editExternalTransferOrderStatusToRequestPosted(externalTransferOrder);
    }

//  Function: To edit a External Transfer Order (Request) Status to Posted
    public void updateExternalTransferOrderToFulfilled(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
        storageBin = storageBean.getStorageBin(storageBinId);

        for (ExternalTransferOrderDetail e : externalTransferOrderDetailList) {
            for (StockUnit s : storageBin.getStockUnits()) {
                if (e.getStock().equals(s.getStock())) {
                    if (e.getQty() < s.getQty().intValue() || e.getQty() == s.getQty().intValue()) {
                        check++;
                        break;
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is insuffice", ""));
                        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_externalpending.xhtml?id=" + externalTransferOrder.getId().toString());
                    }
                }
            }
        }

        if (check == externalTransferOrderDetailList.size()) {
            goodsIssuedDocument = issuedBean.createGoodsIssuedDocument(plant);
            issuedBean.editGoodsIssuedDocument(goodsIssuedDocument.getId(), externalTransferOrder.getTransferDate(), externalTransferOrder.getRequestingPlant());
            for (ExternalTransferOrderDetail e : externalTransferOrderDetailList) {
                for (StockUnit stockUnit : storageBin.getStockUnits()) {
                    if (e.getStock().equals(stockUnit.getStock())) {
                        transferBean.createStockUnit2(stockUnit.getStock(), stockUnit.getId(), stockUnit.getBatchNo(), e.getQty().longValue(), stockUnit.getLocation(), goodsIssuedDocument);
                        transferBean.editStockUnitQuantity(stockUnit.getId(), stockUnit.getQty() - e.getQty().longValue());
                    }
                }
            }

            Calendar cal = Calendar.getInstance();
            Date date = new Date();
            cal.setTime(date);
            postingDate = cal;

            for (StockUnit g : issuedBean.viewStockUnitPendingMovementAtGoodsIssuedDocument(goodsIssuedDocument)) {
                issuedBean.createGoodsIssuedDocumentDetail(goodsIssuedDocument.getId(), g.getStock().getId(), g.getQty());
                issuedBean.postGoodsIssuedDocument(goodsIssuedDocument.getId(), postingDate);
                transferBean.deleteStockUnit(g.getId());

                // Start: To check if Quantity = 0
                stockUnitOld = transferBean.getStockUnit(g.getCommitStockUnitId());
                if (stockUnitOld.getQty() == 0) {
                    transferBean.deleteStockUnit(stockUnitOld.getId());
                }
                // End
            }

            transferBean.editExternalTransferOrderStatusToRequestFulfilled(externalTransferOrder, plant);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "The Goods Issued Document was successfully created for External Transfer Order #" + externalTransferOrder.getId(), ""));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocument.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentposted.xhtml");

        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is no such stock", ""));
            FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_externalpending.xhtml?id=" + externalTransferOrder.getId().toString());
        }
    }

    public String getPlantTypeRequest() {
        return plantTypeRequest;
    }

    public void setPlantTypeRequest(String plantTypeRequest) {
        this.plantTypeRequest = plantTypeRequest;
    }

    public String getPlantTypeFulfill() {
        return plantTypeFulfill;
    }

    public void setPlantTypeFulfill(String plantTypeFulfill) {
        this.plantTypeFulfill = plantTypeFulfill;
    }

    public List<Plant> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<Plant> plantList) {
        this.plantList = plantList;
    }

    public StockUnit getStockUnitOld() {
        return stockUnitOld;
    }

    public void setStockUnitOld(StockUnit stockUnitOld) {
        this.stockUnitOld = stockUnitOld;
    }

    public Calendar getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Calendar postingDate) {
        this.postingDate = postingDate;
    }

    public GoodsIssuedDocument getGoodsIssuedDocument() {
        return goodsIssuedDocument;
    }

    public void setGoodsIssuedDocument(GoodsIssuedDocument goodsIssuedDocument) {
        this.goodsIssuedDocument = goodsIssuedDocument;
    }

    public ManageGoodsIssuedLocal getIssuedBean() {
        return issuedBean;
    }

    public void setIssuedBean(ManageGoodsIssuedLocal issuedBean) {
        this.issuedBean = issuedBean;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public StorageBin getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(StorageBin storageBin) {
        this.storageBin = storageBin;
    }

    public Long getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Long storageBinId) {
        this.storageBinId = storageBinId;
    }

    public Calendar getDateCal() {
        return dateCal;
    }

    public void setDateCal(Calendar dateCal) {
        this.dateCal = dateCal;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Date getDateType() {
        return dateType;
    }

    public void setDateType(Date dateType) {
        this.dateType = dateType;
    }

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
    }

    public ManageOrganizationalHierarchyBeanLocal getOrgBean() {
        return orgBean;
    }

    public void setOrgBean(ManageOrganizationalHierarchyBeanLocal orgBean) {
        this.orgBean = orgBean;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public List<ExternalTransferOrderDetail> getExternalTransferOrderDetailList() {
        return externalTransferOrderDetailList;
    }

    public void setExternalTransferOrderDetailList(List<ExternalTransferOrderDetail> externalTransferOrderDetailList) {
        this.externalTransferOrderDetailList = externalTransferOrderDetailList;
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getExternalDateString() {
        return externalDateString;
    }

    public void setExternalDateString(String externalDateString) {
        this.externalDateString = externalDateString;
    }

    public ExternalTransferOrder getExternalTransferOrder() {
        return externalTransferOrder;
    }

    public void setExternalTransferOrder(ExternalTransferOrder externalTransferOrder) {
        this.externalTransferOrder = externalTransferOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
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

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
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

}
