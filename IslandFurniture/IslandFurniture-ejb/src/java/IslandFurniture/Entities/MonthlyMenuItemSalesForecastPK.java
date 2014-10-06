/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import IslandFurniture.Enums.Month;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class MonthlyMenuItemSalesForecastPK implements Serializable {
    private Month month;
    private Integer year;
    private Long countryOffice;
    private Long menuItem;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonthlyMenuItemSalesForecastPK)) {
            return false;
        }
        MonthlyMenuItemSalesForecastPK other = (MonthlyMenuItemSalesForecastPK) object;
        return this.month.equals(other.month) && this.year.equals(other.year) && this.countryOffice.equals(other.countryOffice) && this.menuItem.equals(other.menuItem);
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
    public String toString() {
        return this.month + "," + this.year + "," + this.countryOffice + "," + this.menuItem;
    }
}
