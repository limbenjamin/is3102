/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.DataStructures.Couple;
import IslandFurniture.Enums.MmsfStatus;
import IslandFurniture.Enums.Month;
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

/**
 *
 * @author James
 */
@Entity
@IdClass(MonthlyMenuItemSalesForecastPK.class)
@NamedQueries({
    @NamedQuery(
            name = "getMmsfByStore",
            query = "SELECT a FROM MonthlyMenuItemSalesForecast a WHERE a.store=:store"),
    @NamedQuery(
            name = "getMmsfByStoreMi",
            query = "SELECT a FROM MonthlyMenuItemSalesForecast a WHERE "
            + "a.store = :store AND a.menuItem = :menuItem AND "
            + "a.year*12 + a.month >= :startYr*12 + :startMth AND "
            + "a.year*12 + a.month <= :endYr*12 + :endMth"),
})
public class MonthlyMenuItemSalesForecast implements Serializable, Comparable<MonthlyMenuItemSalesForecast> {

    private static final long serialVersionUID = 1L;
    @Id
    private Month month;
    @Id
    private Integer year;
    @Id
    @ManyToOne
    private Store store;
    @Id
    @ManyToOne
    private MenuItem menuItem;

    private int qtyForecasted = 0;
    private int qtySold = 0;
    private MmsfStatus status;
    private boolean endMthUpdated = false;

    @OneToMany(mappedBy = "mmsf")
    private List<WeeklyMenuItemSalesForecast> wmsfList;

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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQtyForecasted() {
        return qtyForecasted;
    }

    public void setQtyForecasted(int qtyForecasted) {
        this.qtyForecasted = qtyForecasted;
    }

    public int getQtySold() {
        return qtySold;
    }

    public void setQtySold(int qtySold) {
        this.qtySold = qtySold;
    }

    public MmsfStatus getStatus() {
        return status;
    }

    public void setStatus(MmsfStatus status) {
        this.status = status;
    }

    public boolean isEndMthUpdated() {
        return endMthUpdated;
    }

    public void setEndMthUpdated(boolean endMthUpdated) {
        this.endMthUpdated = endMthUpdated;
    }

    public List<WeeklyMenuItemSalesForecast> getWmsfList() {
        return wmsfList;
    }

    public void setWmsfList(List<WeeklyMenuItemSalesForecast> wmsfList) {
        this.wmsfList = wmsfList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        hash = 97 * hash + Objects.hashCode(this.store);
        hash = 97 * hash + Objects.hashCode(this.menuItem);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyMenuItemSalesForecast)) {
            return false;
        }
        MonthlyMenuItemSalesForecast other = (MonthlyMenuItemSalesForecast) object;
        return this.month.equals(other.month) && this.year.equals(other.year) && this.store.equals(other.store) && this.menuItem.equals(other.menuItem);
    }

    @Override
    public String toString() {
        return "MonthlyMenuItemSalesForecast[ id=" + this.month + ", " + this.year + ", " + this.store + this.menuItem + " ]";
    }

    // Implements Comparable
    @Override
    public int compareTo(MonthlyMenuItemSalesForecast other) {
        if (this.store.getId() > other.store.getId()) {
            return 1;
        }
        if (this.store.getId() < other.store.getId()) {
            return -1;
        }
        if (this.year * 12 + this.month.value > other.year * 12 + other.month.value) {
            return 1;
        }
        if (this.year * 12 + this.month.value < other.year * 12 + other.month.value) {
            return -1;
        }
        if (this.menuItem.getId() > other.menuItem.getId()) {
            return 1;
        }
        if (this.menuItem.getId() < other.menuItem.getId()) {
            return -1;
        }
        return 0;

    }

    // Extra Methods
    public static List<Couple<String, String>> getLabels() {
        List<Couple<String, String>> labels = new ArrayList();
        labels.add(new Couple("qtyForecasted", "Quantity Forecasted"));
        labels.add(new Couple("qtySold", "Quantity Sold"));
        labels.add(new Couple("status", "Status"));

        return labels;
    }

}
