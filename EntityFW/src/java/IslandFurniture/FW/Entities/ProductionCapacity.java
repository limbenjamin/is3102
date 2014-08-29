/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Keys.ProductionCapacityPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(ProductionCapacityPK.class)
public class ProductionCapacity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private Stock stock;
    @Id
    @ManyToOne
    private ManufacturingFacility manufacturingFacility;
    private Integer qty;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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
        hash = 89 * hash + Objects.hashCode(this.stock);
        hash = 89 * hash + Objects.hashCode(this.manufacturingFacility);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductionCapacity)) {
            return false;
        }
        ProductionCapacity other = (ProductionCapacity) object;
        return this.stock.equals(other.stock) && this.manufacturingFacility.equals(other.manufacturingFacility);
    }

    @Override
    public String toString() {
        return "IslandFurniture.FW.Entities.ProductionCapacity[ id=" + this.stock.getId() + ", " + this.manufacturingFacility.getId() + " ]";
    }
    
}
