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
    private Long manufacturingFacility;

    public StockSuppliedPK() {
    }

    public StockSuppliedPK(Long stock, Long countryOffice, Long manufacturingFacility) {
        this.stock = stock;
        this.countryOffice = countryOffice;
        this.manufacturingFacility = manufacturingFacility;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(Long countryOffice) {
        this.countryOffice = countryOffice;
    }

    public Long getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(Long manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StockSuppliedPK)) {
            return false;
        }
        StockSuppliedPK other = (StockSuppliedPK) object;
        return this.stock.equals(other.stock) && this.countryOffice.equals(other.countryOffice) && this.manufacturingFacility.equals(other.manufacturingFacility);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.stock);
        hash = 17 * hash + Objects.hashCode(this.countryOffice);
        hash = 17 * hash + Objects.hashCode(this.manufacturingFacility);
        return hash;
    }

    @Override
    public String toString() {
        return stock + ", " + countryOffice + ", " + manufacturingFacility;
    }
}
