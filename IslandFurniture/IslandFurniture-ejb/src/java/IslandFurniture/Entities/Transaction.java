/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author James
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(
            // Fetches all Transactions from specified store falling between startDate and endDate
            name = "getStoreTransactions",
            query = "SELECT a FROM Transaction a WHERE a.transTime >= :startDate AND a.transTime <= :endDate AND a.store = :store")
})
public abstract class Transaction extends Document implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    protected Store store;
    @Temporal(TemporalType.TIMESTAMP)
    protected Calendar transTime;
    protected Double grandTotal;
    protected Double voucherTotal;

    @ManyToOne
    protected Customer member;


    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Calendar getTransTime() {
        return transTime;
    }

    public void setTransTime(Calendar transTime) {
        this.transTime = transTime;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getVoucherTotal() {
        return voucherTotal;
    }

    public void setVoucherTotal(Double voucherTotal) {
        this.voucherTotal = voucherTotal;
    }

    public Customer getMember() {
        return member;
    }

    public void setMember(Customer member) {
        this.member = member;
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
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transaction[ id=" + this.getStore() + ", " + id + " ]";
    }

}
