/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageGoodsIssuedLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorageLocationLocal;
import IslandFurniture.Entities.ExternalTransferOrder;
import IslandFurniture.Entities.ExternalTransferOrderDetail;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class InventoryTransferExternalManagedBean implements Serializable {

    private Long storageAreaId;
    private Long storageBinId;
    private Long stockId;
    private Integer quantity;
    private String plantType;

    private List<StorageBin> storageBinList;
    private List<StockUnit> stockUnitList;
    private List<ExternalTransferOrder> allETORequest;
    private List<ExternalTransferOrder> allETOFulfill;
    private List<ExternalTransferOrder> externalTransferOrderListRequestPendingList;
    private List<ExternalTransferOrder> externalTransferOrderListRequestPostedList;
    private List<ExternalTransferOrder> externalTransferOrderListFulfilledPendingList;
    private List<ExternalTransferOrder> externalTransferOrderListFulfilledPostedList;
    private List<ExternalTransferOrder> externalTransferOrderListFulfilledPostedListFromRequesting;

    private ExternalTransferOrder externalTransferOrder;

    private String username;
    private Staff staff;
    private Plant plant;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    public ManageStorageLocationLocal storageBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        storageBinList = storageBean.viewStorageBinsAtShippingOnly(plant);
        stockUnitList = transferBean.viewStockUnitDistinctName(plant);
//        allETORequest = transferBean.viewAllExternalTransferOrderRequesting(plant);
//        changePlantNameRequest(allETORequest);
//        allETOFulfill = transferBean.viewAllExternalTransferOrderFulFilling(plant);;
        externalTransferOrderListRequestPendingList = transferBean.viewExternalTransferOrderRequestedPending(plant);
//        changePlantNameFulfill(externalTransferOrderListRequestPendingList);
        externalTransferOrderListRequestPostedList = transferBean.viewExternalTransferOrderRequestedPosted(plant);
//        changePlantNameFulfill(externalTransferOrderListRequestPostedList);
        externalTransferOrderListFulfilledPendingList = transferBean.viewExternalTransferOrderFulfilledPending(plant);
//        changePlantNameRequest(externalTransferOrderListFulfilledPendingList);
        externalTransferOrderListFulfilledPostedList = transferBean.viewExternalTransferOrderFulfilledPosted(plant);
//        changePlantNameRequest(externalTransferOrderListFulfilledPostedList);
        externalTransferOrderListFulfilledPostedListFromRequesting = transferBean.viewExternalTransferOrderFulfilledPostedFromRequesting(plant);
//        changePlantNameFulfill(externalTransferOrderListFulfilledPostedListFromRequesting);
    }

    void changePlantNameRequest(List<ExternalTransferOrder> list) {
        for (ExternalTransferOrder e : list) {

            if (e.getRequestingPlant() != null) {
                plantType = e.getRequestingPlant().getClass().getSimpleName();
                if (plantType.equals("ManufacturingFacility")) {
                    plantType = "MFG";
                } else if (plantType.equals("CountryOffice")) {
                    plantType = "CO";
                } else if (plantType.equals("GlobalHQ")) {
                    plantType = ""; //no need cos global HQ global HQ looks ugly
                }

                e.getRequestingPlant().setName(e.getRequestingPlant().getName() + " " + plantType);
            }
        }
    }
    
       void changePlantNameFulfill(List<ExternalTransferOrder> list) {
        for (ExternalTransferOrder e : list) {
            if (e.getFulfillingPlant() != null) {
                plantType = e.getFulfillingPlant().getClass().getSimpleName();
                if (plantType.equals("ManufacturingFacility")) {
                    plantType = "MFG";
                } else if (plantType.equals("CountryOffice")) {
                    plantType = "CO";
                } else if (plantType.equals("GlobalHQ")) {
                    plantType = ""; //no need cos global HQ global HQ looks ugly
                }

                e.getFulfillingPlant().setName(e.getFulfillingPlant().getName() + " " + plantType);
            }
        }
    }

//  Function: To create a External Tranfer Order
    public void createExternalTransferOrder(ActionEvent event) throws IOException {
        externalTransferOrder = transferBean.createExternalTransferOrder(plant, getCalendar());
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventorytransfer_externaldetail.xhtml?id=" + externalTransferOrder.getId().toString());
    }

//  Function: To delete a External Transfer Order  
    public void deleteExternalTransferOrder(ActionEvent event) throws IOException {
        ExternalTransferOrder to = (ExternalTransferOrder) event.getComponent().getAttributes().get("to");
        if (!transferBean.viewExternalTransferOrderDetail(to.getId()).isEmpty()) {
            for (ExternalTransferOrderDetail g : transferBean.viewExternalTransferOrderDetail(to.getId())) {
                transferBean.deleteExternaTransferOrderDetail(g);
            }
        }
        transferBean.deleteExternaTransferOrder(to.getId());
        externalTransferOrderListRequestPendingList = transferBean.viewExternalTransferOrderRequestedPending(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The External Transfer Order was successfully deleted", ""));
    }

    //  Function: To get current Time in Calendar type
    Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Calendar calDate = cal;
        return calDate;
    }

    public List<ExternalTransferOrder> getAllETORequest() {
        return allETORequest;
    }

    public void setAllETORequest(List<ExternalTransferOrder> allETORequest) {
        this.allETORequest = allETORequest;
    }

    public List<ExternalTransferOrder> getAllETOFulfill() {
        return allETOFulfill;
    }

    public void setAllETOFulfill(List<ExternalTransferOrder> allETOFulfill) {
        this.allETOFulfill = allETOFulfill;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public Long getStorageAreaId() {
        return storageAreaId;
    }

    public void setStorageAreaId(Long storageAreaId) {
        this.storageAreaId = storageAreaId;
    }

    public Long getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(Long storageBinId) {
        this.storageBinId = storageBinId;
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

    public List<StorageBin> getStorageBinList() {
        return storageBinList;
    }

    public void setStorageBinList(List<StorageBin> storageBinList) {
        this.storageBinList = storageBinList;
    }

    public List<StockUnit> getStockUnitList() {
        return stockUnitList;
    }

    public void setStockUnitList(List<StockUnit> stockUnitList) {
        this.stockUnitList = stockUnitList;
    }

    public List<ExternalTransferOrder> getExternalTransferOrderListRequestPendingList() {
        return externalTransferOrderListRequestPendingList;
    }

    public void setExternalTransferOrderListRequestPendingList(List<ExternalTransferOrder> externalTransferOrderListRequestPendingList) {
        this.externalTransferOrderListRequestPendingList = externalTransferOrderListRequestPendingList;
    }

    public List<ExternalTransferOrder> getExternalTransferOrderListRequestPostedList() {
        return externalTransferOrderListRequestPostedList;
    }

    public void setExternalTransferOrderListRequestPostedList(List<ExternalTransferOrder> externalTransferOrderListRequestPostedList) {
        this.externalTransferOrderListRequestPostedList = externalTransferOrderListRequestPostedList;
    }

    public List<ExternalTransferOrder> getExternalTransferOrderListFulfilledPendingList() {
        return externalTransferOrderListFulfilledPendingList;
    }

    public void setExternalTransferOrderListFulfilledPendingList(List<ExternalTransferOrder> externalTransferOrderListFulfilledPendingList) {
        this.externalTransferOrderListFulfilledPendingList = externalTransferOrderListFulfilledPendingList;
    }

    public List<ExternalTransferOrder> getExternalTransferOrderListFulfilledPostedList() {
        return externalTransferOrderListFulfilledPostedList;
    }

    public void setExternalTransferOrderListFulfilledPostedList(List<ExternalTransferOrder> externalTransferOrderListFulfilledPostedList) {
        this.externalTransferOrderListFulfilledPostedList = externalTransferOrderListFulfilledPostedList;
    }

    public List<ExternalTransferOrder> getExternalTransferOrderListFulfilledPostedListFromRequesting() {
        return externalTransferOrderListFulfilledPostedListFromRequesting;
    }

    public void setExternalTransferOrderListFulfilledPostedListFromRequesting(List<ExternalTransferOrder> externalTransferOrderListFulfilledPostedListFromRequesting) {
        this.externalTransferOrderListFulfilledPostedListFromRequesting = externalTransferOrderListFulfilledPostedListFromRequesting;
    }

    public ExternalTransferOrder getExternalTransferOrder() {
        return externalTransferOrder;
    }

    public void setExternalTransferOrder(ExternalTransferOrder externalTransferOrder) {
        this.externalTransferOrder = externalTransferOrder;
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
