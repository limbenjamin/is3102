/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE",discriminatorType = DiscriminatorType.STRING,length = 255)
public abstract class PromotionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne
    protected PromotionCampaign promotionCampaign;

    protected double percentageDiscount;
    protected double absoluteDiscount = 0;
    
    @ManyToOne
    protected MembershipTier membershiptier;

    @OneToOne
    protected Plant applicablePlant;

    protected Integer usageCount = Integer.MAX_VALUE;

    @OneToMany(mappedBy = "promotionDetail",cascade = CascadeType.REMOVE)
    protected List<PromotionCoupon> promotionCoupons = new ArrayList<>();

    public String getCouponStatus() {
        if (this.getPromotionCoupons().size() == 0) {
            return "No Coupon";
        }

        if (this.getPromotionCoupons().get(0).getOneTimeUsage() == false) {
            return "Non-Perishable Coupon";
        }

        return "Coupon";

    }

    public void setCouponStatus(String s) {
        return; //dummy for stupid JSF EL
    }

    public List<PromotionCoupon> getPromotionCoupons() {
        return promotionCoupons;
    }

    public void setPromotionCoupons(List<PromotionCoupon> promotionCoupons) {
        this.promotionCoupons = promotionCoupons;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Plant getApplicablePlant() {
        return applicablePlant;
    }

    public void setApplicablePlant(Plant applicablePlant) {
        this.applicablePlant = applicablePlant;
    }

    public MembershipTier getMembershiptier() {
        return membershiptier;
    }

    public void setMembershiptier(MembershipTier membershiptier) {
        this.membershiptier = membershiptier;
    }

    public double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public double getAbsoluteDiscount() {
        return absoluteDiscount;
    }

    public void setAbsoluteDiscount(double absoluteDiscount) {
        this.absoluteDiscount = absoluteDiscount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PromotionCampaign getPromotionCampaign() {
        return promotionCampaign;
    }

    public void setPromotionCampaign(PromotionCampaign promotionCampaign) {
        this.promotionCampaign = promotionCampaign;
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
        if (!(object instanceof PromotionDetail)) {
            return false;
        }
        PromotionDetail other = (PromotionDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PromotionDetail[ id=" + id + " ]";
    }

}
