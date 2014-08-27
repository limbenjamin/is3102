/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.MANUFACTURING;

import FW.IslandFurniture.Entities.Enums.Month;
import FW.IslandFurniture.Entities.Keys.MonthlyProductionPlanPK;
import FW.IslandFurniture.Entities.STORE.FurnitureModel;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author asus
 */
@Entity
@IdClass(MonthlyProductionPlanPK.class)
public class MonthlyProductionPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private FurnitureModel furnitureModel;
    @Id
    private Month month;
    @Id
    private Integer year;
    @OneToMany(mappedBy="monthlyProductionPlan")
    private List<WeeklyProductionPlan> weeklyProductionPlans;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += ((furnitureModel != null && month != null && year != null) ? (furnitureModel.hashCode() ^ month.hashCode() ^ year.hashCode()) : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyProductionPlan)) {
            return false;
        }
        MonthlyProductionPlan other = (MonthlyProductionPlan) object;
        return this.furnitureModel.equals(other.furnitureModel) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.MonthlyProductionPlan[ id=" + this.furnitureModel.getId() + ", " + this.month + ", " + this.year + " ]";
    }
    
}
