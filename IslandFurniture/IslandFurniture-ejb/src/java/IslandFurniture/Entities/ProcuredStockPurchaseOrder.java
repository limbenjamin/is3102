/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({
    @NamedQuery(
            name = "getAllPurchaseOrders",
            query = "SELECT a FROM ProcuredStockPurchaseOrder a")
})
public class ProcuredStockPurchaseOrder extends PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "purchaseOrder", cascade = {CascadeType.ALL})
    private List<ProcuredStockPurchaseOrderDetail> purchaseOrderDetails = new ArrayList();

    @ManyToOne
    private ProcuredStockSupplier supplier;

    @OneToOne
    private GoodsReceiptDocument goodsReceiptDocument;

    @OneToOne
    private ManufacturingFacility manufacturingFacility;

    public List<ProcuredStockPurchaseOrderDetail> getPurchaseOrderDetails() {
        return purchaseOrderDetails;
    }

    public void setPurchaseOrderDetails(List<ProcuredStockPurchaseOrderDetail> purchaseOrderDetails) {
        this.purchaseOrderDetails = purchaseOrderDetails;
    }

    public ProcuredStockSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ProcuredStockSupplier supplier) {
        this.supplier = supplier;
    }

    public GoodsReceiptDocument getGoodsReceiptDocument() {
        return goodsReceiptDocument;
    }

    public void setGoodsReceiptDocument(GoodsReceiptDocument goodsReceiptDocument) {
        this.goodsReceiptDocument = goodsReceiptDocument;
    }

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
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
        if (!(object instanceof ProcuredStockPurchaseOrder)) {
            return false;
        }
        ProcuredStockPurchaseOrder other = (ProcuredStockPurchaseOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProcuredStockPurchaseOrder[ id=" + id + " ]";
    }

    // Extra Methods
    public boolean hasProcuredStock(ProcuredStock stock) {
        for (ProcuredStockPurchaseOrderDetail pod : purchaseOrderDetails) {
            if (pod.getProcuredStock().equals(stock)) {
                return true;
            }
        }
        return false;
    }
}
