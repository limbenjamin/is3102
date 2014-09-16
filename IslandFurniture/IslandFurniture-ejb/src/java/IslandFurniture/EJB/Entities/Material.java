/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "findMaterialByName",
            query = "SELECT a FROM Material a WHERE a.name = :name"),
    @NamedQuery(name = "Material.getMaterialList",
            query = "SELECT m FROM Material m")
})
public class Material extends ProcuredStock implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double materialWeight;
    @OneToMany(mappedBy = "material")
    private List<WeeklyMRPRecord> weeklyMRPRecords;
    
    public Double getMaterialWeight() {
        return materialWeight;
    }

    public void setMaterialWeight(Double materialWeight) {
        this.materialWeight = materialWeight;
    }
    public Material() {
        System.out.println("Material: create");
    }

    public Material(String name) {
        this.name = name;
    }

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
        return "Material[ id=" + id + " ]";
    }

    // Entity Callbacks

    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
