/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities.Keys;

import IslandFurniture.FW.Entities.ProcuredStock;
import IslandFurniture.FW.Entities.Supplier;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class LeadTimePK implements Serializable {
    private ProcuredStock procuredStock;
    private Supplier supplier;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LeadTimePK)) {
            return false;
        }
        LeadTimePK other = (LeadTimePK) object;
        return this.procuredStock.equals(other.procuredStock) && this.supplier.equals(other.supplier);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.procuredStock);
        hash = 97 * hash + Objects.hashCode(this.supplier);
        return hash;
    }
    
    @Override
    public String toString() {
        return this.procuredStock.getId() + ", " + this.supplier.getId();
    }
}
