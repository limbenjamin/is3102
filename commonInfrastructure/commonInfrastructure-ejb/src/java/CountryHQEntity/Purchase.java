/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CountryHQEntity;

import OVERLAPS.Furniture;
import OVERLAPS.RES_Menu;
import OVERLAPS.Retail_Item;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
public class Purchase implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(cascade = {CascadeType.ALL})
    private Furniture furniture;
    @OneToOne(cascade = {CascadeType.ALL})
    private Retail_Item retail_item;
    @OneToOne(cascade = {CascadeType.ALL})
    private RES_Menu res_Menu;
    
    private Double FinalPrice;
    private Double DiscountAmt;
    private String ProductType;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Purchase)) {
            return false;
        }
        Purchase other = (Purchase) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CountryHQEntity.Purchase[ id=" + getId() + " ]";
    }

    /**
     * @return the furniture
     */
    public Furniture getFurniture() {
        return furniture;
    }

    /**
     * @param furniture the furniture to set
     */
    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }

    /**
     * @return the retail_item
     */
    public Retail_Item getRetail_item() {
        return retail_item;
    }

    /**
     * @param retail_item the retail_item to set
     */
    public void setRetail_item(Retail_Item retail_item) {
        this.retail_item = retail_item;
    }

    /**
     * @return the res_Menu
     */
    public RES_Menu getRes_Menu() {
        return res_Menu;
    }

    /**
     * @param res_Menu the res_Menu to set
     */
    public void setRes_Menu(RES_Menu res_Menu) {
        this.res_Menu = res_Menu;
    }

    /**
     * @return the FinalPrice
     */
    public Double getFinalPrice() {
        return FinalPrice;
    }

    /**
     * @param FinalPrice the FinalPrice to set
     */
    public void setFinalPrice(Double FinalPrice) {
        this.FinalPrice = FinalPrice;
    }

    /**
     * @return the DiscountAmt
     */
    public Double getDiscountAmt() {
        return DiscountAmt;
    }

    /**
     * @param DiscountAmt the DiscountAmt to set
     */
    public void setDiscountAmt(Double DiscountAmt) {
        this.DiscountAmt = DiscountAmt;
    }

    /**
     * @return the ProductType
     */
    public String getProductType() {
        return ProductType;
    }

    /**
     * @param ProductType the ProductType to set
     */
    public void setProductType(String ProductType) {
        this.ProductType = ProductType;
    }
    
}
