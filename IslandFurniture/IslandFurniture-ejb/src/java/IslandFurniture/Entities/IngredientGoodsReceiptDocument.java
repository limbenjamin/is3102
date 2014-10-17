/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
public class IngredientGoodsReceiptDocument extends Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private IngredientSupplier supplier;

    @OneToMany(mappedBy = "ingredGoodsReceiptDocument")
    private List<IngredientGoodsReceiptDocumentDetail> ingredGoodsReceiptDocumentDetails;

    @OneToOne(mappedBy = "ingredGoodsReceiptDoc")
    private IngredientPurchaseOrder ingredientPurchaseOrder;

    public IngredientSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(IngredientSupplier supplier) {
        this.supplier = supplier;
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
