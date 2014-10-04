/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import IslandFurniture.Enums.Month;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class MonthlyProcurementPlanPK implements Serializable {
    private Month month;
    private Integer year;
    private Long retailItem;
    private Long manufacturingFacility;  
    
    public MonthlyProcurementPlanPK(Long MF,Long retailItem,Month m,Integer year){
        this.manufacturingFacility=MF;
        this.retailItem=retailItem;
        this.month=m;
        this.year=year;
        
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonthlyProcurementPlanPK)) {
            return false;
        }
        MonthlyProcurementPlanPK other = (MonthlyProcurementPlanPK) object;
        return this.month.equals(other.month) && this.year.equals(other.year) && this.retailItem.equals(other.retailItem)&& this.manufacturingFacility.equals(other.manufacturingFacility);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.month);
        hash = 53 * hash + Objects.hashCode(this.year);
        hash = 53 * hash + Objects.hashCode(this.retailItem);
        hash = 53 * hash +Objects.hashCode(this.manufacturingFacility);
        return hash;
    }
    
    @Override
    public String toString() {
        return month + ", " + year + ", " + retailItem + manufacturingFacility;
    }
}
