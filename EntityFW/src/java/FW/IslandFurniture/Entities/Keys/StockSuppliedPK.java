/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.Keys;

import FW.IslandFurniture.Entities.MANUFACTURING.ManufacturingFacility;
import FW.IslandFurniture.Entities.STORE.Stock;
import FW.IslandFurniture.Entities.STORE.Store;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author asus
 */
public class StockSuppliedPK implements Serializable{
    private Stock stock;
    private ManufacturingFacility manufacturingFacility;
    private Store store;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StockSuppliedPK)) {
            return false;
        }
        StockSuppliedPK other = (StockSuppliedPK) object;
        return this.stock.equals(other.stock) && this.manufacturingFacility.equals(other.manufacturingFacility) && this.store.equals(other.store);
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
    public String toString() {
        return this.stock.getId() + ", " + this.manufacturingFacility.getId() + ", " + this.store.getId();
    }
}
