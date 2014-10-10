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
 * @author James
 */
@Entity
public class RestaurantGoodReceiptDocument implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private ProcuredStockSupplier supplier;
    
    @OneToMany(mappedBy="restaurantGoodReceiptDocument")
    private List<RestaurantGoodsReceiptDocumentDetail> restaurantGoodsReceiptDocumentDetails;
    
    @ManyToOne
    private Ingredient ingredient;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcuredStockSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ProcuredStockSupplier supplier) {
        this.supplier = supplier;
    }

    public List<RestaurantGoodsReceiptDocumentDetail> getRestaurantGoodsReceiptDocumentDetails() {
        return restaurantGoodsReceiptDocumentDetails;
    }

    public void setRestaurantGoodsReceiptDocumentDetails(List<RestaurantGoodsReceiptDocumentDetail> restaurantGoodsReceiptDocumentDetails) {
        this.restaurantGoodsReceiptDocumentDetails = restaurantGoodsReceiptDocumentDetails;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
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
        if (!(object instanceof RestaurantGoodReceiptDocument)) {
            return false;
        }
        RestaurantGoodReceiptDocument other = (RestaurantGoodReceiptDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.RestaurantGoodReceiptDocument[ id=" + id + " ]";
    }
    
}
