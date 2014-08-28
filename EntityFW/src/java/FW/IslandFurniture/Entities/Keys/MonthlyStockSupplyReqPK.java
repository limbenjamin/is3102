/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.Keys;

import FW.IslandFurniture.Entities.Enums.Month;
import FW.IslandFurniture.Entities.STORE.Store;
import FW.IslandFurniture.Entities.STORE.Stock;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author asus
 */
public class MonthlyStockSupplyReqPK implements Serializable {
    private Stock stock;
    private Store store;
    private Month month;
    private Integer year;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonthlyStockSupplyReqPK)) {
            return false;
        }
        MonthlyStockSupplyReqPK other = (MonthlyStockSupplyReqPK) object;
        return this.stock.equals(other.stock) && this.store.equals(other.store) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.stock);
        hash = 97 * hash + Objects.hashCode(this.store);
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        return hash;
    }
    
    @Override
    public String toString() {
        return this.stock.getId() + "," + store.getId() + "," + month + "," + year;
    }
}
