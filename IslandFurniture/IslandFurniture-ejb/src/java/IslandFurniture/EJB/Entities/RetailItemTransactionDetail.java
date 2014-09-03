/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 *
 * @author James
 */
@Entity
public class RetailItemTransactionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="TRANSACTION_ID", referencedColumnName="ID", insertable=false, updatable=false),
        @JoinColumn(name="STORE_ID", referencedColumnName="STORE_ID", insertable=false, updatable=false)
    })
    private RetailItemTransaction retailItemTransaction;
    @ManyToOne
    private RetailItem retailItem;
    @ManyToOne
    private PromotionDetail promotionDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RetailItemTransaction getRetailItemTransaction() {
        return retailItemTransaction;
    }

    public void setRetailItemTransaction(RetailItemTransaction retailItemTransaction) {
        this.retailItemTransaction = retailItemTransaction;
    }

    public RetailItem getRetailItem() {
        return retailItem;
    }

    public void setRetailItem(RetailItem retailItem) {
        this.retailItem = retailItem;
    }

    public PromotionDetail getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(PromotionDetail promotionDetail) {
        this.promotionDetail = promotionDetail;
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
        if (!(object instanceof RetailItemTransactionDetail)) {
            return false;
        }
        RetailItemTransactionDetail other = (RetailItemTransactionDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.RetailItemTransactionDetail[ id=" + id + " ]";
    }

}
