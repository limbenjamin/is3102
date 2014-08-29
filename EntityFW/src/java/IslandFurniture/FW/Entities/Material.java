/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author asus
 */
@Entity
public class Material extends ProcuredStock implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy="material")
    private List<WeeklyMRPRecord> weeklyMRPRecords;

    public List<WeeklyMRPRecord> getWeeklyMRPRecords() {
        return weeklyMRPRecords;
    }

    public void setWeeklyMRPRecords(List<WeeklyMRPRecord> weeklyMRPRecords) {
        this.weeklyMRPRecords = weeklyMRPRecords;
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
        if (!(object instanceof Material)) {
            return false;
        }
        Material other = (Material) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.Material[ id=" + id + " ]";
    }
    
}
