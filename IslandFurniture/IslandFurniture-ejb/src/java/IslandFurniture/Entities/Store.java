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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;

/**
 *
 * @author Benjamin
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getAllStores",
            query = "SELECT a FROM Store a")
})
public class Store extends Plant implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    private CountryOffice countryOffice;

    @OneToMany(mappedBy = "store")
    private List<MonthlyMenuItemSalesForecast> monthlyMenuItemSalesForecasts = new ArrayList();

    @ManyToMany
    private List<Stock> sells = new ArrayList();

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public List<MonthlyMenuItemSalesForecast> getMonthlyMenuItemSalesForecasts() {
        return monthlyMenuItemSalesForecasts;
    }

    public void setMonthlyMenuItemSalesForecasts(List<MonthlyMenuItemSalesForecast> monthlyMenuItemSalesForecasts) {
        this.monthlyMenuItemSalesForecasts = monthlyMenuItemSalesForecasts;
    }

    public List<Stock> getSells() {
        return sells;
    }

    public void setSells(List<Stock> sells) {
        this.sells = sells;
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
        if (!(object instanceof Store)) {
            return false;
        }
        Store other = (Store) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Store[ id=" + id + " ]";
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
