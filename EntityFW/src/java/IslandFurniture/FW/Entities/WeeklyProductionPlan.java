/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author asus
 */
@Entity
public class WeeklyProductionPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private MonthlyProductionPlan monthlyProductionPlan;
    @OneToOne
    private ProductionOrder productionOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MonthlyProductionPlan getMonthlyProductionPlan() {
        return monthlyProductionPlan;
    }

    public void setMonthlyProductionPlan(MonthlyProductionPlan monthlyProductionPlan) {
        this.monthlyProductionPlan = monthlyProductionPlan;
    }

    public ProductionOrder getProductionOrder() {
        return productionOrder;
    }

    public void setProductionOrder(ProductionOrder productionOrder) {
        this.productionOrder = productionOrder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WeeklyProductionPlan)) {
            return false;
        }
        WeeklyProductionPlan other = (WeeklyProductionPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.WeeklyProductionPlan[ id=" + id + " ]";
    }
    
}
