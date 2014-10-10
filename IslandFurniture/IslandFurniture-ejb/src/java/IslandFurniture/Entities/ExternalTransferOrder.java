/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class ExternalTransferOrder extends TransferOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    private Plant fulfillingPlant;
    
    @OneToMany(mappedBy = "extTransOrder")
    private List<ExternalTransferOrderDetail> extTransOrderDetails;

    public Plant getFulfillingPlant() {
        return fulfillingPlant;
    }

    public void setFulfillingPlant(Plant fulfillingPlant) {
        this.fulfillingPlant = fulfillingPlant;
    }

    public List<ExternalTransferOrderDetail> getExtTransOrderDetails() {
        return extTransOrderDetails;
    }

    public void setExtTransOrderDetails(List<ExternalTransferOrderDetail> extTransOrderDetails) {
        this.extTransOrderDetails = extTransOrderDetails;
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
        if (!(object instanceof ExternalTransferOrder)) {
            return false;
        }
        ExternalTransferOrder other = (ExternalTransferOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ExternalTransferOrder[ id=" + id + " ]";
    }
    
}
