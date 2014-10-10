/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class ReplenishmentTransferOrder extends TransferOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer qty;
    
    @OneToOne
    private Stock stock;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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
        if (!(object instanceof ReplenishmentTransferOrder)) {
            return false;
        }
        ReplenishmentTransferOrder other = (ReplenishmentTransferOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReplenishmentTransferOrder[ id=" + id + " ]";
    }
    
}
