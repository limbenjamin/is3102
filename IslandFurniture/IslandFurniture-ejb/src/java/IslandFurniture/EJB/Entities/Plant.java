/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author James
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"COUNTRY_ID", "NAME"})
})
@NamedQueries({
    @NamedQuery(
        name="findPlantByName",
        query="SELECT a FROM Plant a WHERE a.country = :country AND a.name = :name")
})
public abstract class Plant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String name;
    @ManyToOne
    protected Country country;
    @OneToMany(mappedBy = "plant")
    private List<Staff> employees;
    @ManyToMany
    protected List<PlantStockInventory> plantStockInventories;
    @OneToMany(mappedBy = "plant")
    protected List<StorageArea> storageAreas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Staff> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Staff> employees) {
        this.employees = employees;
    }

    public List<PlantStockInventory> getPlantStockInventories() {
        return plantStockInventories;
    }

    public void setPlantStockInventories(List<PlantStockInventory> plantStockInventories) {
        this.plantStockInventories = plantStockInventories;
    }

    public List<StorageArea> getStorageAreas() {
        return storageAreas;
    }

    public void setStorageAreas(List<StorageArea> storageAreas) {
        this.storageAreas = storageAreas;
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
        if (!(object instanceof Plant)) {
            return false;
        }
        Plant other = (Plant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.Plant[ id=" + id + " ]";
    }

}
