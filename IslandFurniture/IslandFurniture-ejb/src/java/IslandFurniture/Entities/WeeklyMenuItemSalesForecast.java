/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getWmsfByStoreMi",
            query = "SELECT a FROM WeeklyMenuItemSalesForecast a WHERE a.mmsf.store = :store AND a.mmsf.menuItem = :mi AND a.mmsf.year = :year AND a.mmsf.month = :month")
})
public class WeeklyMenuItemSalesForecast implements Serializable, Comparable<WeeklyMenuItemSalesForecast> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer weekNo;
    private Integer qty;
    private Boolean locked;
    
    @ManyToOne
    private MonthlyMenuItemSalesForecast mmsf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(Integer weekNo) {
        this.weekNo = weekNo;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public MonthlyMenuItemSalesForecast getMmsf() {
        return mmsf;
    }

    public void setMmsf(MonthlyMenuItemSalesForecast mmsf) {
        this.mmsf = mmsf;
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
        if (!(object instanceof WeeklyMenuItemSalesForecast)) {
            return false;
        }
        WeeklyMenuItemSalesForecast other = (WeeklyMenuItemSalesForecast) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(WeeklyMenuItemSalesForecast other) {
        if(this.weekNo < other.weekNo) return -1;
        if(this.weekNo > other.weekNo) return 1;
        
        return 0;
    }
    
    @Override
    public String toString() {
        return "WeeklyMenuItemSalesForecast[ id=" + id + " ]";
    }

    
}
