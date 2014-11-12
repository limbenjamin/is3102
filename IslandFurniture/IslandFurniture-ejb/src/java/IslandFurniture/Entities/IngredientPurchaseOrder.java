/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getIngredientSuppliersPO",
            query = "SELECT DISTINCT a.ingredSupplier FROM IngredientPurchaseOrder a")
})
public class IngredientPurchaseOrder extends PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "ingredPurchaseOrder", cascade = {CascadeType.ALL})
    private List<IngredientPurchaseOrderDetail> ingredPurchaseOrderDetails;

    @ManyToOne
    private IngredientSupplier ingredSupplier;

    @ManyToOne
    private Store store;
    
    @OneToOne
    private IngredientGoodsReceiptDocument ingredGoodsReceiptDoc;

    public List<IngredientPurchaseOrderDetail> getIngredPurchaseOrderDetails() {
        return ingredPurchaseOrderDetails;
    }

    public void setIngredPurchaseOrderDetails(List<IngredientPurchaseOrderDetail> ingredPurchaseOrderDetails) {
        this.ingredPurchaseOrderDetails = ingredPurchaseOrderDetails;
    }

    public IngredientSupplier getIngredSupplier() {
        return ingredSupplier;
    }

    public void setIngredSupplier(IngredientSupplier ingredSupplier) {
        this.ingredSupplier = ingredSupplier;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public IngredientGoodsReceiptDocument getIngredGoodsReceiptDoc() {
        return ingredGoodsReceiptDoc;
    }

    public void setIngredGoodsReceiptDoc(IngredientGoodsReceiptDocument ingredGoodsReceiptDoc) {
        this.ingredGoodsReceiptDoc = ingredGoodsReceiptDoc;
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
        if (!(object instanceof IngredientPurchaseOrder)) {
            return false;
        }
        IngredientPurchaseOrder other = (IngredientPurchaseOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IngredientPurchaseOrder[ id=" + id + " ]";
    }
    
    // Extra Methods
    public boolean hasIngredient(Ingredient ingredient) {
        for (IngredientPurchaseOrderDetail pod : ingredPurchaseOrderDetails) {
            if (pod.getIngredient().equals(ingredient)) {
                return true;
            }
        }
        return false;
    }    

}
