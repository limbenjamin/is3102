/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class ReconciliationRecord extends Document implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Double balance;

    @ManyToOne
    private Staff supvr;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Staff getSupvr() {
        return supvr;
    }

    public void setSupvr(Staff supvr) {
        this.supvr = supvr;
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
        if (!(object instanceof ReconciliationRecord)) {
            return false;
        }
        ReconciliationRecord other = (ReconciliationRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReconciliationRecord[ id=" + id + " ]";
    }
    
}
