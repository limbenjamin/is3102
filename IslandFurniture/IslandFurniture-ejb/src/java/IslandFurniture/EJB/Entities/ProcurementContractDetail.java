/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getStockList",
            query = "SELECT a.procuredStock FROM ProcurementContractDetail a WHERE a.procurementContract.supplier = :supplier AND a.supplierFor = :mf"),
    @NamedQuery(
            name = "getProcurementContractDetailByStockMFAndSupplier",
            query = "SELECT a FROM ProcurementContractDetail a WHERE a.procuredStock = :stock AND a.supplierFor = :mf AND a.procurementContract.supplier = :supplier")
})
public class ProcurementContractDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer leadTimeInDays;
    private Integer lotSize;
    @ManyToOne
    private ProcurementContract procurementContract;
    @ManyToOne
    private ProcuredStock procuredStock;
    @OneToOne
    private ManufacturingFacility supplierFor;

    public Integer getLeadTimeInDays() {
        return leadTimeInDays;
    }

    public void setLeadTimeInDays(Integer leadTimeInDays) {
        this.leadTimeInDays = leadTimeInDays;
    }

    public Integer getLotSize() {
        return lotSize;
    }

    public void setLotSize(Integer lotSize) {
        this.lotSize = lotSize;
    }

    public ProcurementContractDetail() {
        
    }
    
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
