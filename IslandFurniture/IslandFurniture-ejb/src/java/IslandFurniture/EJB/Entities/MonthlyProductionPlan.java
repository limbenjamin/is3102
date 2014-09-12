/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlanPK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(MonthlyProductionPlanPK.class)
public class MonthlyProductionPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private ManufacturingFacility manufacturingFacility;
    @Id
    @ManyToOne(cascade = {CascadeType.ALL})
    private FurnitureModel furnitureModel;
    @Id
    private Month month;
    @Id
    private Integer year;

    @OneToMany(mappedBy = "monthlyProductionPlan", cascade = {CascadeType.ALL})
    private List<WeeklyProductionPlan> weeklyProductionPlans = new ArrayList<WeeklyProductionPlan>();

    private Integer QTY;

    private Boolean locked = false;

    @OneToMany(mappedBy = "monthlyProductionPlan", cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID"),
        @JoinColumn(name = "FURNITUREMODEL_ID", referencedColumnName = "STOCK_ID", insertable = false, updatable = false),
        @JoinColumn(name = "MONTH", referencedColumnName = "MONTH", insertable = false, updatable = false),
        @JoinColumn(name = "YEAR", referencedColumnName = "YEAR", insertable = false, updatable = false)
    })
    private List<MonthlyStockSupplyReq> monthlyStockSupplyReqs = new ArrayList<MonthlyStockSupplyReq>();

    @OneToOne(cascade = {CascadeType.ALL}) //Kind of like the linked list approach
    @JoinColumns({
        @JoinColumn(name = "NEXT_MANUFACTURINGFACILITY_ID", referencedColumnName = "MANUFACTURINGFACILITY_ID"),
        @JoinColumn(name = "NEXT_FURNITUREMODEL_ID", referencedColumnName = "FURNITUREMODEL_ID"),
        @JoinColumn(name = "NEXT_MONTH", referencedColumnName = "MONTH"),
        @JoinColumn(name = "NEXT_YEAR", referencedColumnName = "YEAR")
    })
    private MonthlyProductionPlan nextMonthlyProductionPlan;

    @OneToOne(mappedBy = "nextMonthlyProductionPlan", cascade = {CascadeType.ALL}) //Kind of like the linked list approach it works ok . special case of JPA 
    //http://stackoverflow.com/questions/3393515/jpa-how-to-have-one-to-many-relation-of-the-same-entity-type
    private MonthlyProductionPlan prevMonthlyProductionPlan;

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }

    public FurnitureModel getFurnitureModel() {
        return furnitureModel;
    }

    public void setFurnitureModel(FurnitureModel furnitureModel) {
        this.furnitureModel = furnitureModel;
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

    public List<WeeklyProductionPlan> getWeeklyProductionPlans() {
        return weeklyProductionPlans;
    }

    public void setWeeklyProductionPlans(List<WeeklyProductionPlan> weeklyProductionPlans) {
        this.weeklyProductionPlans = weeklyProductionPlans;
    }

    public Integer getQTY() {
        return QTY;
    }

    public void setQTY(Integer QTY) {
        this.QTY = QTY;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public List<MonthlyStockSupplyReq> getMonthlyStockSupplyReqs() {
        return monthlyStockSupplyReqs;
    }

    public void setMonthlyStockSupplyReqs(List<MonthlyStockSupplyReq> monthlyStockSupplyReqs) {
        this.monthlyStockSupplyReqs = monthlyStockSupplyReqs;
    }

    public MonthlyProductionPlan getNextMonthlyProductionPlan() {
        return nextMonthlyProductionPlan;
    }

    public void setNextMonthlyProductionPlan(MonthlyProductionPlan nextMonthlyProductionPlan) {
        this.nextMonthlyProductionPlan = nextMonthlyProductionPlan;
    }

    public MonthlyProductionPlan getPrevMonthlyProductionPlan() {
        return prevMonthlyProductionPlan;
    }

    public void setPrevMonthlyProductionPlan(MonthlyProductionPlan prevMonthlyProductionPlan) {
        this.prevMonthlyProductionPlan = prevMonthlyProductionPlan;
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyProductionPlan)) {
            return false;
        }
        MonthlyProductionPlan other = (MonthlyProductionPlan) object;
        return this.manufacturingFacility.equals(other.manufacturingFacility) && this.furnitureModel.equals(other.furnitureModel) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public String toString() {
        return "MonthlyProductionPlan[ id=" + this.manufacturingFacility.getId() + ", " + this.furnitureModel.getId() + ", " + this.month + ", " + this.year + " ]";
    }

    // Extra Methods
    public long getTotalDemand() {
        long total = 0;

        for (MonthlyStockSupplyReq mssr : monthlyStockSupplyReqs) {
            total += mssr.getQtyRequested();
        }

        return (total);
    }

    public int getNumWorkDays() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, this.month.value, 1);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
