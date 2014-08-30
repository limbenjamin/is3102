/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities.Keys;

import IslandFurniture.FW.Enums.Month;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class MonthlyProcurementPlanPK implements Serializable {
    private Month month;
    private Integer year;
    private Long retailItem;
    private Long purchaseOrderDetail;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonthlyProcurementPlanPK)) {
            return false;
        }
        MonthlyProcurementPlanPK other = (MonthlyProcurementPlanPK) object;
        return this.month.equals(other.month) && this.year.equals(other.year) && this.retailItem.equals(other.retailItem) && this.purchaseOrderDetail.equals(other.purchaseOrderDetail);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.month);
        hash = 53 * hash + Objects.hashCode(this.year);
        hash = 53 * hash + Objects.hashCode(this.retailItem);
        hash = 53 * hash + Objects.hashCode(this.purchaseOrderDetail);
        return hash;
    }
    
    @Override
    public String toString() {
        return month + ", " + year + ", " + retailItem + ", " + this.purchaseOrderDetail;
    }
}
