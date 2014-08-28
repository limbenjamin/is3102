/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.Keys;

import FW.IslandFurniture.Entities.Enums.Month;
import FW.IslandFurniture.Entities.STORE.FurnitureModel;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author asus
 */
public class MonthlyProductionPlanPK implements Serializable{
    private FurnitureModel furnitureModel;
    private Month month;
    private Integer year;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonthlyProductionPlanPK)) {
            return false;
        }
        MonthlyProductionPlanPK other = (MonthlyProductionPlanPK) object;
        return this.furnitureModel.equals(other.furnitureModel) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.furnitureModel);
        hash = 53 * hash + Objects.hashCode(this.month);
        hash = 53 * hash + Objects.hashCode(this.year);
        return hash;
    }
    
    @Override
    public String toString() {
        return this.furnitureModel.getId() + "," + month + "," + year;
    }
}
