/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import IslandFurniture.EJB.Entities.StockSuppliedPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(StockSuppliedPK.class)
@NamedQueries({
@NamedQuery(name = "StockSupplied.FindByMf",query = "select ss from StockSupplied SS where SS.manufacturingFacility=:mf"),
    @NamedQuery(name = "StockSupplied.FindByMfAndS",query = "select ss from StockSupplied SS where SS.manufacturingFacility=:mf and SS.stock=:s")
})
public class StockSupplied implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private Stock stock;
    @Id
    @ManyToOne
    private CountryOffice countryOffice;
    @ManyToOne
    private ManufacturingFacility manufacturingFacility;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.stock);
        hash = 41 * hash + Objects.hashCode(this.countryOffice);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockSupplied)) {
            return false;
        }
        StockSupplied other = (StockSupplied) object;
        return this.stock.equals(other.stock) && this.countryOffice.equals(other.countryOffice);
    }

    @Override
    public String toString() {
        return "StockSupplied[ id=" + this.stock + ", " + this.countryOffice + " ]";
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
