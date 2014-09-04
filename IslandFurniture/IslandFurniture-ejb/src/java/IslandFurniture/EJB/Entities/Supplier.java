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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToMany
    private List<Ingredient> ingredients;
    @OneToMany(mappedBy="supplier")
    private List<PurchaseOrder> purchaseOrders;
    @ManyToOne
    private Country country;
    @OneToMany(mappedBy="supplier")
    private List<RestaurantPurchaseOrder> restaurantPurchaseOrders;
    @ManyToMany(mappedBy="suppliers")
    private List<ProcuredStock> procuredStocks;
    @OneToOne(mappedBy="supplier")
    private ProcurementContractDetail procurementContractDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<RestaurantPurchaseOrder> getRestaurantPurchaseOrders() {
        return restaurantPurchaseOrders;
    }

    public void setRestaurantPurchaseOrders(List<RestaurantPurchaseOrder> restaurantPurchaseOrders) {
        this.restaurantPurchaseOrders = restaurantPurchaseOrders;
    }

    public List<ProcuredStock> getProcuredStocks() {
        return procuredStocks;
    }

    public void setProcuredStocks(List<ProcuredStock> procuredStocks) {
        this.procuredStocks = procuredStocks;
    }

    public ProcurementContractDetail getProcurementContractDetail() {
        return procurementContractDetail;
    }

    public void setProcurementContractDetail(ProcurementContractDetail procurementContractDetail) {
        this.procurementContractDetail = procurementContractDetail;
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
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.Supplier[ id=" + id + " ]";
    }
    
}