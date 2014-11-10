/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@Entity
public class MarketBasketAnalysis implements Serializable {

    //This analysis shall not have time trend
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    private List<FurnitureModel> furnituremodels = new ArrayList<FurnitureModel>();

    @OneToOne
    private CountryOffice countryOffice;
    
    private Integer basketCount=0;

    public Integer getBasketCount() {
        return basketCount;
    }

    public void setBasketCount(Integer basketCount) {
        this.basketCount = basketCount;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof MarketBasketAnalysis)) {
            return false;
        }
        MarketBasketAnalysis other = (MarketBasketAnalysis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IslandFurniture.Entities.MarketBasketAnalysis[ id=" + id + " ]";
    }

    public List<FurnitureModel> getFurnituremodels() {
        return furnituremodels;
    }

    public void setFurnituremodels(List<FurnitureModel> furnituremodels) {
        this.furnituremodels = furnituremodels;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }
    
    
    
    

}
