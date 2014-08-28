/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.GLOBALHQ;

import FW.IslandFurniture.Entities.STORE.Plant;
import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author asus
 */
@Entity
public class GlobalHQ extends Plant implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GlobalHQ)) {
            return false;
        }
        GlobalHQ other = (GlobalHQ) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.GLOBALHQ.GlobalHQ[ id=" + id + " ]";
    }
    
}
