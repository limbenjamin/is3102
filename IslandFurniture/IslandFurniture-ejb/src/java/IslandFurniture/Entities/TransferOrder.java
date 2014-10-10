/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import IslandFurniture.Enums.TransferOrderStatus;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TransferOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected TransferOrderStatus status;
    
    @ManyToOne
    protected Plant requestingPlant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plant getRequestingPlant() {
        return requestingPlant;
    }

    public void setRequestingPlant(Plant requestingPlant) {
        this.requestingPlant = requestingPlant;
    }

    public TransferOrderStatus getStatus() {
        return status;
    }

    public void setStatus(TransferOrderStatus status) {
        this.status = status;
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
        if (!(object instanceof TransferOrder)) {
            return false;
        }
        TransferOrder other = (TransferOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TransferOrder[ id=" + id + " ]";
    }
    
}
