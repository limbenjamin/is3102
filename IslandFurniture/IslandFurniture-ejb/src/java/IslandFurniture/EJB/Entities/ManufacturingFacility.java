/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.Query;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="getAllMFs",
        query="SELECT a FROM ManufacturingFacility a")
})
public class ManufacturingFacility extends Plant implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    private CountryOffice countryOffice;
    @OneToOne(mappedBy="supplierFor")
    private ProcurementContractDetail suppliedBy;
    @OneToMany(mappedBy="manufacturingFacility")
    private List<StockSupplied> supplyingWhatTo;
    @OneToMany(mappedBy="manufacturingFacility")
    private List<ProductionCapacity> productionCapacities;

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public ProcurementContractDetail getSuppliedBy() {
        return suppliedBy;
    }

    public void setSuppliedBy(ProcurementContractDetail suppliedBy) {
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
    public ProductionCapacity findProductionCapacity(FurnitureModel furnitureModel){
        for(ProductionCapacity eachProdCap: this.productionCapacities){
            if(eachProdCap.getFurnitureModel().equals(furnitureModel)){
                return eachProdCap;
            }
        }
        
        return null;
    }
    
        public double getCurrentFreeCapacity(EntityManager em,Month m,int year)
    {
        
        Query q=em.createNamedQuery("MonthlyProductionPlan.FindAllInPeriod");
        q.setParameter("m",m);
        q.setParameter("y",year);
        double cCap=0;
    for(Object o : q.getResultList())
    {
        MonthlyProductionPlan mpp=(MonthlyProductionPlan) o;
        cCap+=mpp.getQTY()/(0.0+this.findProductionCapacity(mpp.getFurnitureModel()).getCapacity(m, year));
    }
    
    return 1-cCap;
    
    }
    
    
    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
