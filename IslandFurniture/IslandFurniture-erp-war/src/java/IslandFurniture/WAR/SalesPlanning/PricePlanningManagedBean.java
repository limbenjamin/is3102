/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
import IslandFurniture.EJB.SalesPlanning.PriceManagerLocal;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class PricePlanningManagedBean {
    @EJB
    private StockManagerLocal stockManager;
    @EJB
    private SupplierManagerLocal supplierManager;
    @EJB
    private PriceManagerLocal priceManager;
    
    private List<FurnitureModel> furnitureList;
    private List<RetailItem> retailItemList;
    private List<StockSupplied> stockSuppliedList;
    private List<StockSupplied> displayList; 
    private Set<FurnitureModel> furnitureSet;
    private Set<RetailItem> retailItemSet;
    private Stock stock;
    private FurnitureModel furniture;
    private RetailItem retailItem;
    private boolean displayFurniture;
    private boolean displayRetailItem;

    public List<FurnitureModel> getFurnitureList() {
        return furnitureList;
    }

    public void setFurnitureList(List<FurnitureModel> furnitureList) {
        this.furnitureList = furnitureList;
    }

    public List<RetailItem> getRetailItemList() {
        return retailItemList;
    }

    public void setRetailItemList(List<RetailItem> retailItemList) {
        this.retailItemList = retailItemList;
    }

    public List<StockSupplied> getStockSuppliedList() {
        return stockSuppliedList;
    }

    public void setStockSuppliedList(List<StockSupplied> stockSuppliedList) {
        this.stockSuppliedList = stockSuppliedList;
    }

    public List<StockSupplied> getDisplayList() {
        return displayList;
    }

    public void setDisplayList(List<StockSupplied> displayList) {
        this.displayList = displayList;
    }

    public Set<FurnitureModel> getFurnitureSet() {
        return furnitureSet;
    }

    public void setFurnitureSet(Set<FurnitureModel> furnitureSet) {
        this.furnitureSet = furnitureSet;
    }

    public Set<RetailItem> getRetailItemSet() {
        return retailItemSet;
    }

    public void setRetailItemSet(Set<RetailItem> retailItemSet) {
        this.retailItemSet = retailItemSet;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public FurnitureModel getFurniture() {
        return furniture;
    }

    public void setFurniture(FurnitureModel furniture) {
        this.furniture = furniture;
    }

    public RetailItem getRetailItem() {
        return retailItem;
    }

    public void setRetailItem(RetailItem retailItem) {
        this.retailItem = retailItem;
    }

    public boolean isDisplayFurniture() {
        return displayFurniture;
    }

    public boolean isDisplayRetailItem() {
        return displayRetailItem;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("init:PricePlanningManagedBean");
        stockSuppliedList = supplierManager.getAllStockSupplied();
        furnitureSet = new HashSet<>();
        retailItemSet = new HashSet<>();
        for(StockSupplied ss : stockSuppliedList) {
            if(ss.getStock() instanceof FurnitureModel)
                furnitureSet.add((FurnitureModel)ss.getStock());
            else
                retailItemSet.add((RetailItem)ss.getStock());
        }
        furnitureList = new ArrayList<>(furnitureSet);
        retailItemList = new ArrayList<>(retailItemSet);
    }
    public void viewPricing(AjaxBehaviorEvent event) {
        System.out.println("PricePlanningManagedBean.viewPricing()");
        Long stockID = (Long) event.getComponent().getAttributes().get("stockID");
        stock = stockManager.getStock(stockID);
        if(stock instanceof FurnitureModel){
            furniture = (FurnitureModel) stock;
            this.displayFurniture = true;
            this.displayRetailItem = false;
        } else {
            retailItem = (RetailItem) stock;
            this.displayRetailItem = true;
            this.displayFurniture = false;
        }
        displayList = priceManager.findCountryOfficeWithStock(stockID);
    }
    public String editPrice() {
        System.out.println("PricePlanningManagedBean.editPrice()");
        String msg = "";
        if(displayFurniture) {
            msg = priceManager.editPoints(this.stock.getId(), this.furniture.getPointsWorth());
        } else {
            msg = priceManager.editPoints(this.stock.getId(), this.retailItem.getPointsWorth());
        }
        if (msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
            return "priceplanning";
        } 
        for(StockSupplied ss : displayList) {
            msg = priceManager.editPrice(ss, ss.getPrice());
            if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
            } 
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Pricing/Points for \"" + stock.getName() + "\" has been updated", ""));

        return "priceplanning";
    }
}
