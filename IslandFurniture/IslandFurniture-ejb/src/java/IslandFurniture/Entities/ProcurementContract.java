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
            query = "SELECT a.supplier FROM ProcurementContract a WHERE EXISTS "
                    + "(SELECT p FROM ProcurementContractDetail p WHERE p.procurementContract.id = a.id AND p.supplierFor = :mf)")
        ,    @NamedQuery(
            name = "getSupplierForMaterial",
            query = "SELECT a.supplier FROM ProcurementContract a WHERE EXISTS (SELECT p FROM ProcurementContractDetail p WHERE p.procurementContract.id = a.id AND  p.procuredStock=:mat AND p.supplierFor = :mf )"),
        @NamedQuery(
        name="ProcurementContract.getSupplierForMFAndMaterial",
                query="select pc from ProcurementContract pc where exists(select pcd from ProcurementContractDetail pcd where pcd.procurementContract.id=pc.id and pcd.supplierFor=:mf and pcd.procuredStock=:ma)"
        )

        
        
})
public class ProcurementContract implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Supplier supplier;
    @OneToMany(mappedBy="procurementContract", cascade={CascadeType.PERSIST})
    private List<ProcurementContractDetail> procurementContractDetails = new ArrayList();
    
    public ProcurementContract() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public List<ProcurementContractDetail> getProcurementContractDetails() {
        return procurementContractDetails;
    }

    public void setProcurementContractDetails(List<ProcurementContractDetail> procurementContractDetails) {
        this.procurementContractDetails = procurementContractDetails;
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
        if (!(object instanceof ProcurementContract)) {
            return false;
        }
        ProcurementContract other = (ProcurementContract) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.GLOBALHQ.ProcurementContract[ id=" + id + " ]";
    }
    
}
