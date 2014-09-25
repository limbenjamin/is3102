/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class ProductionOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long batchNo;
    private ProdOrderStatus status;
    @Temporal(TemporalType.DATE)
    private Calendar prodOrderDate;
    private int qty;
    private int completedQty;

    @ManyToOne
    private ManufacturingFacility mf;
    @ManyToOne
    private FurnitureModel furnitureModel;

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public ProdOrderStatus getStatus() {
        return status;
    }

    public void setStatus(ProdOrderStatus status) {
        this.status = status;
    }

    public Calendar getProdOrderDate() {
        return prodOrderDate;
    }

    public void setProdOrderDate(Calendar prodOrderDate) {
        this.prodOrderDate = prodOrderDate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(int completedQty) {
        this.completedQty = completedQty;
    }

    public ManufacturingFacility getMf() {
        return mf;
    }

    public void setMf(ManufacturingFacility mf) {
        this.mf = mf;
    }

    public FurnitureModel getFurnitureModel() {
        return furnitureModel;
    }

    public void setFurnitureModel(FurnitureModel furnitureModel) {
        this.furnitureModel = furnitureModel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (batchNo != null ? batchNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductionOrder)) {
            return false;
        }
        ProductionOrder other = (ProductionOrder) object;
        if ((this.batchNo == null && other.batchNo != null) || (this.batchNo != null && !this.batchNo.equals(other.batchNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.ProductionOrder[ id=" + batchNo + " ]";
    }

}
