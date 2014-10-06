/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecastPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author James
 */
@Entity
@IdClass(MonthlyMenuItemSalesForecastPK.class)
public class MonthlyMenuItemSalesForecast implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Month month;
    @Id
    private Integer year;
    @Id
    @ManyToOne
    private CountryOffice countryOffice;
    @Id
    @ManyToOne
    private MenuItem menuItem;

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.month);
        hash = 13 * hash + Objects.hashCode(this.year);
        hash = 13 * hash + Objects.hashCode(this.countryOffice);
        hash = 13 * hash + Objects.hashCode(this.menuItem);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyMenuItemSalesForecast)) {
            return false;
        }
        MonthlyMenuItemSalesForecast other = (MonthlyMenuItemSalesForecast) object;
        return this.month.equals(other.month) && this.year.equals(other.year) && this.countryOffice.equals(other.countryOffice) && this.menuItem.equals(other.menuItem);
    }

    @Override
    public String toString() {
        return "MonthlyMenuItemSalesForecast[ id=" + this.month + ", " + this.year + ", " + this.countryOffice + this.menuItem + " ]";
    }

}