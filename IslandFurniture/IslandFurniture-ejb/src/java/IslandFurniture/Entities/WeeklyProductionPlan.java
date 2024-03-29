/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.EJB.Manufacturing.ManageProductionPlanTimerBean;
import IslandFurniture.StaticClasses.Helper;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "WeeklyProductionPlan.getForMF", query = "select wpp from WeeklyProductionPlan wpp where wpp.monthlyProductionPlan.manufacturingFacility=:MF and wpp.monthlyProductionPlan.month+wpp.monthlyProductionPlan.year*12=:m+:y*12  ORDER BY wpp.monthlyProductionPlan.furnitureModel.name ASC,wpp.WeekNo ASC"),
    @NamedQuery(name = "WeeklyProductionPlan.getForMFatWK", query = "select wpp from WeeklyProductionPlan wpp where wpp.monthlyProductionPlan.manufacturingFacility=:MF and wpp.monthlyProductionPlan.month+wpp.monthlyProductionPlan.year*12=:m+:y*12 and wpp.WeekNo=:wk  ORDER BY wpp.monthlyProductionPlan.furnitureModel.name ASC,wpp.WeekNo ASC")
})
public class WeeklyProductionPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private MonthlyProductionPlan monthlyProductionPlan;
    @OneToOne
    private ProductionOrder productionOrder;

    private int WeekNo;

    private int QTY;

    private boolean locked;

    public int getQTY() {
        return QTY;
    }

    public boolean isLocked() {

        try {


            Calendar t = Helper.getStartDateOfWeek(this.getMonthlyProductionPlan().getMonth().value, this.getMonthlyProductionPlan().getYear(), this.getWeekNo());
            t.add(Calendar.DATE, -1);

            if (t.before(ManageProductionPlanTimerBean.cdate.getCalendar())) { //expired
                System.out.println("Expired(): WPP=" + this.monthlyProductionPlan.getMonth() + "/" + this.monthlyProductionPlan.getYear() + "/" + this.WeekNo + " VS Server Time:" + ManageProductionPlanTimerBean.cdate.getCalendar().get(Calendar.MONTH) + "/" + ManageProductionPlanTimerBean.cdate.getCalendar().get(Calendar.YEAR));
                return true;
            }

        } catch (Exception ex) {
        }

        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setQTY(int QTY) {
        this.QTY = QTY;
    }

    public Long getId() {
        return id;
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
        return "WeeklyProductionPlan[ id=" + id + " ]";
    }

    public int getWeekNo() {
        return WeekNo;
    }

    public void setWeekNo(int WeekNo) {
        this.WeekNo = WeekNo;
    }

}
