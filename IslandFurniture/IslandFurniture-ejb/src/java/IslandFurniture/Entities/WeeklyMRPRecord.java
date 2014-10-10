/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.Month;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
//@IdClass(WeeklyMRPRecordPK.class)
@NamedQueries({
    @NamedQuery(name = "weeklyMRPRecord.findwMRPatMFMnospecmat", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.year=:y and wmrp.month+0=:m+0 and wmrp.week=:w order by wmrp.year*52+wmrp.month*4+wmrp.week asc"),
    @NamedQuery(name = "weeklyMRPRecord.findwMRPatMFM", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.year=:y and wmrp.month+0=:m+0 and wmrp.week=:w and wmrp.material=:ma"),
    @NamedQuery(name = "weeklyMRPRecord.findwMRPOrderedatMFM", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and WMRP.orderMonth=:m  and WMRP.orderYear=:y and WMRP.orderWeek=:w and wmrp.material=:ma"),
    @NamedQuery(name = "weeklyMRPRecord.findwMRPatMFmonth", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.month=:m and wmrp.year=:y order by wmrp.material.name asc , WMRP.year*1000+ WMRP.month*10+wmrp.week asc"),
    @NamedQuery(name = "weeklyMRPRecord.findwMRPatMF", query = "select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.month=:m and wmrp.week=:w and wmrp.year=:y"),
    @NamedQuery(name = "weeklyMRPRecord.findInventoryforMAT", query = "select sum(wmrp.orderAMT-wmrp.qtyReq) from WeeklyMRPRecord wmrp where (wmrp.month+wmrp.year*12)<=:m+:y*12 and wmrp.material=:ma")
})
public class WeeklyMRPRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer week; //this is the requirement date
    private Month month; //requirement date
    private Integer year; //requirement date

    @ManyToOne
    private Material material;

    private Integer orderWeek = 1; //this is the order date
    private Month orderMonth; //requirement order date
    private Integer orderYear = 1; //requirement order date
    private Integer orderAMT = 0;
    private Integer orderLot = 0;
    private Integer lotSize = 1;
    private Integer plannedOrder = 0;
    private Integer leadTime;
    private Integer onHand;
    private Integer qtyReq = 0;

    @ManyToOne
    private ManufacturingFacility manufacturingFacility;

    @OneToOne
    private ProcuredStockPurchaseOrderDetail purchaseOrderDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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

    public Integer getOrderAMT() {
        return orderAMT;
    }

    public void setOrderAMT(Integer orderAMT) {
        this.orderAMT = orderAMT;
    }

    public Integer getOrderLot() {
        return orderLot;
    }

    public void setOrderLot(Integer orderLot) {
        this.orderLot = orderLot;
    }

    public Integer getLotSize() {
        return lotSize;
    }

    public void setLotSize(Integer lotSize) {
        this.lotSize = lotSize;
    }

    public Integer getPlannedOrder() {
        return plannedOrder;
    }

    public void setPlannedOrder(Integer plannedOrder) {
        this.plannedOrder = plannedOrder;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getOnHand() {
        return onHand;
    }

    public void setOnHand(Integer onHand) {
        this.onHand = onHand;
    }

    public Integer getQtyReq() {
        return qtyReq;
    }

    public void setQtyReq(Integer qtyReq) {
        this.qtyReq = qtyReq;
    }

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }

    public ProcuredStockPurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(ProcuredStockPurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.material);
        hash = 97 * hash + Objects.hashCode(this.week);
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        hash = 97 * hash + Objects.hashCode(this.manufacturingFacility);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WeeklyMRPRecord)) {
            return false;
        }
        WeeklyMRPRecord other = (WeeklyMRPRecord) object;
        return this.material.equals(other.material) && this.week.equals(other.week) && this.month.equals(other.month) && this.year.equals(other.year) && this.manufacturingFacility.equals((other.manufacturingFacility));
    }

    @Override
    public String toString() {
        return "WeeklyMRPRecord[ id=" + this.material.getId() + "," + week + "," + month + "," + year + " ]";
    }
}
