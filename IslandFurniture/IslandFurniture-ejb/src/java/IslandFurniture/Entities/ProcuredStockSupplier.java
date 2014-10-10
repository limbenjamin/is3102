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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "findSupplierByName",
            query = "SELECT a FROM ProcuredStockSupplier a WHERE a.name = :name"),
    @NamedQuery(
            name = "getAllSuppliers",
            query = "SELECT a FROM ProcuredStockSupplier a")
})
public class ProcuredStockSupplier extends Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "supplier")
    private List<PurchaseOrder> purchaseOrders;

    @ManyToMany(mappedBy = "suppliers")
    private List<ProcuredStock> procuredStocks;

    @OneToOne(mappedBy = "supplier", cascade = {CascadeType.PERSIST})
    private ProcuredStockContract procuredStockContract;

    public List<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public List<ProcuredStock> getProcuredStocks() {
        return procuredStocks;
    }

    public void setProcuredStocks(List<ProcuredStock> procuredStocks) {
        this.procuredStocks = procuredStocks;
    }

    public ProcuredStockContract getProcuredStockContract() {
        return procuredStockContract;
    }

    public void setProcuredStockContract(ProcuredStockContract procuredStockContract) {
        this.procuredStockContract = procuredStockContract;
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
        if (!(object instanceof ProcuredStockSupplier)) {
            return false;
        }
        ProcuredStockSupplier other = (ProcuredStockSupplier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProcuredStockSupplier[ id=" + id + " ]";
    }

}
