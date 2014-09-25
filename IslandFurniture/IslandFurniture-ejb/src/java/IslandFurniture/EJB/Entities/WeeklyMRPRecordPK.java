/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import IslandFurniture.EJB.Entities.Month;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class WeeklyMRPRecordPK implements Serializable {
    private Long material;
    private Integer week;
    private Month month;
    private Integer year;
    private Long manufacturingFacility;
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WeeklyMRPRecordPK)) {
            return false;
        }
        WeeklyMRPRecordPK other = (WeeklyMRPRecordPK) object;
        return this.material.equals(other.material) && this.week.equals(other.week) && this.month.equals(other.month) && this.year.equals(other.year) && this.manufacturingFacility.equals((other.manufacturingFacility));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.material);
        hash = 97 * hash + Objects.hashCode(this.week);
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        hash = 97 * hash + Objects.hashCode(this.manufacturingFacility);
        return hash;
    }
    
    @Override
    public String toString() {
        return material + "," + week + "," + month + "," + year;
    }
}
