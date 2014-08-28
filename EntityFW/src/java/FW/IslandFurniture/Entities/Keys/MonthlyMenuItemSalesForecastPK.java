/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.Keys;

import FW.IslandFurniture.Entities.Enums.Month;
import FW.IslandFurniture.Entities.STORE.MenuItem;
import FW.IslandFurniture.Entities.STORE.Store;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author asus
 */
public class MonthlyMenuItemSalesForecastPK implements Serializable {
    private Month month;
    private Integer year;
    private Store store;
    private MenuItem menuItem;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonthlyMenuItemSalesForecastPK)) {
            return false;
        }
        MonthlyMenuItemSalesForecastPK other = (MonthlyMenuItemSalesForecastPK) object;
        return this.month.equals(other.month) && this.year.equals(other.year) && this.store.equals(other.store) && this.menuItem.equals(other.menuItem);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.month);
        hash = 37 * hash + Objects.hashCode(this.year);
        hash = 37 * hash + Objects.hashCode(this.store);
        hash = 37 * hash + Objects.hashCode(this.menuItem);
        return hash;
    }
    
    @Override
    public String toString() {
        return this.month + "," + this.year + "," + this.store.getId() + "," + this.menuItem.getId();
    }
}
