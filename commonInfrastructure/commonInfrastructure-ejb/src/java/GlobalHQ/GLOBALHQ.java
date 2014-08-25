/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GlobalHQ;

import CountryHQEntity.Country_Office;
import OVERLAPS.ManufacturingFacility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.Singleton;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
/* this is a singleton*/
public class GLOBALHQ implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany()
    private Collection<Country_Office> CountryOffices=new ArrayList<Country_Office>();
    
    @OneToMany()
    private Collection<ManufacturingFacility> facilities=new ArrayList<ManufacturingFacility>();
    
    
    

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
        if (!(object instanceof GLOBALHQ)) {
            return false;
        }
        GLOBALHQ other = (GLOBALHQ) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GlobalHQ.GLOBALHQ[ id=" + getId() + " ]";
    }

    /**
     * @return the CountryOffices
     */
    public Collection<Country_Office> getCountryOffices() {
        return CountryOffices;
    }

    /**
     * @param CountryOffices the CountryOffices to set
     */
    public void setCountryOffices(Collection<Country_Office> CountryOffices) {
        this.CountryOffices = CountryOffices;
    }

    /**
     * @return the facilities
     */
    public Collection<ManufacturingFacility> getFacilities() {
        return facilities;
    }

    /**
     * @param facilities the facilities to set
     */
    public void setFacilities(Collection<ManufacturingFacility> facilities) {
        this.facilities = facilities;
    }
    
}
