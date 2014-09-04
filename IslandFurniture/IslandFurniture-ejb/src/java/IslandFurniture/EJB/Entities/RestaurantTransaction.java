/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
public class RestaurantTransaction extends Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy = "restaurantTransaction")
    private List<RestaurantTransactionDetail> restaurantTransactionDetails;

    public List<RestaurantTransactionDetail> getRestaurantTransactionDetails() {
        return restaurantTransactionDetails;
    }

    public void setRestaurantTransactionDetails(List<RestaurantTransactionDetail> restaurantTransactionDetails) {
        this.restaurantTransactionDetails = restaurantTransactionDetails;
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
        if (!(object instanceof RestaurantTransaction)) {
            return false;
        }
        RestaurantTransaction other = (RestaurantTransaction) object;
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