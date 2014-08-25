package CountryHQEntity;

import OVERLAPS.Country;
import OVERLAPS.Store;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity(name = "Country_Office")
public class Country_Office implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(cascade = {CascadeType.ALL})
    private Country country;
    
    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "country_office")
    private Collection<Store> stores=new ArrayList<Store>();
    
    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "country_office")
    private Collection<Forecasts> forecasts=new ArrayList<Forecasts>();
    
    @OneToMany(cascade = {CascadeType.ALL})
    private Collection<Transfer_Order> transfers=new ArrayList<Transfer_Order>();
    
    
    
    

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
        if (!(object instanceof Country_Office)) {
            return false;
        }
        Country_Office other = (Country_Office) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ENTITY.Country_Office[ id=" + getId() + " ]";
    }

    /**
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * @return the stores
     */
    public Collection<Store> getStores() {
        return stores;
    }

    /**
     * @param stores the stores to set
     */
    public void setStores(Collection<Store> stores) {
        this.stores = stores;
    }

    /**
     * @return the forecasts
     */
    public Collection<Forecasts> getForecasts() {
        return forecasts;
    }

    /**
     * @param forecasts the forecasts to set
     */
    public void setForecasts(Collection<Forecasts> forecasts) {
        this.forecasts = forecasts;
    }

    /**
     * @return the transfers
     */
    public Collection<Transfer_Order> getTransfers() {
        return transfers;
    }

    /**
     * @param transfers the transfers to set
     */
    public void setTransfers(Collection<Transfer_Order> transfers) {
        this.transfers = transfers;
    }
    
}
