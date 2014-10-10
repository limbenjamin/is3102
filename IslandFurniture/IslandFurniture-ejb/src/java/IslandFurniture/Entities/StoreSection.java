/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class StoreSection implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer storeLevel;
    private String name;
    private String description;
    
    @ManyToOne
    Store store;
    
    @OneToMany(mappedBy = "locationInStore")
    List<StorefrontInventory> storefrontInventories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStoreLevel() {
        return storeLevel;
    }

    public void setStoreLevel(Integer storeLevel) {
        this.storeLevel = storeLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<StorefrontInventory> getStorefrontInventories() {
        return storefrontInventories;
    }

    public void setStorefrontInventories(List<StorefrontInventory> storefrontInventories) {
        this.storefrontInventories = storefrontInventories;
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
        if (!(object instanceof StoreSection)) {
            return false;
        }
        StoreSection other = (StoreSection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StoreSection[ id=" + id + " ]";
    }

}
