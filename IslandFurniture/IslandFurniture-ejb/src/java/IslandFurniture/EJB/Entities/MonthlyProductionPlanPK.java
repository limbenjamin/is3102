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
public class MonthlyProductionPlanPK implements Serializable{
    private Long furnitureModel;
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
        return this.furnitureModel + "," + month + "," + year;
    }
}
