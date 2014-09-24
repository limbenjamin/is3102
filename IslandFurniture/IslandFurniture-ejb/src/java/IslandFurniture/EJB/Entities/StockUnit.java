/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author James
 */
@Entity
public class StockUnit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String batchNo;
    private Long qty = 0l;
    private Boolean available;
    private Long commitStockUnitId;
    @ManyToOne
    private GoodsIssuedDocument goodsIssuedDocument;
    @ManyToOne
    private Stock stock;
    @ManyToOne
    private StorageBin location;
    @ManyToOne
    private StorageBin pendingLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
        
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getCommitStockUnitId() {
        return commitStockUnitId;
    }

    public void setCommitStockUnitId(Long commitStockUnitId) {
        this.commitStockUnitId = commitStockUnitId;
    }

    public GoodsIssuedDocument getGoodsIssuedDocument() {
        return goodsIssuedDocument;
    }

    public void setGoodsIssuedDocument(GoodsIssuedDocument goodsIssuedDocument) {
        this.goodsIssuedDocument = goodsIssuedDocument;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StorageBin getLocation() {
        return location;
    }

    public void setLocation(StorageBin location) {
        this.location = location;
    }

    public StorageBin getPendingLocation() {
        return pendingLocation;
    }

    public void setPendingLocation(StorageBin pendingLocation) {
        this.pendingLocation = pendingLocation;
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
        if (!(object instanceof StockUnit)) {
            return false;
        }
        StockUnit other = (StockUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.StockUnit[ id=" + id + " ]";
    }

}
