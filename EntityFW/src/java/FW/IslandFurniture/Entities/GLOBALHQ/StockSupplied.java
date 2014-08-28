/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.GLOBALHQ;

import FW.IslandFurniture.Entities.Keys.StockSuppliedPK;
import FW.IslandFurniture.Entities.MANUFACTURING.ManufacturingFacility;
import FW.IslandFurniture.Entities.STORE.Stock;
import FW.IslandFurniture.Entities.STORE.Store;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author asus
 */
@Entity
@IdClass(StockSuppliedPK.class)
public class StockSupplied implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private Stock stock;
    @Id
    @ManyToOne
    private ManufacturingFacility manufacturingFacility;
    @Id
    @ManyToOne
    private Store store;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.stock);
        hash = 89 * hash + Objects.hashCode(this.manufacturingFacility);
        hash = 89 * hash + Objects.hashCode(this.store);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockSupplied)) {
            return false;
        }
        StockSupplied other = (StockSupplied) object;
        return this.stock.equals(other.stock) && this.manufacturingFacility.equals(other.manufacturingFacility) && this.store.equals(other.store);
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.GLOBALHQ.StockSupplied[ id=" + this.stock.getId() + ", " + this.manufacturingFacility.getId() + ", " + this.store.getId() + " ]";
    }
    
}
