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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;

@Entity
@NamedQueries({
    @NamedQuery(name = "findStorageBinByName", query = "SELECT s FROM StorageBin s WHERE s.name = :name AND s.storageArea = :sa"),
    @NamedQuery(name = "findStorageBinByAreaType", query = "SELECT s FROM StorageBin s WHERE s.storageArea.plant = :plant AND s.storageArea.type = :saType")
})
public class StorageBin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "location")
    private List<StockUnit> stockUnits;
    @ManyToOne
    private StorageArea storageArea;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<StockUnit> getStockUnits() {
        return stockUnits;
    }

    public void setStockUnits(List<StockUnit> stockUnits) {
        this.stockUnits = stockUnits;
    }

    public StorageArea getStorageArea() {
        return storageArea;
    }

    public void setStorageArea(StorageArea storageArea) {
        this.storageArea = storageArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof StorageBin)) {
            return false;
        }
        StorageBin other = (StorageBin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StorageBin[ id=" + id + " ]";
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
