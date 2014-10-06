/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.PurchaseOrderStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
    @NamedQuery(
            name = "getAllPurchaseOrders",
            query = "SELECT a FROM PurchaseOrder a")
})
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Plant shipsTo;
    @OneToMany(mappedBy = "purchaseOrder", cascade = {CascadeType.ALL})
    private List<PurchaseOrderDetail> purchaseOrderDetails;
    @ManyToOne
    private Supplier supplier;
    @OneToOne
    private GoodsReceiptDocument goodsReceiptDocument;
    @Temporal(TemporalType.DATE)
    private Calendar orderDate;
    private PurchaseOrderStatus status;
    @ManyToOne
    private MonthlyProcurementPlan monthlyProcurementPlan;
    
    
    @JoinColumn(name = "MF_ID",referencedColumnName = "ID")
    private ManufacturingFacility manufacturingFacility;

 

    public PurchaseOrder() {
        this.purchaseOrderDetails = new ArrayList<PurchaseOrderDetail>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plant getShipsTo() {
        return shipsTo;
    }

    public void setShipsTo(Plant shipsTo) {
        this.shipsTo = shipsTo;
    }

    public List<PurchaseOrderDetail> getPurchaseOrderDetails() {
        return purchaseOrderDetails;
    }

    public void setPurchaseOrderDetails(List<PurchaseOrderDetail> purchaseOrderDetails) {
        this.purchaseOrderDetails = purchaseOrderDetails;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public GoodsReceiptDocument getGoodsReceiptDocument() {
        return goodsReceiptDocument;
    }

    public void setGoodsReceiptDocument(GoodsReceiptDocument goodsReceiptDocument) {
        this.goodsReceiptDocument = goodsReceiptDocument;
    }

    public Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public MonthlyProcurementPlan getMonthlyProcurementPlan() {
        return monthlyProcurementPlan;
    }

    public void setMonthlyProcurementPlan(MonthlyProcurementPlan monthlyProcurementPlan) {
        this.monthlyProcurementPlan = monthlyProcurementPlan;
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
        if (!(object instanceof PurchaseOrder)) {
            return false;
        }
        PurchaseOrder other = (PurchaseOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.PurchaseOrder[ id=" + id + " ]";
    }

    // Extra Methods
    public boolean hasProcuredStock(ProcuredStock stock) {
        for (PurchaseOrderDetail pod : purchaseOrderDetails) {
            if (pod.getProcuredStock().equals(stock)) {
                return true;
            }
        }
        return false;
    }

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }
    


}