/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.InventoryManagement.ManageStoreSectionLocal;
import IslandFurniture.EJB.Kitchen.ManageIngredientInventoryLocal;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientInventory;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class IngredientInventoryManagedBean implements Serializable {

    private Long ingredientId;
    private List<IngredientInventory> ingredientInventoryList;
    private List<Ingredient> ingredientList;

    private String username;
    private Staff staff;
    private Plant plant;

    private int currentQty;
    private int thresholdQty;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageStoreSectionLocal storeSectionBean;
    @EJB
    public ManageIngredientInventoryLocal ingredientInventoryBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        ingredientInventoryList = ingredientInventoryBean.viewIngredientInventory(plant);
    }

//  Function: To edit a Ingredient Inventory
    public void editIngredientInventory(ActionEvent event) throws IOException {
        IngredientInventory ii = (IngredientInventory) event.getComponent().getAttributes().get("ingredientInventory");
        ingredientInventoryBean.editIngredientInventory(ii);
        ingredientInventoryList = ingredientInventoryBean.viewIngredientInventory(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient Inventory has sucessfully been edited", ""));
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public List<IngredientInventory> getIngredientInventoryList() {
        return ingredientInventoryList;
    }

    public void setIngredientInventoryList(List<IngredientInventory> ingredientInventoryList) {
        this.ingredientInventoryList = ingredientInventoryList;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
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

    public int getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(int currentQty) {
        this.currentQty = currentQty;
    }

    public int getThresholdQty() {
        return thresholdQty;
    }

    public void setThresholdQty(int thresholdQty) {
        this.thresholdQty = thresholdQty;
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

    public ManageIngredientInventoryLocal getIngredientInventoryBean() {
        return ingredientInventoryBean;
    }

    public void setIngredientInventoryBean(ManageIngredientInventoryLocal ingredientInventoryBean) {
        this.ingredientInventoryBean = ingredientInventoryBean;
    }

    public ManageInventoryTransferLocal getTransferBean() {
        return transferBean;
    }

    public void setTransferBean(ManageInventoryTransferLocal transferBean) {
        this.transferBean = transferBean;
    }

}
