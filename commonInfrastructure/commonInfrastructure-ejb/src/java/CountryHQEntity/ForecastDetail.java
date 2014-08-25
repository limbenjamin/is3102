/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CountryHQEntity;

import OVERLAPS.*;
import java.io.Serializable;
import java.util.Date;
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
public abstract class ForecastDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Date Period;
    
    private long forecast;
    
    private String type;
    
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
        if (!(object instanceof ForecastDetail)) {
            return false;
        }
        ForecastDetail other = (ForecastDetail) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ENTITY.Forecast[ id=" + getId() + " ]";
    }

    /**
     * @return the Period
     */
    public Date getPeriod() {
        return Period;
    }

    /**
     * @param Period the Period to set
     */
    public void setPeriod(Date Period) {
        this.Period = Period;
    }

    /**
     * @return the forecast
     */
    public long getForecast() {
        return forecast;
    }

    /**
     * @param forecast the forecast to set
     */
    public void setForecast(long forecast) {
        this.forecast = forecast;
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
