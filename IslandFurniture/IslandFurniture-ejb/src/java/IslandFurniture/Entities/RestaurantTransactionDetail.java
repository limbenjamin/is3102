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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

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
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "TRANSACTION_ID", referencedColumnName = "ID"),
        @JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID")
    })
    private RestaurantTransaction restaurantTransaction;
    @ManyToOne
    private MenuItem menuItem;
    @ManyToOne
    private PromotionDetail promotionDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PromotionDetail getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(PromotionDetail promotionDetail) {
        this.promotionDetail = promotionDetail;
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
        return "FW.IslandFurniture.Entities.STORE.RestaurantTransactionDetail[ id=" + id + " ]";
    }

}