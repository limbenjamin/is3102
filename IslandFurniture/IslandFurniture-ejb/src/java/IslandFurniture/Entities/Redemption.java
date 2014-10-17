/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class Redemption extends Document implements Serializable {

    private static final long serialVersionUID = 1L;
    private Boolean claimed;

    @ManyToOne
    private Customer customer;

    @OneToOne
    private RedeemableItem redeemableItem;

    public Boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(Boolean claimed) {
        this.claimed = claimed;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RedeemableItem getRedeemableItem() {
        return redeemableItem;
    }

    public void setRedeemableItem(RedeemableItem redeemableItem) {
        this.redeemableItem = redeemableItem;
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
        if (!(object instanceof Redemption)) {
            return false;
        }
        Redemption other = (Redemption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Redemption[ id=" + id + " ]";
    }

}
