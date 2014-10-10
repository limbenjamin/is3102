/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class IngredientSupplier extends Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(mappedBy = "ingredSupplier")
    private IngredientContract ingredContract;

    @OneToMany(mappedBy = "ingredSupplier")
    private List<IngredientPurchaseOrder> ingredPurchaseOrders;

    public IngredientContract getIngredContract() {
        return ingredContract;
    }

    public void setIngredContract(IngredientContract ingredContract) {
        this.ingredContract = ingredContract;
    }

    public List<IngredientPurchaseOrder> getIngredPurchaseOrders() {
        return ingredPurchaseOrders;
    }

    public void setIngredPurchaseOrders(List<IngredientPurchaseOrder> ingredPurchaseOrders) {
        this.ingredPurchaseOrders = ingredPurchaseOrders;
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
        if (!(object instanceof IngredientSupplier)) {
            return false;
        }
        IngredientSupplier other = (IngredientSupplier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IngredientSupplier[ id=" + id + " ]";
    }

}
