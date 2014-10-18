/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "findCountryOfficeByName",
            query = "SELECT a FROM CountryOffice a WHERE a.name = :name"),
    @NamedQuery(
            name = "getAllCountryOffices",
            query = "SELECT a FROM CountryOffice a")
})
public class CountryOffice extends Plant implements Serializable {

    private static final long serialVersionUID = 1L;
    private String urlCode;
    
    @OneToMany(mappedBy = "countryOffice")
    private List<Store> stores = new ArrayList();

    @OneToMany(mappedBy = "countryOffice")
    private List<ManufacturingFacility> manufacturingFacilities = new ArrayList();

    @OneToMany(mappedBy = "countryOffice")
    private List<MonthlyStockSupplyReq> monthlyStockSupplyReqs = new ArrayList();

    @OneToMany(mappedBy = "countryOffice")
    private List<StockSupplied> suppliedWithFrom = new ArrayList();

    @OneToMany(mappedBy = "countryOffice")
    private List<PromotionCampaign> promotionCampaigns = new ArrayList();

    @OneToMany(mappedBy = "countryOffice")
    private List<RedeemableItem> redeemableItems = new ArrayList();
    
    // Web customisation attributes
    @OneToMany(mappedBy = "countryOffice")
    private List<WebBanner> webBanners;
    @OneToMany
    private List<Stock> featuredProducts;

    public String getUrlCode() {
        return urlCode;
    }

    public void setUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<ManufacturingFacility> getManufacturingFacilities() {
        return manufacturingFacilities;
    }

    public void setManufacturingFacilities(List<ManufacturingFacility> manufacturingFacilities) {
        this.manufacturingFacilities = manufacturingFacilities;
    }

    public List<MonthlyStockSupplyReq> getMonthlyStockSupplyReqs() {
        return monthlyStockSupplyReqs;
    }

    public void setMonthlyStockSupplyReqs(List<MonthlyStockSupplyReq> monthlyStockSupplyReqs) {
        this.monthlyStockSupplyReqs = monthlyStockSupplyReqs;
    }

    public List<StockSupplied> getSuppliedWithFrom() {
        return suppliedWithFrom;
    }

    public void setSuppliedWithFrom(List<StockSupplied> suppliedWithFrom) {
        this.suppliedWithFrom = suppliedWithFrom;
    }

    public List<PromotionCampaign> getPromotionCampaigns() {
        return promotionCampaigns;
    }

    public void setPromotionCampaigns(List<PromotionCampaign> promotionCampaigns) {
        this.promotionCampaigns = promotionCampaigns;
    }

    public List<RedeemableItem> getRedeemableItems() {
        return redeemableItems;
    }

    public void setRedeemableItems(List<RedeemableItem> redeemableItems) {
        this.redeemableItems = redeemableItems;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CountryOffice)) {
            return false;
        }
        CountryOffice other = (CountryOffice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CountryOffice[ id=" + id + " ]";
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
