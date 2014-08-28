/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.CountryOffice;

import FW.IslandFurniture.Entities.INFRA.StoreMember;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author James
 */
@Entity
public class MembershipTier implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToMany
    private List<PromotionCampaign> promotionCampaigns;
    @ManyToMany(mappedBy="membershipTiers")
    private List<StoreMember> members;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PromotionCampaign> getPromotionCampaigns() {
        return promotionCampaigns;
    }

    public void setPromotionCampaigns(List<PromotionCampaign> promotionCampaigns) {
        this.promotionCampaigns = promotionCampaigns;
    }

    public List<StoreMember> getMembers() {
        return members;
    }

    public void setMembers(List<StoreMember> members) {
        this.members = members;
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
        if (!(object instanceof MembershipTier)) {
            return false;
        }
        MembershipTier other = (MembershipTier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.CountryOffice.MembershipTier[ id=" + id + " ]";
    }
    
}