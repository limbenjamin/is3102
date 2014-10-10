/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
public class IngredientPurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "ingredPurchaseOrder")
    private List<IngredientPurchaseOrderDetail> ingredPurchaseOrderDetails;

    @ManyToOne
    private IngredientSupplier ingredSupplier;

    @ManyToOne
    private Store store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

}
