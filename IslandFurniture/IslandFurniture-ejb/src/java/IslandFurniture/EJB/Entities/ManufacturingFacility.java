/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class ManufacturingFacility extends Plant implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToOne(mappedBy="supplierFor")
    private ProcurementContractDetail suppliedBy;
    @ManyToMany
    private List<Stock> produces;
    @OneToMany(mappedBy="manufacturingFacility")
    private List<StockSupplied> supplyingWhatTo;
    @OneToMany(mappedBy="manufacturingFacility")
    private List<ProductionCapacity> productionCapacities;

    public ProcurementContractDetail getSuppliedBy() {
        return suppliedBy;
    }

    public void setSuppliedBy(ProcurementContractDetail suppliedBy) {
        this.suppliedBy = suppliedBy;
    }

    public List<Stock> getProduces() {
        return produces;
    }

    public void setProduces(List<Stock> produces) {
        this.produces = produces;
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
        return "FW.IslandFurniture.Entities.MANUFACTURING.ManufacturingFacility[ id=" + id + " ]";
    }
    
}
