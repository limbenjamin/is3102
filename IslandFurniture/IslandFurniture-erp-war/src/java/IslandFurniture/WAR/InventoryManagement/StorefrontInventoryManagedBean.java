/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.InventoryManagement.ManageStoreSectionLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryLocal;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.Entities.StorefrontInventory;
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
public class StorefrontInventoryManagedBean implements Serializable {

    private Long stockId;
    private Long storeSectionId;

    private List<StoreSection> storeSectionList;
    private List<StorefrontInventory> storefrontInventoryList;
    private List<Stock> stockList;

    private String username;
    private Staff staff;
    private Plant plant;

    private int replenishQty;
    private int maxQty;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageStoreSectionLocal storeSectionBean;
    @EJB
    public ManageStorefrontInventoryLocal storefrontInventoryBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        storeSectionList = storeSectionBean.viewStoreSectionList(plant);
        storefrontInventoryList = storefrontInventoryBean.viewStorefrontInventory(plant);
        stockList = transferBean.viewStock();
//        for (Stock s : transferBean.viewStock()) {
//            if (!(s instanceof Material)) {
//                stockList.add(s);
//            }
//            for (StorefrontInventory i : storefrontInventoryList) {
//                if (stockList.contains(i.getStock())) {
//                    stockList.remove(i.getStock());
//                    break;
//                }
//            }
//        }
    }

//  Function: To create a Storefront Inventory
    public void addStorefrontInventory(ActionEvent event) throws IOException {
        if (replenishQty > maxQty) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Replenishment Quantity should be lesser than Maximum Quantity. Creation of Storefront Inventory was unsuccessful.", ""));

        } else if (replenishQty > maxQty * .5) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Replenishment Quantity should be minimally 50% or lesser than the Maximum Quantity. <br/> The suggested Replenishment Quantity with Maximum Quantity of " + maxQty + " should be " + Math.floor((double) maxQty * .5) + " or lesser. <br/> Creation of Storefront Inventory was unsuccessful.", ""));
        } else {
            storefrontInventoryBean.createStorefrontInventory(plant, stockId, replenishQty, maxQty, storeSectionId);
            storefrontInventoryList = storefrontInventoryBean.viewStorefrontInventory(plant);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Storefront Inventory has sucessfully been created", ""));
        }
    }

//  Function: To edit a Storefront Inventory
    public void editStorefrontInventory(ActionEvent event) throws IOException {
        StorefrontInventory si = (StorefrontInventory) event.getComponent().getAttributes().get("storefrontInventory");
        if (si.getRepQty() > si.getMaxQty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Replenishment Quantity should be lesser than Maximum Quantity. Editing of Storefront Inventory was unsuccessful.", ""));

        } else if (si.getRepQty() > si.getMaxQty() * .5) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Replenishment Quantity should be minimally 50% or lesser than the Maximum Quantity. <br/> The suggested Replenishment Quantity with Maximum Quantity of " + si.getMaxQty() + " should be " + Math.floor((double) si.getMaxQty() * .5) + " or lesser. <br/> Editing of Storefront Inventory was unsuccessful.", ""));
        } else {
            storefrontInventoryBean.editStorefrontInventory(si);
            storefrontInventoryList = storefrontInventoryBean.viewStorefrontInventory(plant);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Storefront Inventory has sucessfully been edited", ""));
        }
    }

//  Function: To delete a Storefront Inventory
    public void deleteStorefrontInventory(ActionEvent event) throws IOException {
        StorefrontInventory si = (StorefrontInventory) event.getComponent().getAttributes().get("storefrontInventory");
        storefrontInventoryBean.deleteStorefrontInventory(si);
        storefrontInventoryList = storefrontInventoryBean.viewStorefrontInventory(plant);
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }

    public int getReplenishQty() {
        return replenishQty;
    }

    public void setReplenishQty(int replenishQty) {
        this.replenishQty = replenishQty;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStoreSectionId() {
        return storeSectionId;
    }

    public void setStoreSectionId(Long storeSectionId) {
        this.storeSectionId = storeSectionId;
    }

    public List<StoreSection> getStoreSectionList() {
        return storeSectionList;
    }

    public void setStoreSectionList(List<StoreSection> storeSectionList) {
        this.storeSectionList = storeSectionList;
    }

    public List<StorefrontInventory> getStorefrontInventoryList() {
        return storefrontInventoryList;
    }

    public void setStorefrontInventoryList(List<StorefrontInventory> storefrontInventoryList) {
        this.storefrontInventoryList = storefrontInventoryList;
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

    public ManageStoreSectionLocal getStoreSectionBean() {
        return storeSectionBean;
    }

    public void setStoreSectionBean(ManageStoreSectionLocal storeSectionBean) {
        this.storeSectionBean = storeSectionBean;
    }

    public ManageStorefrontInventoryLocal getStorefrontInventoryBean() {
        return storefrontInventoryBean;
    }

    public void setStorefrontInventoryBean(ManageStorefrontInventoryLocal storefrontInventoryBean) {
        this.storefrontInventoryBean = storefrontInventoryBean;
    }

    public ManageInventoryTransferLocal getTransferBean() {
        return transferBean;
    }

    public void setTransferBean(ManageInventoryTransferLocal transferBean) {
        this.transferBean = transferBean;
    }

}
