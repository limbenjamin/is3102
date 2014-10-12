/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
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
            query = "SELECT a.procuredStock FROM ProcuredStockContractDetail a WHERE a.procuredStockContract.supplier = :supplier AND a.supplierFor = :mf"),
    @NamedQuery(
            name = "getProcurementContractDetailByStockMFAndSupplier",
            query = "SELECT a FROM ProcuredStockContractDetail a WHERE a.procuredStock = :stock AND a.supplierFor = :mf AND a.procuredStockContract.supplier = :supplier"),
    @NamedQuery(
            name = "getProcurementContractDetailByStockAndMF",
            query = "SELECT a FROM ProcuredStockContractDetail a WHERE a.procuredStock = :stock AND a.supplierFor = :mf"),
    @NamedQuery(
            name = "getProcurementContractDetailByStock",
            query = "SELECT a FROM ProcuredStockContractDetail a WHERE a.procuredStock = :stock")
})
public class ProcuredStockContractDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer leadTimeInDays;
    private Integer lotSize;
    private Double lotPrice;
    @ManyToOne
    private ProcuredStockContract procuredStockContract;
    @ManyToOne
    private ProcuredStock procuredStock;
    @ManyToOne
    private ManufacturingFacility supplierFor;
    @ManyToOne(cascade={CascadeType.PERSIST})
    private Currency currency;

    public ProcuredStockContractDetail() {
        
    }
    
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

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcuredStockContract getProcurementContract() {
        return procuredStockContract;
    }

    public void setProcurementContract(ProcuredStockContract procuredStockContract) {
        this.procuredStockContract = procuredStockContract;
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

    public ProcuredStockContract getProcuredStockContract() {
        return procuredStockContract;
    }

    public void setProcuredStockContract(ProcuredStockContract procuredStockContract) {
        this.procuredStockContract = procuredStockContract;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getLotPrice() {
        return lotPrice;
    }

    public void setLotPrice(Double lotPrice) {
        this.lotPrice = lotPrice;
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
        if (!(object instanceof ProcuredStockContractDetail)) {
            return false;
        }
        ProcuredStockContractDetail other = (ProcuredStockContractDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProcuredStockContractDetail[ id=" + id + " ]";
    }
    
}
