/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;

/**
 *
 * @author James
 */
@Entity
public class RestaurantTransactionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer qty;
    private Double unitPrice;
    private Long unitPoints;

    @ManyToOne
    private RestaurantTransaction restaurantTransaction;

    @ManyToOne
    private MenuItem menuItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getUnitPoints() {
        return unitPoints;
    }

    public void setUnitPoints(Long unitPoints) {
        this.unitPoints = unitPoints;
    }

    public RestaurantTransaction getRestaurantTransaction() {
        return restaurantTransaction;
    }

    public void setRestaurantTransaction(RestaurantTransaction restaurantTransaction) {
        this.restaurantTransaction = restaurantTransaction;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
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
        if (!(object instanceof RestaurantTransactionDetail)) {
            return false;
        }
        RestaurantTransactionDetail other = (RestaurantTransactionDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RestaurantTransactionDetail[ id=" + id + " ]";
    }

    // Extra Methods
    public Double getSubtotal() {
        return this.qty * this.unitPrice;
    }

    public Long getTotalPoints() {
        return this.qty * this.unitPoints;
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }

}
