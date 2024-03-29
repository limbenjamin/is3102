/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getAllProcuredStock",
            query = "SELECT m FROM ProcuredStock m")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ProcuredStock extends Stock implements Serializable {

    protected static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "procuredStock")
    protected List<ProcuredStockContractDetail> procuredStockContractDetails;

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
        if (!(object instanceof ProcuredStock)) {
            return false;
        }
        ProcuredStock other = (ProcuredStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Procuredstock[ id=" + id + " ]";
    }

}
