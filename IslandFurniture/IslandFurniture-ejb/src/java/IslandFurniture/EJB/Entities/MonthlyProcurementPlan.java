/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlanPK;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(MonthlyProcurementPlanPK.class)
public class MonthlyProcurementPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Month month;
    @Id
    private Integer year;
    @Id
    private RetailItem retailItem;
    @Id
    @ManyToOne
    private ManufacturingFacility manufacturingFacility;    
    @OneToMany(mappedBy="monthlyProcurementPlan")
    private List<PurchaseOrder> purchaseOrderList;
    private Integer qty;
    private Boolean locked;


    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public RetailItem getRetailItem() {
        return retailItem;
    }

    public void setRetailItem(RetailItem retailItem) {
        this.retailItem = retailItem;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public ManufacturingFacility getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacility manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.month);
        hash = 53 * hash + Objects.hashCode(this.year);
        hash = 53 * hash + Objects.hashCode(this.retailItem);
        hash = 53 * hash + Objects.hashCode(this.purchaseOrderList);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyProcurementPlan)) {
            return false;
        }
        MonthlyProcurementPlan other = (MonthlyProcurementPlan) object;
        return this.month.equals(other.month) && this.year.equals(other.year) && this.retailItem.equals(other.retailItem) && this.purchaseOrderList.equals(other.purchaseOrderList);
    }

    @Override
    public String toString() {
        return "IslandFurniture.FW.Entities.MonthlyProcurementPlan[ id=" + month + ", " + year + ", " + this.retailItem.getId() + ", " + this.purchaseOrderList + " ]";
    }
    
}
