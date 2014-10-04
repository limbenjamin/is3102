/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostPersist;

/**
 *
 * @author James
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "findRetailItemByName",
            query = "SELECT a FROM RetailItem a WHERE a.name = :name"),
    @NamedQuery(
            name = "getAllRetailItems",
            query = "SELECT a FROM RetailItem a")
})
public class RetailItem extends ProcuredStock implements Serializable {

    private static final long serialVersionUID = 1L;
    private Double price;

    public Double getPrice() {
        return price; 
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    public RetailItem() {
       
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
        if (!(object instanceof RetailItem)) {
            return false;
        }
        RetailItem other = (RetailItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RetailItem[ id=" + id + " ]";
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
