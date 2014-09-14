/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import IslandFurniture.EJB.Entities.Month;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class MonthlyStockSupplyReqPK implements Serializable {
    private Long stock;
    private Long store;
    private Month month;
    private Integer year;

    public MonthlyStockSupplyReqPK(Long stock, Long store, Month month, Integer year) {
        this.stock = stock;
        this.store = store;
        this.month = month;
        this.year = year;
    }
    
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
        return stock + "," + store + "," + month + "," + year;
    }
}
