/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;

/**
 *
 * @author James
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "findFurnitureByName",
            query = "SELECT a FROM FurnitureModel a WHERE a.name = :name"),
    @NamedQuery(
            name = "getAllFurnitureModels",
            query = "SELECT a FROM FurnitureModel a")
})
public class FurnitureModel extends Stock implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToOne(cascade={CascadeType.PERSIST})
    private BOM bom;
    @OneToMany(mappedBy = "furnitureModel", cascade={CascadeType.PERSIST})
    protected List<ProductionCapacity> productionCapacity;

    public FurnitureModel() {
    }
    public FurnitureModel(String name) {
        this.name = name;
    }
    public BOM getBom() {
        return bom;
    }

    public void setBom(BOM bom) {
        this.bom = bom;
    }

    public List<ProductionCapacity> getProductionCapacity() {
        return productionCapacity;
    }

    public void setProductionCapacity(List<ProductionCapacity> productionCapacity) {
        this.productionCapacity = productionCapacity;
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
        if (!(object instanceof FurnitureModel)) {
            return false;
        }
        FurnitureModel other = (FurnitureModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FurnitureModel[ id=" + id + " ]";
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }

}
