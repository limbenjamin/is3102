/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Integer numReturned;
    private Integer numClaimed;
    private Double unitPrice;
    private Integer unitPoints;

    @ManyToOne
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

    public Integer getNumReturned() {
        return numReturned;
    }

    public void setNumReturned(Integer numReturned) {
        this.numReturned = numReturned;
    }

    public Integer getNumClaimed() {
        return numClaimed;
    }

    public void setNumClaimed(Integer numClaimed) {
        this.numClaimed = numClaimed;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getUnitPoints() {
        return unitPoints;
    }

    public void setUnitPoints(Integer unitPoints) {
        this.unitPoints = unitPoints;
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

    // Extra Methods
    public Double getSubtotal() {
        return this.qty * this.unitPrice;
    }

    public Integer getTotalPoints() {
        return this.qty * this.unitPoints;
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
