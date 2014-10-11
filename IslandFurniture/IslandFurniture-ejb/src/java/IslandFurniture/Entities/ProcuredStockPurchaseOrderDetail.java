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

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class ProcuredStockPurchaseOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private ProcuredStockPurchaseOrder purchaseOrder;
    @ManyToOne
    private ProcuredStock procuredStock;
    private Integer quantity;
    private Integer numberOfLots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcuredStockPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(ProcuredStockPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public ProcuredStock getProcuredStock() {
        return procuredStock;
    }

    public void setProcuredStock(ProcuredStock procuredStock) {
        this.procuredStock = procuredStock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getNumberOfLots() {
        return numberOfLots;
    }

    public void setNumberOfLots(Integer numberOfLots) {
        this.numberOfLots = numberOfLots;
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
        if (!(object instanceof ProcuredStockPurchaseOrderDetail)) {
            return false;
        }
        ProcuredStockPurchaseOrderDetail other = (ProcuredStockPurchaseOrderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProcuredStockPurchaseOrderDetail[ id=" + id + " ]";
    }
}
