/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.STORE;

import FW.IslandFurniture.Entities.CountryOffice.PlantStockInventory;
import FW.IslandFurniture.Entities.MANUFACTURING.MonthlyStockSupplyReq;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Stock implements Serializable {
    protected static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @OneToMany(mappedBy="stock")
    protected List<StockUnit> stockUnit;
    
    @OneToMany(mappedBy="stock")
    protected List<PlantStockInventory> planStockInventories;
    
    @OneToMany(mappedBy="stock")
    protected List<MonthlyStockSupplyReq> monthlyStockSupplyReqs;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<StockUnit> getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(List<StockUnit> stockUnit) {
        this.stockUnit = stockUnit;
    }

    public List<PlantStockInventory> getPlanStockInventories() {
        return planStockInventories;
    }

    public void setPlanStockInventories(List<PlantStockInventory> planStockInventories) {
        this.planStockInventories = planStockInventories;
    }

    public List<MonthlyStockSupplyReq> getMonthlyStockSupplyReqs() {
        return monthlyStockSupplyReqs;
    }

    public void setMonthlyStockSupplyReqs(List<MonthlyStockSupplyReq> monthlyStockSupplyReqs) {
        this.monthlyStockSupplyReqs = monthlyStockSupplyReqs;
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
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.Stock[ id=" + id + " ]";
    }
    
}
