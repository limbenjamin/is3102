/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorageLocationLocal;
import IslandFurniture.Entities.ExternalTransferOrder;
import IslandFurniture.Entities.ExternalTransferOrderDetail;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
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
    private Long stockId;
    private Long plantId;
    private Integer quantity;
    Long id;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;
    private List<Stock> stockList;
    private List<Plant> plantList;
    private List<ExternalTransferOrderDetail> externalTransferOrderDetailList;

    private String username;
    private String externalDateString;
    private String plantType;
    private String dateString;
    private Date dateType;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar dateCal;

    private Staff staff;
    private Plant plant;
    private Stock stock;

    private ExternalTransferOrder externalTransferOrder;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    public ManageStorageLocationLocal storageBean;
    @EJB
    private ManageOrganizationalHierarchyBeanLocal orgBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
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

        if (externalTransferOrder.getRequestingPlant() != null) {
            plantId = externalTransferOrder.getRequestingPlant().getId();
        }

        // end: To display date properly
        // start: To display the Plant List properly
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

        // end: To display the Plant List properly
    }

//  Function: To create a External Transfer Order Detail
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

//  Function: To edit a External Transfer Order    
    public void editExternalTransferOrder(ActionEvent event) throws ParseException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");

        dateType = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        dateCal = Calendar.getInstance();
        Date date = dateType;
        dateCal.setTime(date);

        transferBean.editExternalTransferOrder(externalTransferOrder, dateCal);

    }

//  Function: To edit a External Transfer Order Detail    
    public void editExternalTransferOrderDetail(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
        ExternalTransferOrderDetail tod = (ExternalTransferOrderDetail) event.getComponent().getAttributes().get("tod");
        transferBean.editExternalTransferOrderDetail(tod, (Long) event.getComponent().getAttributes().get("stockId"));
        externalTransferOrderDetailList = transferBean.viewExternalTransferOrderDetail(id);
    }

//  Function: To edit a External Transfer Order Detail  
    public void deleteExternalTransferOrderDetail(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
        ExternalTransferOrderDetail externalTransferOrderDetail = (ExternalTransferOrderDetail) event.getComponent().getAttributes().get("tod");
        transferBean.deleteExternaTransferOrderDetail(externalTransferOrderDetail);
        externalTransferOrderDetailList = transferBean.viewExternalTransferOrderDetail(id);
    }

    //  Function: To edit a External Transfer Order Detail  
    public void updateExternalTransferOrderToPosted(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("transferorderexternalid");
//        ExternalTransferOrderDetail externalTransferOrderDetail = (ExternalTransferOrderDetail) event.getComponent().getAttributes().get("tod");
//        transferBean.deleteExternaTransferOrderDetail(externalTransferOrderDetail);
//        externalTransferOrderDetailList = transferBean.viewExternalTransferOrderDetail(id);
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

    public List<Plant> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<Plant> plantList) {
        this.plantList = plantList;
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
