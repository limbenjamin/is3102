/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurniture.StaticClasses.Helper.QueryMethods;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Query;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(MonthlyProductionPlanPK.class)
@NamedQueries({
    @NamedQuery(name = "MonthlyProductionPlan.FindAllInPeriod", query = "select MPP from MonthlyProductionPlan MPP where MPP.month=:m and MPP.year=:y"),
    @NamedQuery(name = "MonthlyProductionPlan.Find", query = "select MPP from MonthlyProductionPlan MPP where MPP.furnitureModel=:fm and MPP.month=:m and MPP.year=:y"),
    @NamedQuery(name = "MonthlyProductionPlan.FindUntil", query = "select MPP from MonthlyProductionPlan MPP where MPP.furnitureModel=:fm and ((MPP.month+1)+MPP.year*12)<=(:m+1)+:y*12 and MPP.locked=false"),
    @NamedQuery(name = "MonthlyProductionPlan.FindUntilAllModel", query = "select MPP from MonthlyProductionPlan MPP where ((MPP.month+1)+MPP.year*12)<=(:m+1)+:y*12 and MPP.locked=false order by MPP.furnitureModel.name"),
    @NamedQuery(name="MonthlyProductionPlan.FindAllOfMF",query = "select MPP from MonthlyProductionPlan MPP where MPP.manufacturingFacility=:mf ORDER BY MPP.furnitureModel.name ASC, MPP.year*12+MPP.month ASC")
})
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



    public int getNumWorkDays() {

        return Helper.getNumWorkDays(this.month, this.year);
    }
}
