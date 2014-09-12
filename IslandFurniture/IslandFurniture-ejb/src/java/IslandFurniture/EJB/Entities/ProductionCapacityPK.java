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
public class ProductionCapacityPK implements Serializable {

    private Long furnitureModel;
    private Long manufacturingFacility;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductionCapacityPK)) {
            return false;
        }
        ProductionCapacityPK other = (ProductionCapacityPK) object;
        return this.furnitureModel.equals(other.furnitureModel) && this.manufacturingFacility.equals(other.manufacturingFacility);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.furnitureModel);
        hash = 59 * hash + Objects.hashCode(this.manufacturingFacility);
        return hash;
    }

    @Override
    public String toString() {
        return furnitureModel + ", " + manufacturingFacility;
    }
}
