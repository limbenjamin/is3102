/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class GoodsIssuedDocumentDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private GoodsIssuedDocument goodsIssuedDocument;
    @ManyToOne
    private Stock stock;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "STOCK_ID", referencedColumnName = "STOCK_ID", insertable = false, updatable = false),
        @JoinColumn(name = "COUNTRYOFFICE_ID", referencedColumnName = "COUNTRYOFFICE_ID"),
        @JoinColumn(name = "MONTH", referencedColumnName = "MONTH"),
        @JoinColumn(name = "YEAR", referencedColumnName = "YEAR")
    })
    private MonthlyStockSupplyReq monthlyStockSupplyReq;
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MonthlyStockSupplyReq getMonthlyStockSupplyReq() {
        return monthlyStockSupplyReq;
    }

    public void setMonthlyStockSupplyReq(MonthlyStockSupplyReq monthlyStockSupplyReq) {
        this.monthlyStockSupplyReq = monthlyStockSupplyReq;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
        if (!(object instanceof GoodsIssuedDocumentDetail)) {
            return false;
        }
        GoodsIssuedDocumentDetail other = (GoodsIssuedDocumentDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.GoodsIssuedDocumentDetail[ id=" + id + " ]";
    }

}
