/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
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
    private List<ProductionCapacity> productionCapacity;
    private Double price;
    private FurnitureCategory category;
    private FurnitureSubcategory subcategory;
    private List colour;

    public FurnitureModel() {
        
        this.category = FurnitureCategory.EMPTY;
        this.subcategory = FurnitureSubcategory.EMPTY;
    }
    public FurnitureModel(String name) {
        this.category = FurnitureCategory.EMPTY;
        this.subcategory = FurnitureSubcategory.EMPTY;
        this.name = name;
    }

    public FurnitureSubcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(FurnitureSubcategory subcategory) {
        this.subcategory = subcategory;
    }

    public FurnitureCategory getCategory() {
        return category;
    }

    public void setCategory(FurnitureCategory category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List getColour() {
        return colour;
    }

    public void setColour(List colour) {
        this.colour = colour;
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
