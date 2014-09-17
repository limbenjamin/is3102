/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class StockSuppliedPK implements Serializable {

    private Long stock;
    private Long countryOffice;

    public StockSuppliedPK() {
    }

    public StockSuppliedPK(Long stock, Long countryOffice) {
        this.stock = stock;
        this.countryOffice = countryOffice;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getStore() {
        return countryOffice;
    }

    public void setStore(Long countryOffice) {
        this.countryOffice = countryOffice;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StockSuppliedPK)) {
            return false;
        }
        StockSuppliedPK other = (StockSuppliedPK) object;
        return this.stock.equals(other.stock) && this.countryOffice.equals(other.countryOffice);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.stock);
        hash = 41 * hash + Objects.hashCode(this.countryOffice);
        return hash;
    }

    @Override
    public String toString() {
        return stock + ", " + countryOffice;
    }
}
