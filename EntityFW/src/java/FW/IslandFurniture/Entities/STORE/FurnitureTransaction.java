/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.STORE;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
public class FurnitureTransaction extends Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy="furnitureTransaction")
    private List<FurnitureTransactionDetail> furnitureTransactionDetails;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

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
        return "FW.IslandFurniture.Entities.STORE.RetailItemTransaction[ id=" + id + " ]";
    }
    
}
