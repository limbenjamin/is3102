/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBean;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.EJB.InventoryManagement.ManageStorageLocationLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
public class StorageLocationManagedBean implements Serializable {

    private Long plantId;
    private Long storageAreaId;
    private Long storageBinId;

    private String storageAreaName;
    private String storageBinName;
    private String username;

    private List<StorageBin> storageBinList;
    private List<StorageArea> storageAreaList;

    private StorageArea storageArea;
    private StorageBin storageBin;
    private Staff staff;
    private Plant plant;

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
        storageBinList = msll.viewStorageBin(plant);
        storageAreaList = msll.viewStorageArea(plant);
        System.out.println("Init");
    }

    public String addStorageArea() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        storageAreaName = request.getParameter("createStorageArea:storageAreaName");
        
        if (msll.viewStorageAreaSameName(plant, storageAreaName).isEmpty()) {
            msll.createStorageArea(plant, storageAreaName);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Storage Area has sucessfully been created", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an existing Storage Area with that name. Creation of Storage Area was unsuccessful.",""));
        }

        return "storagelocation";
    }

    public String addStorageBin() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        storageAreaId = Long.parseLong(request.getParameter("createStorageBin:storageAreaId"));
        storageArea = msll.getStorageArea(storageAreaId);
        storageBinName = request.getParameter("createStorageBin:storageBinName");
        
        if (msll.viewStorageBinSameName(plant, storageAreaId, storageBinName).isEmpty()) {
            msll.createStorageBin(storageArea, storageBinName);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Storage Bin has sucessfully been created", ""));
        } else {
             FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an existing Storage Bin with that name. Creation of Storage Bin was unsuccessful.",""));
        }
        
        
        return "storagelocation";
    }

    public String deleteStorageArea() throws IOException {
        storageAreaId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("storageAreaId"));
        storageArea = msll.getStorageArea(storageAreaId);
        if (storageArea.getStorageBins().isEmpty()) {
            msll.deleteStorageArea(storageAreaId);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Storage Area has sucessfully been deleted", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There are Storage Bins associated with this Storage Area. Delete of Storage Area was unsuccessful.", ""));
        }
        return "storagelocation";
    }

    public String deleteStorageBin() throws IOException {
        storageBinId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("storageBinId"));
        storageBin = msll.getStorageBin(storageBinId);
        if (storageBin.getStockUnits().isEmpty()) {
            msll.deleteStorageBin(storageBinId);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Storage Bin has sucessfully been deleted", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There are Stock Units stored in the Storage Bin. Delete of Storage Bin was unsuccessful.", ""));
        }

        return "storagelocation";
    }

    public String editStorageArea(ActionEvent event) throws IOException {
        StorageArea sa = (StorageArea) event.getComponent().getAttributes().get("said");
        msll.editStorageArea(sa.getId(), sa.getName());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Storage Area has sucessfully been edited", ""));
        return "storagelocation";
    }

    public String editStorageBin(ActionEvent event) throws IOException {
        StorageBin sb = (StorageBin) event.getComponent().getAttributes().get("sbid");
        msll.editStorageBin(sb.getStorageArea().getId(), sb.getId(), sb.getName());
        storageBinList = msll.viewStorageBin(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Storage Bin has sucessfully been edited", ""));
        return "storagelocation";
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
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

    public String getStorageAreaName() {
        return storageAreaName;
    }

    public void setStorageAreaName(String storageAreaName) {
        this.storageAreaName = storageAreaName;
    }

    public String getStorageBinName() {
        return storageBinName;
    }

    public void setStorageBinName(String storageBinName) {
        this.storageBinName = storageBinName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public StorageArea getStorageArea() {
        return storageArea;
    }

    public void setStorageArea(StorageArea storageArea) {
        this.storageArea = storageArea;
    }

    public StorageBin getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(StorageBin storageBin) {
        this.storageBin = storageBin;
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