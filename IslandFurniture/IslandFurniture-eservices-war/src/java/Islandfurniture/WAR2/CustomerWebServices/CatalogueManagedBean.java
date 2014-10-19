/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class CatalogueManagedBean implements Serializable{

    private List<FurnitureModel> furnitureList;
    private List<RetailItem> retailItemList;
    private String name;
    private String furnitureDescription;
    private Double price;
    private FurnitureCategory category;
    private FurnitureSubcategory subcategory;   
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    
    @EJB
    private ManageCatalogueBeanLocal mcbl;
    
    @PostConstruct
    public void init() {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        CountryOffice co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        
        furnitureList = mcbl.getStoreFurniture(co);
        retailItemList = mcbl.getStoreRetailItems(co);
        System.out.println("loaded " + co.getName() + " furniture models and retail items");
    }

    public List<FurnitureModel> getFurnitureList() {
        return furnitureList;
    }

    public void setFurnitureList(List<FurnitureModel> furnitureList) {
        this.furnitureList = furnitureList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFurnitureDescription() {
        return furnitureDescription;
    }

    public void setFurnitureDescription(String furnitureDescription) {
        this.furnitureDescription = furnitureDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public FurnitureCategory getCategory() {
        return category;
    }

    public void setCategory(FurnitureCategory category) {
        this.category = category;
    }

    public FurnitureSubcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(FurnitureSubcategory subcategory) {
        this.subcategory = subcategory;
    }

    public ManageCatalogueBeanLocal getMcbl() {
        return mcbl;
    }

    public void setMcbl(ManageCatalogueBeanLocal mcbl) {
        this.mcbl = mcbl;
    }

    public List<RetailItem> getRetailItemList() {
        return retailItemList;
    }

    public void setRetailItemList(List<RetailItem> retailItemList) {
        this.retailItemList = retailItemList;
    }
    
}
