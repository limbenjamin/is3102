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
public class StorefrontInventoryPK implements Serializable {

    private Long store;
    private Long stock;

    public StorefrontInventoryPK(long store, long stock) {
        this.store = store;
        this.stock = stock;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StorefrontInventoryPK)) {
            return false;
        }
        StorefrontInventoryPK other = (StorefrontInventoryPK) object;
        return this.stock.equals(other.stock) && this.store.equals(other.store);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.store);
        hash = 47 * hash + Objects.hashCode(this.stock);
        return hash;
    }

    @Override
    public String toString() {
        return stock + "," + store;
    }
}
