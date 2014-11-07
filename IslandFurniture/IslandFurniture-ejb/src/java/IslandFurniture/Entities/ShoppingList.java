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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class ShoppingList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String idHash;
    private String name;
    private Double totalPrice;

    @OneToMany(mappedBy = "shoppingList")
    private List<ShoppingListDetail> shoppingListDetails;

    @ManyToMany
    private List<Customer> customers; // Shopping list can be collaborated upon.

    @ManyToOne
    private Store store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdHash() {
        return idHash;
    }

    public void setIdHash(String idHash) {
        this.idHash = idHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ShoppingListDetail> getShoppingListDetails() {
        return shoppingListDetails;
    }

    public void setShoppingListDetails(List<ShoppingListDetail> shoppingListDetails) {
        this.shoppingListDetails = shoppingListDetails;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
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
        if (!(object instanceof ShoppingList)) {
            return false;
        }
        ShoppingList other = (ShoppingList) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ShoppingList[ id=" + id + " ]";
    }

    // Extra Methods
    public boolean hasFurniture(FurnitureModel furniture) {
        for (ShoppingListDetail detail : shoppingListDetails) {
            if (detail.getFurnitureModel().equals(furniture)) {
                return true;
            }
        }
        return false;
    }
}
