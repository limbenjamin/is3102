/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.WeeklyMRPRecordPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(WeeklyMRPRecordPK.class)
@NamedQueries({
    @NamedQuery(name = "weeklyMRPRecord.findwMRPatMFM", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.year*52+wmrp.month*4+wmrp.week=:m*4 +:w+:y*52 and wmrp.material=:ma"),
    @NamedQuery(name = "weeklyMRPRecord.findwMRPOrderedatMFM", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and WMRP.orderMonth=:m  and WMRP.orderYear=:y and WMRP.orderWeek=:w and wmrp.material=:ma"),
    @NamedQuery(name = "weeklyMRPRecord.findwMRPatMFmonth", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.month=:m and wmrp.year=:y order by wmrp.material.name asc , WMRP.month*4+WMRP.year*52+wmrp.week asc"),
    @NamedQuery(name = "weeklyMRPRecord.findwMRPatMF", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.month=:m and wmrp.week=:w and wmrp.year=:y"),
    @NamedQuery(name = "weeklyMRPRecord.findInventoryforMAT", query = "select sum(wmrp.orderAMT-wmrp.qtyReq) from WeeklyMRPRecord wmrp where (wmrp.month+wmrp.year*12)<=:m+:y*12 and wmrp.material=:ma")
})
public class WeeklyMRPRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private Material material;
    @Id
    private Integer week; //this is the requirement date
    @Id
    private Month month; //requirement date
    @Id
    private Integer year; //requirement date

    @Id
    @ManyToOne
    private ManufacturingFacility manufacturingFacility;

    private Integer orderWeek=1; //this is the order date
    private Month orderMonth; //requirement order date
    private Integer orderYear=1; //requirement order date
    private Integer orderAMT=0;
    private Integer orderLot=0;
    private Integer lotSize=1;

    @OneToOne
    private WeeklyProductionPlan weeklyProductionPlan; // This is useless . remove it next time
    @OneToOne
    private PurchaseOrderDetail purchaseOrderDetail;
    private Integer qtyReq=0;

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }
    

    public Integer getOrderLot() {
        return orderLot;
    }

    public void setOrderLot(Integer orderLot) {
        this.orderLot = orderLot;
    }
    private int leadTime;

    private int onHand;

    public int getOnHand() {
        return onHand;
    }

    public void setOnHand(int onHand) {
        this.onHand = onHand;
    }

    public Integer getOrderAMT() {
        return orderAMT;
    }

    public void setOrderAMT(Integer orderAMT) {
        this.orderAMT = orderAMT;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getOrderWeek() {
        return orderWeek;
    }

    public void setOrderWeek(Integer orderWeek) {
        this.orderWeek = orderWeek;
    }

    public Month getOrderMonth() {
        return orderMonth;
    }

    public void setOrderMonth(Month orderMonth) {
        this.orderMonth = orderMonth;
    }

    public Integer getOrderYear() {
        return orderYear;
    }

    public void setOrderYear(Integer orderYear) {
        this.orderYear = orderYear;
    }

    public void orderMaterial() {

    }

    public Integer getQtyReq() {
        return qtyReq;
    }

    public void setQtyReq(Integer qtyReq) {
        this.qtyReq = qtyReq;
    }

    public Integer getLotSize() {
        return lotSize;
    }

    public void setLotSize(Integer lotSize) {
        this.lotSize = lotSize;
    }

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
