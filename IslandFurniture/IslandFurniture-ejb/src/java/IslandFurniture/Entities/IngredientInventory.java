/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(IngredientInventoryPK.class)
public class IngredientInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private Store store;
    @Id
    @ManyToOne
    private Ingredient ingredient;

    private Integer qty;
    private Integer threshold;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.store);
        hash = 17 * hash + Objects.hashCode(this.ingredient);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IngredientInventory)) {
            return false;
        }
        IngredientInventory other = (IngredientInventory) object;
        return this.store.equals(other.store) && this.ingredient.equals(other.ingredient);
    }

    @Override
    public String toString() {
        return "IngredientInventory[ id=" + this.store + ", " + this.ingredient + " ]";
    }
    
}
