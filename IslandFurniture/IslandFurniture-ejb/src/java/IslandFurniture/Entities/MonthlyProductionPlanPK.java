/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.Month;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class MonthlyProductionPlanPK implements Serializable {

    private Long manufacturingFacility;
    private Long furnitureModel;
    private Month month;
    private Integer year;
    
    public MonthlyProductionPlanPK(Long MF,Long fm,Month m,Integer year){
        this.manufacturingFacility=MF;
        this.furnitureModel=fm;
        this.month=m;
        this.year=year;
        
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonthlyProductionPlanPK)) {
            return false;
        }
        MonthlyProductionPlanPK other = (MonthlyProductionPlanPK) object;
        return this.manufacturingFacility.equals(other.manufacturingFacility) && this.furnitureModel.equals(other.furnitureModel) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.manufacturingFacility);
        hash = 29 * hash + Objects.hashCode(this.furnitureModel);
        hash = 29 * hash + Objects.hashCode(this.month);
        hash = 29 * hash + Objects.hashCode(this.year);
        return hash;
    }

    @Override
    public String toString() {
        return this.manufacturingFacility + ", " + this.furnitureModel + ", " + month + ", " + year;
    }
}
