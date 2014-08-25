/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CountryHQEntity;

import OVERLAPS.Furniture;
import OVERLAPS.RES_Menu;
import OVERLAPS.Retail_Item;
import OVERLAPS.Tier;
import java.awt.Menu;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
public class Promotion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Date PromoStartDate;
    private Date PromoEndDate;
    private Tier AppliesToTier;
    
    private double DiscountAmt;
    private double DiscountP;
    
    @OneToMany(cascade = {CascadeType.ALL})
    private Collection<Furniture> furniture=new ArrayList<Furniture>();
    @OneToMany(cascade = {CascadeType.ALL})
    private Collection<Retail_Item> retailitem=new ArrayList<Retail_Item>();
    @OneToMany(cascade = {CascadeType.ALL})
    private Collection<RES_Menu> menu=new ArrayList<RES_Menu>();
    

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
        if (!(object instanceof Promotion)) {
            return false;
        }
        Promotion other = (Promotion) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CountryHQEntity.Promotion[ id=" + getId() + " ]";
    }

    /**
     * @return the PromoStartDate
     */
    public Date getPromoStartDate() {
        return PromoStartDate;
    }

    /**
     * @param PromoStartDate the PromoStartDate to set
     */
    public void setPromoStartDate(Date PromoStartDate) {
        this.PromoStartDate = PromoStartDate;
    }

    /**
     * @return the PromoEndDate
     */
    public Date getPromoEndDate() {
        return PromoEndDate;
    }

    /**
     * @param PromoEndDate the PromoEndDate to set
     */
    public void setPromoEndDate(Date PromoEndDate) {
        this.PromoEndDate = PromoEndDate;
    }

    /**
     * @return the AppliesToTier
     */
    public Tier getAppliesToTier() {
        return AppliesToTier;
    }

    /**
     * @param AppliesToTier the AppliesToTier to set
     */
    public void setAppliesToTier(Tier AppliesToTier) {
        this.AppliesToTier = AppliesToTier;
    }

    /**
     * @return the DiscountAmt
     */
    public double getDiscountAmt() {
        return DiscountAmt;
    }

    /**
     * @param DiscountAmt the DiscountAmt to set
     */
    public void setDiscountAmt(double DiscountAmt) {
        this.DiscountAmt = DiscountAmt;
    }

    /**
     * @return the DiscountP
     */
    public double getDiscountP() {
        return DiscountP;
    }

    /**
     * @param DiscountP the DiscountP to set
     */
    public void setDiscountP(double DiscountP) {
        this.DiscountP = DiscountP;
    }

    /**
     * @return the furniture
     */
    public Collection<Furniture> getFurniture() {
        return furniture;
    }

    /**
     * @param furniture the furniture to set
     */
    public void setFurniture(Collection<Furniture> furniture) {
        this.furniture = furniture;
    }

    /**
     * @return the retailitem
     */
    public Collection<Retail_Item> getRetailitem() {
        return retailitem;
    }

    /**
     * @param retailitem the retailitem to set
     */
    public void setRetailitem(Collection<Retail_Item> retailitem) {
        this.retailitem = retailitem;
    }

    /**
     * @return the menu
     */
    public Collection<RES_Menu> getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(Collection<RES_Menu> menu) {
        this.menu = menu;
    }
    
}
