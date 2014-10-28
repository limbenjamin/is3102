/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.EJB.OperationalCRM.ManageFeaturedProductsLocal;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
public class FeaturedProductsManagedBean implements Serializable {

    private List<Stock> featuredProductsList;
    private List<Stock> stockList;
    private List<Stock> tempList;
    private Long stockId;
    
    private String username;
    private Staff staff;
    private Plant plant;

    private String title;
    private int points;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageFeaturedProductsLocal featuredBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        featuredProductsList = featuredBean.viewFeaturedProducts(plant);
        tempList = transferBean.viewStock();
        stockList = new ArrayList<>();
        for(Stock s : tempList) {
            if(s instanceof FurnitureModel || s instanceof RetailItem)
                stockList.add(s);
        }

        Iterator<Stock> iterator = stockList.iterator();
        while (iterator.hasNext()){
            Stock s = iterator.next();
            if (featuredProductsList.contains(s)) {
                iterator.remove();
            }
        }
    }

//  Function: To add a Featured Product
    public void addFeaturedProduct(ActionEvent event) throws IOException {
        featuredBean.addStock(plant, stockId);
        featuredProductsList = featuredBean.viewFeaturedProducts(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, transferBean.getStock(stockId).getName() + " has sucessfully been added to the Featured Product.", ""));
    }

//  Function: To delete a Featured Product
    public void deleteFeaturedProduct(ActionEvent event) throws IOException {
        Stock stock = (Stock) event.getComponent().getAttributes().get("stock");
        featuredBean.deleteStock(plant, stock);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, stock.getName() + "  has sucessfully been removed from the Featured Product.", ""));
    }

    public List<Stock> getFeaturedProductsList() {
        return featuredProductsList;
    }

    public void setFeaturedProductsList(List<Stock> featuredProductsList) {
        this.featuredProductsList = featuredProductsList;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageFeaturedProductsLocal getFeaturedBean() {
        return featuredBean;
    }

    public void setFeaturedBean(ManageFeaturedProductsLocal featuredBean) {
        this.featuredBean = featuredBean;
    }

    public ManageInventoryTransferLocal getTransferBean() {
        return transferBean;
    }

    public void setTransferBean(ManageInventoryTransferLocal transferBean) {
        this.transferBean = transferBean;
    }
   
}
