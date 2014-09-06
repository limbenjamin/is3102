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
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
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
    private FurnitureModel furnitureModel;
    @Id
    private Month month;
    @Id
    private Integer year;
    
    @OneToMany(mappedBy="monthlyProductionPlan")
    private List<WeeklyProductionPlan> weeklyProductionPlans=new ArrayList<WeeklyProductionPlan>();
    
    private Integer QTY;
    
    private boolean locked=false;
    
    @OneToMany(mappedBy = "monthlyProductionPlan")
    protected List<MonthlyStockSupplyReq> monthlyStockSupplyReqs=new ArrayList<MonthlyStockSupplyReq>();
    
    @OneToOne
    private ProductionCapacity productionCapacity=null;
//    
//    @OneToOne //Kind of like the linked list approach
//    private MonthlyProductionPlan nextMonthlyProcurementPlan;
//    
//    @OneToOne(mappedBy = "nextMonthlyProcurementPlan") //Kind of like the linked list approach it works ok . special case of JPA 
//    //http://stackoverflow.com/questions/3393515/jpa-how-to-have-one-to-many-relation-of-the-same-entity-type
//    private MonthlyProductionPlan prevMonthlyProcurementPlan;

    
//    public long get_total_demand()
//    {
//        long total=0;
//        
//        for (MonthlyStockSupplyReq mssr : monthlyStockSupplyReqs)
//        {
//            total+=mssr.getQtyRequested();
//        }
//        
//        return(total);
//    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.furnitureModel);
        hash = 53 * hash + Objects.hashCode(this.month);
        hash = 53 * hash + Objects.hashCode(this.year);
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

    public Integer getQTY() {
        return QTY;
    }

    public void setQTY(Integer QTY) {
        this.QTY = QTY;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

//    public List<MonthlyStockSupplyReq> getMonthlyStockSupplyReqs() {
//        return monthlyStockSupplyReqs;
//    }
//
//    public void setMonthlyStockSupplyReqs(List<MonthlyStockSupplyReq> monthlyStockSupplyReqs) {
//        this.monthlyStockSupplyReqs = monthlyStockSupplyReqs;
//    }

    public ProductionCapacity getPc() {
        return productionCapacity;
    }

    public void setPc(ProductionCapacity pc) {
        this.productionCapacity = pc;
    }

    public ProductionCapacity getProductionCapacity() {
        return productionCapacity;
    }

    public void setProductionCapacity(ProductionCapacity productionCapacity) {
        this.productionCapacity = productionCapacity;
    }
//
//    public MonthlyProductionPlan getNextMonthlyProcurementPlan() {
//        return nextMonthlyProcurementPlan;
//    }
//
//    public void setNextMonthlyProcurementPlan(MonthlyProductionPlan nextMonthlyProcurementPlan) {
//        this.nextMonthlyProcurementPlan = nextMonthlyProcurementPlan;
//    }
//
//    public MonthlyProductionPlan getPrevMonthlyProcurementPlan() {
//        return prevMonthlyProcurementPlan;
//    }
//
//    public void setPrevMonthlyProcurementPlan(MonthlyProductionPlan prevMonthlyProcurementPlan) {
//        this.prevMonthlyProcurementPlan = prevMonthlyProcurementPlan;
//    }


}
