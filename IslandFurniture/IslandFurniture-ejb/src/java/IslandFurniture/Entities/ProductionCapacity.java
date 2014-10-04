/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.ProductionCapacityPK;
import IslandFurniture.StaticClasses.Helper;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(ProductionCapacityPK.class)
@NamedQueries({
    @NamedQuery(name = "ProductionCapacity.findPC", query = "SELECT PC from ProductionCapacity PC where PC.manufacturingFacility=:MFNAME and PC.furnitureModel=:FMNAME"),
    @NamedQuery(name="ProductionCapacity.findPCbyMF",query = "SELECT PC from ProductionCapacity PC where PC.manufacturingFacility=:MFNAME order by PC.furnitureModel.name asc")
})
public class ProductionCapacity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private FurnitureModel furnitureModel;
    @Id
    @ManyToOne
    private ManufacturingFacility manufacturingFacility;
    private Integer qty;

    public FurnitureModel getFurnitureModel() {
        return furnitureModel;
    }

    public void setFurnitureModel(FurnitureModel furnitureModel) {
        this.furnitureModel = furnitureModel;
    }

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.furnitureModel);
        hash = 59 * hash + Objects.hashCode(this.manufacturingFacility);
        return hash;
    }

    public int getCapacity(Month mn, int Year) {
        return (Helper.getNumWorkDays(mn, Year) * this.qty);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductionCapacity)) {
            return false;
        }
        ProductionCapacity other = (ProductionCapacity) object;
        return this.furnitureModel.equals(other.furnitureModel) && this.manufacturingFacility.equals(other.manufacturingFacility);
    }

    @Override
    public String toString() {
        return "ProductionCapacity[ id=" + this.furnitureModel.getId() + ", " + this.manufacturingFacility.getId() + " ]";
    }

}
