/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.Month;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getWisrByStoreIngredYrMth",
            query = "SELECT a FROM WeeklyIngredientSupplyReq a WHERE a.store = :store AND a.year = :year AND a.month = :month AND a.ingredient = :ingredient"),
    @NamedQuery(
            name = "findWisrByStoreYrMthWk",
            query = "SELECT a FROM WeeklyIngredientSupplyReq a WHERE a.store = :store AND a.year = :year AND a.month = :month AND a.week = :week AND a.ingredient = :ingredient"),
    @NamedQuery(
            name = "getWisrByStoreYrMthWk",
            query = "SELECT a FROM WeeklyIngredientSupplyReq a WHERE a.store = :store AND a.year = :year AND a.month = :month AND a.week = :week")
})
public class WeeklyIngredientSupplyReq implements Serializable, Comparable<WeeklyIngredientSupplyReq> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer week;
    private Month month;
    private Integer year;
    private Long qty;
    private Long qtyToOrder;

    @ManyToOne
    private Ingredient ingredient;

    @ManyToOne
    private Store store;

    @OneToOne
    private IngredientPurchaseOrderDetail ingredPoDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
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

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Long getQtyToOrder() {
        return qtyToOrder;
    }

    public void setQtyToOrder(Long qtyToOrder) {
        this.qtyToOrder = qtyToOrder;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public IngredientPurchaseOrderDetail getIngredPoDetail() {
        return ingredPoDetail;
    }

    public void setIngredPoDetail(IngredientPurchaseOrderDetail ingredPoDetail) {
        this.ingredPoDetail = ingredPoDetail;
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
        if (!(object instanceof WeeklyIngredientSupplyReq)) {
            return false;
        }
        WeeklyIngredientSupplyReq other = (WeeklyIngredientSupplyReq) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(WeeklyIngredientSupplyReq other){
        if(this.week < other.week){
            return -1;
        } else if (this.week > other.week){
            return 1;
        }
        
        return 0;
    }

    @Override
    public String toString() {
        return "WeeklyIngredientSupplyReq[ id=" + id + " ]";
    }

}
