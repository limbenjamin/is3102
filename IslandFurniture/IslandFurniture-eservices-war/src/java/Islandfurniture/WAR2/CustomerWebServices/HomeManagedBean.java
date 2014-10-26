/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.WebBanner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class HomeManagedBean {
    
    private CountryOffice co;
    private List<WebBanner> webBanners;
    private List<Stock> featuredProducts;
    private List<FurnitureModel> featuredFurniture = new ArrayList<>();
    private List<RetailItem> featuredRetailItems;

    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    
    @EJB
    private ManageMarketingBeanLocal mmbl;    
    
    @PostConstruct
    public void init() {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        
        webBanners = co.getWebBanners();
        featuredProducts = co.getFeaturedProducts();
        getAllFeaturedFurniture();
        getAllFeaturedRetailItems();
        System.out.println("loaded " + co.getName() + " web banners");
    }    
    
    public void getAllFeaturedFurniture() {
        Iterator<Stock> iterator = featuredProducts.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next();
            if (stock instanceof FurnitureModel) {
                System.out.println("it is furniture model");
                featuredFurniture.add((FurnitureModel)stock);
            }
        }        
    }
    
    public void getAllFeaturedRetailItems() {
        Iterator<Stock> iterator = featuredProducts.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next();
            if (stock instanceof RetailItem) {
                featuredRetailItems.add((RetailItem)stock);
            }
        }        
    }
    
    public Double getDiscountedPrice(Stock s) {
        Store st = new Store();
        st.setCountryOffice(co);
        return (Double)mmbl.getDiscountedPrice(s, st, new Customer()).get("D_PRICE");
    }    
    
    public String checkForActive(int index) {
        if (index == 0)
            return "active";
        else
            return "";
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public List<WebBanner> getWebBanners() {
        return webBanners;
    }

    public void setWebBanners(List<WebBanner> webBanners) {
        this.webBanners = webBanners;
    }

    public List<Stock> getFeaturedProducts() {
        return featuredProducts;
    }

    public void setFeaturedProducts(List<Stock> featuredProducts) {
        this.featuredProducts = featuredProducts;
    }

    public List<FurnitureModel> getFeaturedFurniture() {
        return featuredFurniture;
    }

    public void setFeaturedFurniture(List<FurnitureModel> featuredFurniture) {
        this.featuredFurniture = featuredFurniture;
    }

    public List<RetailItem> getFeaturedRetailItems() {
        return featuredRetailItems;
    }

    public void setFeaturedRetailItems(List<RetailItem> featuredRetailItems) {
        this.featuredRetailItems = featuredRetailItems;
    }
}
