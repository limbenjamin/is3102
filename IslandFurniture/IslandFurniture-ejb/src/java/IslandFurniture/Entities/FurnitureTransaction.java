/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;

/**
 *
 * @author James
 */
@Entity
public class FurnitureTransaction extends Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy = "furnitureTransaction", cascade={CascadeType.ALL})
    private List<FurnitureTransactionDetail> furnitureTransactionDetails;

    public List<FurnitureTransactionDetail> getFurnitureTransactionDetails() {
        return furnitureTransactionDetails;
    }

    public void setFurnitureTransactionDetails(List<FurnitureTransactionDetail> furnitureTransactionDetails) {
        this.furnitureTransactionDetails = furnitureTransactionDetails;
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
        if (!(object instanceof FurnitureTransaction)) {
            return false;
        }
        FurnitureTransaction other = (FurnitureTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FurnitureTransaction[ id=" + this.id + ", " + this.store + " ]";
    }

    // Entity Callbacks
    
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}