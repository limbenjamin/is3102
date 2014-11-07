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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getSupplierList",
            query = "SELECT a.supplier FROM ProcuredStockContract a WHERE EXISTS "
                    + "(SELECT p FROM ProcuredStockContractDetail p WHERE p.procuredStockContract.id = a.id AND p.supplierFor = :mf)")
        ,    @NamedQuery(
            name = "getSupplierForMaterial",
            query = "SELECT a.supplier FROM ProcuredStockContract a WHERE EXISTS (SELECT p FROM ProcuredStockContractDetail p WHERE p.procuredStockContract.id = a.id AND  p.procuredStock=:mat AND p.supplierFor = :mf )"),
        @NamedQuery(
        name="ProcuredStockContract.getSupplierForMFAndMaterial",
                query="select pc from ProcuredStockContract pc where exists(select pcd from ProcuredStockContractDetail pcd where pcd.procuredStockContract.id=pc.id and pcd.supplierFor=:mf and pcd.procuredStock=:ma)"
        )
})
public class ProcuredStockContract implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private ProcuredStockSupplier supplier;
    @OneToMany(mappedBy="procuredStockContract", cascade={CascadeType.PERSIST}, fetch=FetchType.EAGER)
    private List<ProcuredStockContractDetail> procuredStockContractDetails = new ArrayList();
    
    public ProcuredStockContract() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcuredStockSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ProcuredStockSupplier supplier) {
        this.supplier = supplier;
    }

    public List<ProcuredStockContractDetail> getProcuredStockContractDetails() {
        return procuredStockContractDetails;
    }

    public void setProcuredStockContractDetails(List<ProcuredStockContractDetail> procuredStockContractDetails) {
        this.procuredStockContractDetails = procuredStockContractDetails;
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
        if (!(object instanceof ProcuredStockContract)) {
            return false;
        }
        ProcuredStockContract other = (ProcuredStockContract) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProcuredStockContract[ id=" + id + " ]";
    }
    
}
