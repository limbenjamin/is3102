/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities.Keys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class ProductionCapacityPK implements Serializable {
    private Long stock;
    private Long manufacturingFacility;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductionCapacityPK)) {
            return false;
        }
        ProductionCapacityPK other = (ProductionCapacityPK) object;
        return this.stock.equals(other.stock) && this.manufacturingFacility.equals(other.manufacturingFacility);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.stock);
        hash = 89 * hash + Objects.hashCode(this.manufacturingFacility);
        return hash;
    }
    
    @Override
    public String toString() {
        return stock + ", " + manufacturingFacility;
    }
}
