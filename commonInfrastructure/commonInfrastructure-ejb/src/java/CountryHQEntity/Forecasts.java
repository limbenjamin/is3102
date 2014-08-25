/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CountryHQEntity;

import OVERLAPS.Store;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.*;
import javax.persistence.ManyToOne;

/**
 *
 * @author James
 */
@Entity
public class Forecasts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(cascade = {CascadeType.ALL})
    private Store store;
    
    @OneToMany(cascade = {CascadeType.ALL})
    private Collection<ForecastDetail> details=new ArrayList<ForecastDetail>();
    
    @ManyToOne
    private Country_Office country_office;
    
    
    

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
        if (!(object instanceof Forecasts)) {
            return false;
        }
        Forecasts other = (Forecasts) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CountryHQEntity.Forecasts[ id=" + getId() + " ]";
    }

    /**
     * @return the store
     */
    public Store getStore() {
        return store;
    }

    /**
     * @param store the store to set
     */
    public void setStore(Store store) {
        this.store = store;
    }

    /**
     * @return the details
     */
    public Collection<ForecastDetail> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(Collection<ForecastDetail> details) {
        this.details = details;
    }

    /**
     * @return the country_office
     */
    public Country_Office getCountry_office() {
        return country_office;
    }

    /**
     * @param country_office the country_office to set
     */
    public void setCountry_office(Country_Office country_office) {
        this.country_office = country_office;
    }
    
}
