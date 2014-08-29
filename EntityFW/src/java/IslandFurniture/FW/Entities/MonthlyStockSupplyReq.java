/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Enums.Month;
import IslandFurniture.FW.Entities.Keys.MonthlyStockSupplyReqPK;
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
 * @author asus
 */
@Entity
@IdClass(MonthlyStockSupplyReqPK.class)
public class MonthlyStockSupplyReq implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private Stock stock;
    @Id
    @ManyToOne
    private Store store;
    @Id
    private Month month;
    @Id
    private Integer year;
    private int beginInventory;
    private int qtySold;
    private int qtyForecasted;
    private int qtyRequested;
    private boolean commited;
    @OneToMany(mappedBy="monthlyStockSupplyReq")
    private List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

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

    public int getBeginInventory() {
        return beginInventory;
    }

    public void setBeginInventory(int beginInventory) {
        this.beginInventory = beginInventory;
    }

    public int getQtySold() {
        return qtySold;
    }

    public void setQtySold(int qtySold) {
        this.qtySold = qtySold;
    }

    public int getQtyForecasted() {
        return qtyForecasted;
    }

    public void setQtyForecasted(int qtyForecasted) {
        this.qtyForecasted = qtyForecasted;
    }

    public int getQtyRequested() {
        return qtyRequested;
    }

    public void setQtyRequested(int qtyRequested) {
        this.qtyRequested = qtyRequested;
    }

    public boolean isCommited() {
        return commited;
    }

    public void setCommited(boolean commited) {
        this.commited = commited;
    }

    public List<GoodsIssuedDocumentDetail> getGoodsIssuedDocumentDetails() {
        return goodsIssuedDocumentDetails;
    }

    public void setGoodsIssuedDocumentDetails(List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails) {
        this.goodsIssuedDocumentDetails = goodsIssuedDocumentDetails;
    }

    public void calcQtyRequested(){
        if(!this.commited)
            this.qtyRequested = this.qtyForecasted - this.qtySold;
    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.stock);
        hash = 97 * hash + Objects.hashCode(this.store);
        hash = 97 * hash + Objects.hashCode(this.month);
        hash = 97 * hash + Objects.hashCode(this.year);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyStockSupplyReq)) {
            return false;
        }
        MonthlyStockSupplyReq other = (MonthlyStockSupplyReq) object;
        return this.stock.equals(other.stock) && this.store.equals(other.store) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.MonthlyStockSupplyReq[ id=" + stock.getId() + ", " + store.getId() + ", " + month + ", " + year + " ]";
    }
    
}
