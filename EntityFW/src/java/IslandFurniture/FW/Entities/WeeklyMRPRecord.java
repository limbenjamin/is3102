/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Enums.Month;
import IslandFurniture.FW.Entities.Keys.WeeklyMRPRecordPK;
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public WeeklyProductionPlan getWeeklyProductionPlan() {
        return weeklyProductionPlan;
    }

    public void setWeeklyProductionPlan(WeeklyProductionPlan weeklyProductionPlan) {
        this.weeklyProductionPlan = weeklyProductionPlan;
    }

    public PurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

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
