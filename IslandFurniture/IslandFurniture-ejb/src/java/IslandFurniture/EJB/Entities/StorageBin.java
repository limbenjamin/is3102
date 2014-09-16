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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;

 /**
 *      @author Kamilul Ashraf
 *
 *      List of Attributes for Storage Location
 *
 * 1.   Plant Number - Integer
 *      Plant/Store Number (eg. 1, 2, 3)
 * 
 * 2.   Storage Area Number - Integer
 *      Area Number (eg. 1, 2.. )
 * 
 * 3.   Storage Area Name - String
 *      Area Name (eg. Staging Area, Receiving Area)
 * 
 * 4.   Storage ID - String
 *      Storage ID (eg. A12, B55, C42.. )
 * 
 * 5.   Storage Type - String
 *      Type of Storage (eg. Public or Private)
 * 
 * 6.   Storage Description - String
 *      Description of Storage Space (eg. Usually store SKU 1234)
 *	
 */

@Entity
public class StorageBin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy="location")
    private List<StockUnit> stockUnits;
    
    @ManyToOne
    private StorageArea storageArea;
    
    private Integer plantNumber;
    private Integer storageAreaNumber;
    private String storageAreaName;
    private String storageID;
    private String storageType;
    private String storageDescription;
    
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

    public Integer getPlantNumber() {
        return plantNumber;
    }

    public void setPlantNumber(Integer plantNumber) {
        this.plantNumber = plantNumber;
    }

    public Integer getStorageAreaNumber() {
        return storageAreaNumber;
    }

    public void setStorageAreaNumber(Integer storageAreaNumber) {
        this.storageAreaNumber = storageAreaNumber;
    }

    public String getStorageAreaName() {
        return storageAreaName;
    }

    public void setStorageAreaName(String storageAreaName) {
        this.storageAreaName = storageAreaName;
    }

    public String getStorageID() {
        return storageID;
    }

    public void setStorageID(String storageID) {
        this.storageID = storageID;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getStorageDescription() {
        return storageDescription;
    }

    public void setStorageDescription(String storageDescription) {
        this.storageDescription = storageDescription;
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
        return "StorageLocation[ id=" + id + " ]";
    }
    
    // Entity Callbacks

    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }
}
