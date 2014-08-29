/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author James
 */
@Entity
public class RestaurantGoodsReceiptDocumentDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private RestaurantGoodReceiptDocument restaurantGoodReceiptDocument;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RestaurantGoodReceiptDocument getRestaurantGoodReceiptDocument() {
        return restaurantGoodReceiptDocument;
    }

    public void setRestaurantGoodReceiptDocument(RestaurantGoodReceiptDocument restaurantGoodReceiptDocument) {
        this.restaurantGoodReceiptDocument = restaurantGoodReceiptDocument;
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
        if (!(object instanceof RestaurantGoodsReceiptDocumentDetail)) {
            return false;
        }
        RestaurantGoodsReceiptDocumentDetail other = (RestaurantGoodsReceiptDocumentDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.RestaurantGoodsReceiptDocumentDetail[ id=" + id + " ]";
    }
    
}
