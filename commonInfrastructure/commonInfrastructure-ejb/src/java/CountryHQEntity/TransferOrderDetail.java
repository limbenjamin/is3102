/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CountryHQEntity;

import OVERLAPS.Furniture;
import OVERLAPS.Retail_Item;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
public class TransferOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String type;
    
    private Double TransferCosting;
    
    @OneToOne(cascade = {CascadeType.ALL})
    private Furniture furniture;
    @OneToOne(cascade = {CascadeType.ALL})
    private Retail_Item retail_item;
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransferOrderDetail)) {
            return false;
        }
        TransferOrderDetail other = (TransferOrderDetail) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CountryHQEntity.TransferOrderDetail[ id=" + getId() + " ]";
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the TransferCosting
     */
    public Double getTransferCosting() {
        return TransferCosting;
    }

    /**
     * @param TransferCosting the TransferCosting to set
     */
    public void setTransferCosting(Double TransferCosting) {
        this.TransferCosting = TransferCosting;
    }

    /**
     * @return the furniture
     */
    public Furniture getFurniture() {
        return furniture;
    }

    /**
     * @param furniture the furniture to set
     */
    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }

    /**
     * @return the retail_item
     */
    public Retail_Item getRetail_item() {
        return retail_item;
    }

    /**
     * @param retail_item the retail_item to set
     */
    public void setRetail_item(Retail_Item retail_item) {
        this.retail_item = retail_item;
    }
    
}
