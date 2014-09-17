/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.Query;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(MonthlyStockSupplyReqPK.class)
@NamedQueries({

    @NamedQuery(name = "MonthlyStockSupplyReq.find",query = "select MSSR from MonthlyStockSupplyReq MSSR where MSSR.year=:y and MSSR.stock=:fm and MSSR.month=:m")
   ,@NamedQuery(name="MonthlyStockSupplyReq.finduntiltime",query="select MSSR from MonthlyStockSupplyReq MSSR where MSSR.year*12+(MSSR.month+1)<=:y*12+(:m+1)"),
    @NamedQuery(
            name = "getMssrByStoreStock",
            query = "SELECT a FROM MonthlyStockSupplyReq a WHERE "
                    + "a.store = :store AND a.stock = :stock AND "
                    + "a.year*12 + a.month >= :startYr*12 + :startMth AND "
                    + "a.year*12 + a.month <= :endYr*12 + :endMth"),
    @NamedQuery(
            name = "MonthlyStockSupplyReq.FindByStoreStock",
            query = "SELECT MSSR FROM MonthlyStockSupplyReq MSSR WHERE MSSR.store = :store and MSSR.stock=:stock and MSSR.year*12+(MSSR.month+1)<=:y*12+(:m+1) and MSSR.committed=TRUE and MSSR.year*12+(MSSR.month+1)>=:ny*12+(:nm+1)")
})
public class MonthlyStockSupplyReq implements Serializable, Comparable<MonthlyStockSupplyReq> {

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
    private int idealInventory;
    private boolean committed;
    @OneToMany(mappedBy = "monthlyStockSupplyReq")
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

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean commited) {
        this.committed = commited;
    }

    public int getIdealInventory() {
        return idealInventory;
    }

    public void setIdealInventory(int idealInventory) {
        this.idealInventory = idealInventory;
    }

    public List<GoodsIssuedDocumentDetail> getGoodsIssuedDocumentDetails() {
        return goodsIssuedDocumentDetails;
    }

    public void setGoodsIssuedDocumentDetails(List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails) {
        this.goodsIssuedDocumentDetails = goodsIssuedDocumentDetails;
    }

    public MonthlyProductionPlan getMonthlyProductionPlan(EntityManager em) throws Exception {
        try{
        Query q=em.createNamedQuery("MonthlyProductionPlan.Find");
        q.setParameter("m", this.month);
        q.setParameter("y", this.year);
        q.setParameter("fm", this.stock);

        return (MonthlyProductionPlan)q.getResultList().get(0);
        }catch(Exception ex){
        throw  new RuntimeException("This MSSR does not have a MPP yet");
        }
    }


    public void calcQtyRequested() {
        if (!this.committed) {
            this.qtyRequested = this.qtyForecasted - this.qtySold;
        }
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
    public int compareTo(MonthlyStockSupplyReq other) {
        if (this.store.getId() > other.store.getId()) {
            return 1;
        }
        if (this.store.getId() < other.store.getId()) {
            return -1;
        }
        if (this.year * 12 + this.month.value > other.year * 12 + other.month.value) {
            return 1;
        }
        if (this.year * 12 + this.month.value < other.year * 12 + other.month.value) {
            return -1;
        }
        if (this.stock.getId() > other.stock.getId()) {
            return 1;
        }
        if (this.stock.getId() < other.stock.getId()) {
            return -1;
        }
        return 0;

    }

    @Override
    public String toString() {
        return "MonthlyStockSupplyReq[ id=" + stock.getId() + ", " + store.getId() + ", " + month + ", " + year + " ]";
    }

    // Entity Callbacks
    @PostPersist
    public void postPersist() {
        System.out.println("Successfully persisted " + this);
    }

}
