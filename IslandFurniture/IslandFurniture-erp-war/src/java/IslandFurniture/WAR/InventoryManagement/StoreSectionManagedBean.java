/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.InventoryManagement.ManageStoreSectionLocal;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class StoreSectionManagedBean implements Serializable {

    private List<StoreSection> storeSectionList;
    
    private String username;
    private Staff staff;
    private Plant plant;
    
    private String name;
    private int level;
    private String description;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageStoreSectionLocal storeSectionBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        storeSectionList = storeSectionBean.viewStoreSectionList(plant);
    }
    
//  Function: To create a Store Section
    public void addStoreSection(ActionEvent event) throws IOException {
        storeSectionBean.createStoreSection(plant, level, name, description);
        storeSectionList = storeSectionBean.viewStoreSectionList(plant);
    }

//  Function: To edit a Store Section
    public void editStoreSection(ActionEvent event) throws IOException {
        StoreSection ss = (StoreSection) event.getComponent().getAttributes().get("storeSection");
        storeSectionBean.editStoreSection(ss);
        storeSectionList = storeSectionBean.viewStoreSectionList(plant);
    }
    
//  Function: To delete a Store Section
    public void deleteStoreSection(ActionEvent event) throws IOException {
        StoreSection ss = (StoreSection) event.getComponent().getAttributes().get("storeSection");
        storeSectionBean.deleteStoreSection(ss);
        storeSectionList = storeSectionBean.viewStoreSectionList(plant);
    }

    public List<StoreSection> getStoreSectionList() {
        return storeSectionList;
    }

    public void setStoreSectionList(List<StoreSection> storeSectionList) {
        this.storeSectionList = storeSectionList;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageStoreSectionLocal getStoreSectionBean() {
        return storeSectionBean;
    }

    public void setStoreSectionBean(ManageStoreSectionLocal storeSectionBean) {
        this.storeSectionBean = storeSectionBean;
    }
}
