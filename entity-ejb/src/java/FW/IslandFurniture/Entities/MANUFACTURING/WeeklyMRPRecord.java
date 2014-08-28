/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.MANUFACTURING;

import FW.IslandFurniture.Entities.Enums.Month;
import FW.IslandFurniture.Entities.Keys.WeeklyMRPRecordPK;
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
@IdClass(WeeklyMRPRecordPK.class)
public class WeeklyMRPRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private Material material;
    @Id
    private Integer week;
    @Id
    private Month month;
    @Id
    private Integer year;
    @OneToOne
    private WeeklyProductionPlan weeklyProductionPlan;
    @OneToOne
    private PurchaseOrderDetail purchaseOrderDetail;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.material);
        hash = 97 * hash + Objects.hashCode(this.week);
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WeeklyMRPRecord)) {
            return false;
        }
        WeeklyMRPRecord other = (WeeklyMRPRecord) object;
        return this.material.equals(other.material) && this.week.equals(other.week) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.WeeklyMRPRecord[ id=" + this.material.getId() + "," + week + "," + month + "," + year + " ]";
    }
    
}
