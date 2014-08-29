/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Fw.EntitiesKeys;

import IslandFurniture.FW.EntitiesEnums.Month;
import IslandFurniture.Fw.Entitites.Material;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author asus
 */
public class WeeklyMRPRecordPK implements Serializable {
    private Material material;
    private Integer week;
    private Month month;
    private Integer year;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WeeklyMRPRecordPK)) {
            return false;
        }
        WeeklyMRPRecordPK other = (WeeklyMRPRecordPK) object;
        return this.material.equals(other.material) && this.week.equals(other.week) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.material);
        hash = 97 * hash + Objects.hashCode(this.week);
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        return hash;
    }
    
    @Override
    public String toString() {
        return this.material.getId() + "," + week + "," + month + "," + year;
    }
}
