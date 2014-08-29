/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities.Keys;

import IslandFurniture.FW.Enums.Month;
import IslandFurniture.FW.Entities.Material;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
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
