/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import IslandFurniture.StaticClasses.Helper.Couple;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(MonthlyStockSupplyReqPK.class)
@NamedQueries({
    @NamedQuery(name = "MonthlyStockSupplyReq.find", query = "select MSSR from MonthlyStockSupplyReq MSSR where MSSR.year=:y and MSSR.stock=:fm and MSSR.month=:m"),
    @NamedQuery(name = "MonthlyStockSupplyReq.finduntiltime", query = "select MSSR from MonthlyStockSupplyReq MSSR where MSSR.year*12+(MSSR.month+1)<=:y*12+(:m+1)"),
    @NamedQuery(
            name = "getMssrByCoStock",
            query = "SELECT a FROM MonthlyStockSupplyReq a WHERE "
            + "a.countryOffice = :countryOffice AND a.stock = :stock AND "
            + "a.year*12 + a.month >= :startYr*12 + :startMth AND "
            + "a.year*12 + a.month <= :endYr*12 + :endMth"),
    @NamedQuery(
            name = "MonthlyStockSupplyReq.FindByCoStockBefore",
            query = "SELECT MSSR FROM MonthlyStockSupplyReq MSSR WHERE MSSR.countryOffice = :co and MSSR.stock=:stock and MSSR.year*12+(MSSR.month+1)<=:y*12+(:m+1) and MSSR.approved=TRUE and MSSR.year*12+(MSSR.month+1)>=:ny*12+(:nm+1)"),
    @NamedQuery(
            name = "MonthlyStockSupplyReq.FindByCoStockAT",
            query = "SELECT MSSR FROM MonthlyStockSupplyReq MSSR WHERE MSSR.countryOffice = :co and MSSR.stock=:stock and MSSR.year*12+(MSSR.month+1)=:y*12+(:m+1) and MSSR.approved=TRUE"),

    @NamedQuery(
            name = "getMssrByCO",
            query = "SELECT a FROM MonthlyStockSupplyReq a WHERE a.countryOffice=:countryOffice"),
    @NamedQuery(
            name = "MonthlyStockSupplyReq.FindByCoStock",
            query = "SELECT MSSR FROM MonthlyStockSupplyReq MSSR WHERE MSSR.countryOffice = :co and MSSR.stock=:stock and MSSR.year*12+(MSSR.month+1)<=:y*12+(:m+1) and MSSR.approved=TRUE and MSSR.year*12+(MSSR.month+1)>=:ny*12+(:nm+1)")
})
public class MonthlyStockSupplyReq implements Serializable, Comparable<MonthlyStockSupplyReq> {

    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private Stock stock;
    @Id
    @ManyToOne
    private CountryOffice countryOffice;
    @Id
    private Month month;
    @Id
    private Integer year;

    private int qtyForecasted = 0;
    private int plannedInventory = 0;
    private int qtySold = 0;
    private int actualInventory = 0;
    private int varianceOffset = 0;
    private int qtyRequested = 0;
    
    private MssrStatus status;
    private boolean endMthUpdated = false;
    private boolean varianceUpdated = false;
    private boolean approved = false;

    @OneToMany(mappedBy = "monthlyStockSupplyReq")
    private List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;

    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
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

    public int getQtyForecasted() {
        return qtyForecasted;
    }

    public void setQtyForecasted(int qtyForecasted) {
        this.qtyForecasted = qtyForecasted;
    }

    public int getPlannedInventory() {
        return plannedInventory;
    }

    public void setPlannedInventory(int plannedInventory) {
        this.plannedInventory = plannedInventory;
    }

    public int getQtySold() {
        return qtySold;
    }

    public void setQtySold(int qtySold) {
        this.qtySold = qtySold;
    }

    public int getActualInventory() {
        return actualInventory;
    }

    public void setActualInventory(int actualInventory) {
        this.actualInventory = actualInventory;
    }

    public int getVarianceOffset() {
        return varianceOffset;
    }

    public void setVarianceOffset(int varianceOffset) {
        this.varianceOffset = varianceOffset;
    }

    public int getQtyRequested() {
        return qtyRequested;
    }

    public void setQtyRequested(int qtyRequested) {
        this.qtyRequested = qtyRequested;
    }

    public MssrStatus getStatus() {
        return status;
    }

    public void setStatus(MssrStatus status) {
        this.status = status;
    }

    public boolean isEndMthUpdated() {
        return endMthUpdated;
    }

    public void setEndMthUpdated(boolean endMthUpdated) {
        this.endMthUpdated = endMthUpdated;
    }

    public boolean isVarianceUpdated() {
        return varianceUpdated;
    }

    public void setVarianceUpdated(boolean varianceUpdated) {
        this.varianceUpdated = varianceUpdated;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public List<GoodsIssuedDocumentDetail> getGoodsIssuedDocumentDetails() {
        return goodsIssuedDocumentDetails;
    }

    public void setGoodsIssuedDocumentDetails(List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails) {
        this.goodsIssuedDocumentDetails = goodsIssuedDocumentDetails;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.stock);
        hash = 97 * hash + Objects.hashCode(this.countryOffice);
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyStockSupplyReq)) {
            return false;
        }
        MonthlyStockSupplyReq other = (MonthlyStockSupplyReq) object;
        return this.stock.equals(other.stock) && this.countryOffice.equals(other.countryOffice) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public int compareTo(MonthlyStockSupplyReq other) {
        if (this.countryOffice.getId() > other.countryOffice.getId()) {
            return 1;
        }
        if (this.countryOffice.getId() < other.countryOffice.getId()) {
            return -1;
        }
        if (this.year * 12 + this.month.value > other.year * 12 + other.month.value) {
            return 1;
        }
        if (this.year * 12 + this.month.value < other.year * 12 + other.month.value) {
            return -1;
        }
        if (this.stock.getId() > other.stock.getId()) {
            return 1;
        }
        if (this.stock.getId() < other.stock.getId()) {
            return -1;
        }
        return 0;

    }

    @Override
    public String toString() {
        return "MonthlyStockSupplyReq[ id=" + stock + ", " + countryOffice + ", " + month + ", " + year + " ]";
    }

    // Extra Methods
    public static List<Couple<String, String>> getLabels() {
        List<Couple<String, String>> labels = new ArrayList();
        labels.add(new Couple("qtyForecasted", "Quantity Forecasted"));
        labels.add(new Couple("plannedInventory", "Planned Inventory"));
        labels.add(new Couple("qtySold","Quantity Sold"));
        labels.add(new Couple("actualInventory", "Actual Inventory"));
        labels.add(new Couple("varianceOffset", "Variance Offset"));
        labels.add(new Couple("qtyRequested", "Quantity Requested"));
        labels.add(new Couple("status", "Status"));

        return labels;
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }

}
