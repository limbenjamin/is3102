/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author James
 */
@Entity
public class PromotionCampaign implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private CountryOffice countryOffice;

    @ManyToMany(mappedBy = "promotionCampaigns")
    private List<MembershipTier> membershipTiers;

    @OneToMany(mappedBy = "promotionCampaign")
    private List<PromotionDetail> promotionDetails;

    private String title;

    private String remark;

    private campaignGoal goal;
    @Temporal(TemporalType.DATE)
    private Calendar validUntil;

    @Temporal(TemporalType.DATE)
    private Calendar validFrom;

    public Calendar getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Calendar validUntil) {
        this.validUntil = validUntil;
    }

    public Calendar getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Calendar validFrom) {
        this.validFrom = validFrom;
    }

    
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public campaignGoal getGoal() {
        return goal;
    }

    public void setGoal(campaignGoal goal) {
        this.goal = goal;
    }

    public Calendar getUntil() {
        return validUntil;
    }

    public void setUntil(Calendar until) {
        this.validUntil = until;
    }

    public enum campaignGoal {

        PROSPECTING(0), SALES_BOOST(1), SEASONAL_PROMO(2), COMPETITION(3), NEW_PRODUCT_TESTING(4);
        public int value;

        private campaignGoal(int value) {
            this.value = value;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public List<MembershipTier> getMembershipTiers() {
        return membershipTiers;
    }

    public void setMembershipTiers(List<MembershipTier> membershipTiers) {
        this.membershipTiers = membershipTiers;
    }

    public List<PromotionDetail> getPromotionDetails() {
        return promotionDetails;
    }

    public void setPromotionDetails(List<PromotionDetail> promotionDetails) {
        this.promotionDetails = promotionDetails;
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
        if (!(object instanceof PromotionCampaign)) {
            return false;
        }
        PromotionCampaign other = (PromotionCampaign) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PromotionCampaign[ id=" + id + " ]";
    }

}
