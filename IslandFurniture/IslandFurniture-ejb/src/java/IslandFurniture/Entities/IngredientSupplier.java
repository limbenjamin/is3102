/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getIngredSuppliersByCo",
            query = "SELECT a FROM IngredientSupplier a WHERE a.co = :co"),
    @NamedQuery(
            name = "findIngredSupplierByNameAndCo",
            query = "SELECT a FROM IngredientSupplier a WHERE a.name = :name AND a.co = :co"),
})
public class IngredientSupplier extends Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(mappedBy = "ingredSupplier", cascade={CascadeType.PERSIST})
    private IngredientContract ingredContract;

    @OneToMany(mappedBy = "ingredSupplier")
    private List<IngredientPurchaseOrder> ingredPurchaseOrders;
    
    @ManyToOne
    private CountryOffice co;

    public IngredientContract getIngredContract() {
        return ingredContract;
    }

    public void setIngredContract(IngredientContract ingredContract) {
        this.ingredContract = ingredContract;
    }

    public List<IngredientPurchaseOrder> getIngredPurchaseOrders() {
        return ingredPurchaseOrders;
    }

    public void setIngredPurchaseOrders(List<IngredientPurchaseOrder> ingredPurchaseOrders) {
        this.ingredPurchaseOrders = ingredPurchaseOrders;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
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
        if (!(object instanceof IngredientSupplier)) {
            return false;
        }
        IngredientSupplier other = (IngredientSupplier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IngredientSupplier[ id=" + id + " ]";
    }

}
