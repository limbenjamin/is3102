/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
@IdClass(ManufacturingCapacityPK.class)
public class ManufacturingCapacity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Id
    private Stock stock;
    @Id
    private ManufacturingFacility manufacturingFacility;
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.stock);
        hash = 59 * hash + Objects.hashCode(this.manufacturingFacility);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ManufacturingCapacity)) {
            return false;
        }
        ManufacturingCapacity other = (ManufacturingCapacity) object;

         return this.stock.equals(other.stock) && this.manufacturingFacility.equals(other.manufacturingFacility);
                  
    }

    @Override
    public String toString() {
         return stock.id + ", " + manufacturingFacility.id ;
    }
    
}
