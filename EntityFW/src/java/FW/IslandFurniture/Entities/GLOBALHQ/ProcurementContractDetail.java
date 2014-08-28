/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.GLOBALHQ;

import FW.IslandFurniture.Entities.MANUFACTURING.ManufacturingFacility;
import FW.IslandFurniture.Entities.STORE.ProcuredStock;
import FW.IslandFurniture.Entities.STORE.Supplier;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author asus
 */
@Entity
public class ProcurementContractDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private ProcurementContract procurementContract;
    @OneToOne
    private Supplier supplier;
    @ManyToOne
    private ProcuredStock procuredStock;
    @OneToOne
    private ManufacturingFacility supplierFor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcurementContract getProcurementContract() {
        return procurementContract;
    }

    public void setProcurementContract(ProcurementContract procurementContract) {
        this.procurementContract = procurementContract;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public ProcuredStock getProcuredStock() {
        return procuredStock;
    }

    public void setProcuredStock(ProcuredStock procuredStock) {
        this.procuredStock = procuredStock;
    }

    public ManufacturingFacility getSupplierFor() {
        return supplierFor;
    }

    public void setSupplierFor(ManufacturingFacility supplierFor) {
        this.supplierFor = supplierFor;
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
        if (!(object instanceof ProcurementContractDetail)) {
            return false;
        }
        ProcurementContractDetail other = (ProcurementContractDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.GLOBALHQ.ProcurementContractDetail[ id=" + id + " ]";
    }
    
}