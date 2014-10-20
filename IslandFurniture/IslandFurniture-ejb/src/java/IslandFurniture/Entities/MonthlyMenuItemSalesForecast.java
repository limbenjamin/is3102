/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.MmsfStatus;
import IslandFurniture.Enums.Month;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
@IdClass(MonthlyMenuItemSalesForecastPK.class)
public class MonthlyMenuItemSalesForecast implements Serializable {

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

}
