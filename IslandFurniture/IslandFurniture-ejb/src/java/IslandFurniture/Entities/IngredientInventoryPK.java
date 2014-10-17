/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class IngredientInventoryPK implements Serializable{
    private Long store;
    private Long ingredient;

    public IngredientInventoryPK(Long store, Long ingredient) {
        this.store = store;
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IngredientInventoryPK)) {
            return false;
        }
        IngredientInventoryPK other = (IngredientInventoryPK) object;
        return this.store.equals(other.store) && this.ingredient.equals(other.ingredient);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.store);
        hash = 17 * hash + Objects.hashCode(this.ingredient);
        return hash;
    }

    @Override
    public String toString() {
        return "IngredientInventoryPK{" + "store=" + store + ", ingredient=" + ingredient + '}';
    }
}
