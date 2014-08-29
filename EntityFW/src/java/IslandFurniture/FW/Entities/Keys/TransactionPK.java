/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities.Keys;

import IslandFurniture.FW.Entities.Store;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author asus
 */
public class TransactionPK implements Serializable {
    private Long id;
    private Store store;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TransactionPK)) {
            return false;
        }
        TransactionPK other = (TransactionPK) object;
        return this.id.equals(other.id) && this.store.equals(other.store);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.store);
        return hash;
    }
    
    @Override
    public String toString() {
        return this.id + "," + this.store.getId();
    }
}
