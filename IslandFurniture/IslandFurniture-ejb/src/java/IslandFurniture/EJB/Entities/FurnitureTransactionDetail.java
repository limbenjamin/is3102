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
import javax.persistence.PostPersist;

/**
 *
 * @author James
 */
@Entity
public class FurnitureTransactionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer qty;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "TRANSACTION_ID", referencedColumnName = "ID"),
        @JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID")
    })
    private FurnitureTransaction furnitureTransaction;
    @ManyToOne
    private FurnitureModel furnitureModel;
    @ManyToOne
    private PromotionDetail promotionDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public FurnitureTransaction getFurnitureTransaction() {
        return furnitureTransaction;
    }

    public void setFurnitureTransaction(FurnitureTransaction furnitureTransaction) {
        this.furnitureTransaction = furnitureTransaction;
    }

    public FurnitureModel getFurnitureModel() {
        return furnitureModel;
    }

    public void setFurnitureModel(FurnitureModel furnitureModel) {
        this.furnitureModel = furnitureModel;
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
        if (!(object instanceof FurnitureTransactionDetail)) {
            return false;
        }
        FurnitureTransactionDetail other = (FurnitureTransactionDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FurnitureTransactionDetail[ id=" + id + " ]";
    }

    // Entity Callbacks
    
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
