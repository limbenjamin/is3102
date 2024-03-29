/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
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
@NamedQueries({
    @NamedQuery(
            name = "getAllMFs",
            query = "SELECT a FROM ManufacturingFacility a ORDER BY a.country.name, a.name ASC")
})
public class ManufacturingFacility extends Plant implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private CountryOffice countryOffice;
    @OneToMany(mappedBy = "supplierFor")
    private List<ProcuredStockContractDetail> suppliedBy;
    @OneToMany(mappedBy = "manufacturingFacility")
    private List<StockSupplied> supplyingWhatTo;
    @OneToMany(mappedBy = "manufacturingFacility")
    private List<ProductionCapacity> productionCapacities;
    @OneToMany(mappedBy = "manufacturingFacility")
    private List<MonthlyProcurementPlan> monthlyProcurementPlan;
    
    @OneToMany(mappedBy = "manufacturingFacility")
    private List<WeeklyMRPRecord> weeklyMRPRecord=new ArrayList<>();
    
    
    @OneToMany(mappedBy = "mf")
    private List<ProductionOrder> prodOrders;

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public List<ProcuredStockContractDetail> getSuppliedBy() {
        return suppliedBy;
    }

    public void setSuppliedBy(List<ProcuredStockContractDetail> suppliedBy) {
        this.suppliedBy = suppliedBy;
    }

    public List<StockSupplied> getSupplyingWhatTo() {
        return supplyingWhatTo;
    }

    public void setSupplyingWhatTo(List<StockSupplied> supplyingWhatTo) {
        this.supplyingWhatTo = supplyingWhatTo;
    }

    public List<ProductionCapacity> getProductionCapacities() {
        return productionCapacities;
    }

    public void setProductionCapacities(List<ProductionCapacity> productionCapacities) {
        this.productionCapacities = productionCapacities;
    }

    public List<MonthlyProcurementPlan> getMonthlyProcurementPlan() {
        return monthlyProcurementPlan;
    }

    public void setMonthlyProcurementPlan(List<MonthlyProcurementPlan> monthlyProcurementPlan) {
        this.monthlyProcurementPlan = monthlyProcurementPlan;
    }

    public List<ProductionOrder> getProdOrders() {
        return prodOrders;
    }

    public void setProdOrders(List<ProductionOrder> prodOrders) {
        this.prodOrders = prodOrders;
    }

    public List<WeeklyMRPRecord> getWeeklyMRPRecord() {
        return weeklyMRPRecord;
    }

    public void setWeeklyMRPRecord(List<WeeklyMRPRecord> weeklyMRPRecord) {
        this.weeklyMRPRecord = weeklyMRPRecord;
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
        if (!(object instanceof ManufacturingFacility)) {
            return false;
        }
        ManufacturingFacility other = (ManufacturingFacility) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ManufacturingFacility[ id=" + id + " ]";
    }

    // Extra Methods
    public ProductionCapacity findProductionCapacity(FurnitureModel furnitureModel) {
        for (ProductionCapacity eachProdCap : this.productionCapacities) {
            if (eachProdCap.getFurnitureModel().equals(furnitureModel)) {
                return eachProdCap;
            }
        }

        return null;
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
