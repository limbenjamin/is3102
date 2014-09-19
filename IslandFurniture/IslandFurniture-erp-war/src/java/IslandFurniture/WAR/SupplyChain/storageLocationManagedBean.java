/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBean;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.StorageArea;
import IslandFurniture.EJB.Entities.StorageBin;
import IslandFurniture.EJB.SupplyChain.ManageStorageLocationLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
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
public class storageLocationManagedBean implements Serializable {

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
    private ManageUserAccountBean staffBean;

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
        msll.createStorageArea(plant, storageAreaName);
        return "storagelocation";
    }

    public String addStorageBin() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        storageAreaId = Long.parseLong(request.getParameter("createStorageBin:storageAreaId"));
        storageArea = msll.getStorageArea(storageAreaId);
        storageBinName = request.getParameter("createStorageBin:storageBinName");
        msll.createStorageBin(storageArea, storageBinName);
        return "storagelocation";
    }

    public String deleteStorageArea() {
        storageAreaId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("storageAreaId"));
        msll.deleteStorageArea(storageAreaId);
        return "storagelocation";
    }

    public String deleteStorageBin() {
        storageBinId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("storageBinId"));
        msll.deleteStorageBin(storageBinId);
        return "storagelocation";
    }

    public String editStorageArea(ActionEvent event) throws IOException {
        StorageArea sa = (StorageArea) event.getComponent().getAttributes().get("said");
        msll.editStorageArea(sa.getId(), sa.getName());
        return "storagelocation";
    }

    public String editStorageBin(ActionEvent event) throws IOException {
        StorageBin sb = (StorageBin) event.getComponent().getAttributes().get("sbid");
        msll.editStorageBin(sb.getStorageArea().getId(), sb.getId(), sb.getName());
        storageBinList = msll.viewStorageBin(plant);
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

    public ManageUserAccountBean getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBean staffBean) {
        this.staffBean = staffBean;
    }

}
