/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author James
 */
@Entity
public class IngredientGoodsReceiptDocument extends Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.DATE)
    private Calendar postingDate;
    @Temporal(TemporalType.DATE)
    private Calendar receiptDate;
    private String deliveryNote;
    private Boolean posted;

    @ManyToOne
    private Store store;

    @OneToMany(mappedBy = "ingredGoodsReceiptDocument")
    private List<IngredientGoodsReceiptDocumentDetail> ingredGoodsReceiptDocumentDetails;

    @OneToOne(mappedBy = "ingredGoodsReceiptDoc")
    private IngredientPurchaseOrder ingredientPurchaseOrder;

    public Calendar getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Calendar postingDate) {
        this.postingDate = postingDate;
    }

    public Calendar getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Calendar receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Boolean isPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<IngredientGoodsReceiptDocumentDetail> getIngredGoodsReceiptDocumentDetails() {
        return ingredGoodsReceiptDocumentDetails;
    }

    public void setIngredGoodsReceiptDocumentDetails(List<IngredientGoodsReceiptDocumentDetail> ingredGoodsReceiptDocumentDetails) {
        this.ingredGoodsReceiptDocumentDetails = ingredGoodsReceiptDocumentDetails;
    }

    public IngredientPurchaseOrder getIngredientPurchaseOrder() {
        return ingredientPurchaseOrder;
    }

    public void setIngredientPurchaseOrder(IngredientPurchaseOrder ingredientPurchaseOrder) {
        this.ingredientPurchaseOrder = ingredientPurchaseOrder;
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
        if (!(object instanceof IngredientGoodsReceiptDocument)) {
            return false;
        }
        IngredientGoodsReceiptDocument other = (IngredientGoodsReceiptDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IngredientGoodsReceiptDocument[ id=" + id + " ]";
    }

}
