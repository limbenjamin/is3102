/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import IslandFurniture.EJB.Enums.Month;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class MonthlyStockSupplyReqPK implements Serializable {
    private Long stock;
    private Long countryOffice;
    private Month month;
    private Integer year;

    public MonthlyStockSupplyReqPK(Long stock, Long countryOffice, Month month, Integer year) {
        this.stock = stock;
        this.countryOffice = countryOffice;
        this.month = month;
        this.year = year;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonthlyStockSupplyReqPK)) {
            return false;
        }
        MonthlyStockSupplyReqPK other = (MonthlyStockSupplyReqPK) object;
        return this.stock.equals(other.stock) && this.countryOffice.equals(other.countryOffice) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.stock);
        hash = 97 * hash + Objects.hashCode(this.countryOffice);
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        return hash;
    }
    
    @Override
    public String toString() {
        return stock + "," + countryOffice + "," + month + "," + year;
    }
}
