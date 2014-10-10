/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getAllStock", query = "SELECT s FROM Stock s")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Stock implements Serializable {

    protected static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Column(unique = true)
    protected String name;

    @OneToMany(mappedBy = "stock")
    protected List<StockUnit> stockUnit = new ArrayList();
    @OneToMany(mappedBy = "stock")
    protected List<PlantStockInventory> plantStockInventories = new ArrayList();
    @OneToMany(mappedBy = "stock")
    protected List<MonthlyStockSupplyReq> monthlyStockSupplyReqs = new ArrayList();
    @OneToMany
    protected List<PriceInCountry> priceInCountry = new ArrayList();
    @ManyToMany(mappedBy = "sells")
    protected List<Store> soldBy = new ArrayList();
    @OneToMany(mappedBy = "stock")
    protected List<NFC> nfcList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StockUnit> getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(List<StockUnit> stockUnit) {
        this.stockUnit = stockUnit;
    }

    public List<PlantStockInventory> getPlantStockInventories() {
        return plantStockInventories;
    }

    public void setPlantStockInventories(List<PlantStockInventory> plantStockInventories) {
        this.plantStockInventories = plantStockInventories;
    }

    public List<MonthlyStockSupplyReq> getMonthlyStockSupplyReqs() {
        return monthlyStockSupplyReqs;
    }

    public void setMonthlyStockSupplyReqs(List<MonthlyStockSupplyReq> monthlyStockSupplyReqs) {
        this.monthlyStockSupplyReqs = monthlyStockSupplyReqs;
    }

    public List<PriceInCountry> getPriceInCountry() {
        return priceInCountry;
    }

    public void setPriceInCountry(List<PriceInCountry> priceInCountry) {
        this.priceInCountry = priceInCountry;
    }

    public List<Store> getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(List<Store> soldBy) {
        this.soldBy = soldBy;
    }

    public List<NFC> getNfcList() {
        return nfcList;
    }

    public void setNfcList(List<NFC> nfcList) {
        this.nfcList = nfcList;
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
        return "Stock[ id=" + id + " ]";
    }

}
