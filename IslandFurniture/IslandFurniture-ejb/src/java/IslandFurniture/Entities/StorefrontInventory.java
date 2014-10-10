/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(StorefrontInventoryPK.class)
public class StorefrontInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer repQty;
    private Integer maxQty;
    private Integer qty;

    @ManyToOne
    @Id
    Store store;

    @ManyToOne
    @Id
    Stock stock;

    @ManyToOne
    StoreSection locationInStore;

    public Integer getRepQty() {
        return repQty;
    }

    public void setRepQty(Integer repQty) {
        this.repQty = repQty;
    }

    public Integer getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Integer maxQty) {
        this.maxQty = maxQty;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StoreSection getLocationInStore() {
        return locationInStore;
    }

    public void setLocationInStore(StoreSection locationInStore) {
        this.locationInStore = locationInStore;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.store);
        hash = 47 * hash + Objects.hashCode(this.stock);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StorefrontInventory)) {
            return false;
        }
        StorefrontInventory other = (StorefrontInventory) object;
        return this.stock.equals(other.stock) && this.store.equals(other.store);
    }

    @Override
    public String toString() {
        return "StorefrontInventory[ id=" + store + ", " + stock + " ]";
    }

}
