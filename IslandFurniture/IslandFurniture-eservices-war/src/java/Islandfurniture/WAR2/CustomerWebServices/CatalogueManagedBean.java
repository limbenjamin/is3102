/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class CatalogueManagedBean implements Serializable{

    private List<FurnitureModel> furnitureList = new ArrayList<>();
    private List<RetailItem> retailItemList;
    private String name;
    private String furnitureDescription;
    private Double price;
    private FurnitureCategory category;
    private FurnitureSubcategory subcategory;
    private List<FurnitureSubcategory> subcategories = new ArrayList<>();
    private CountryOffice co;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    
    @EJB
    private ManageCatalogueBeanLocal mcbl;
    
    @EJB
    private ManageMarketingBeanLocal mmbl;
    
    @PostConstruct
    public void init() {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        
        List<FurnitureModel> tempFurnitureList = mcbl.getStoreFurniture(co);
        //retailItemList = mcbl.getStoreRetailItems(co);
        
        for (FurnitureModel furniture : tempFurnitureList) {
            if (!subcategories.contains(furniture.getSubcategory()))
                subcategories.add(furniture.getSubcategory());
        }
        
        String sub = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sub");
        if (sub != null) {
            for (FurnitureModel furniture : tempFurnitureList) {
                if (furniture.getSubcategory().toString().equals(sub))
                    furnitureList.add(furniture);
            }            
        } else {
            furnitureList = tempFurnitureList;
        }
        
        System.out.println("loaded " + co.getName() + " furniture models and retail items");
    }
    
    public void displayProductDetails(ActionEvent event) throws IOException {
      System.out.println("displayProductDetails()");
      // get country office code
      HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
      String coCode = (String) httpReq.getAttribute("coCode");
      
      String furnitureId = event.getComponent().getAttributes().get("furnitureId").toString();
      ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
      
      ec.redirect(ec.getRequestContextPath() + "/" + coCode + "/productdetail.xhtml?id=" + furnitureId);
    }
    
    public List<FurnitureModel> filteredCategory(String category) {
        List<FurnitureModel> categoryList = new ArrayList<>();
        for (FurnitureModel furniture : furnitureList) {
            if (furniture.getCategory().toString().equals(category))
                categoryList.add(furniture);
        }
        return categoryList;
    }
    
    public List<FurnitureSubcategory> filteredSubs(String category) {
        List<FurnitureSubcategory> subList = new ArrayList<>();
        List<FurnitureModel> tempFurnitureList = mcbl.getStoreFurniture(co);
        for (FurnitureModel furniture : tempFurnitureList) {
            if (furniture.getCategory().toString().equals(category) && !subList.contains(furniture.getSubcategory()))
                subList.add(furniture.getSubcategory());
        }
        return subList;
    }    
    
    public Double getDiscountedPrice(Stock s) {
        Store st = new Store();
        st.setCountryOffice(co);
        return (Double)mmbl.getDiscountedPrice(s, st, new Customer()).get("D_PRICE");
    }

    public List<FurnitureModel> getFurnitureList() {
        return furnitureList;
    }

    public void setFurnitureList(List<FurnitureModel> furnitureList) {
        this.furnitureList = furnitureList;
    }
    
    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
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

    public List<FurnitureSubcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<FurnitureSubcategory> subcategories) {
        this.subcategories = subcategories;
    }
    
}
