/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author James
 */
@Entity
public class PromotionCoupon implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Boolean oneTimeUsage=true;

    
    @ManyToOne
    private PromotionDetail promotionDetail;

    public PromotionDetail getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(PromotionDetail promotionDetail) {
        this.promotionDetail = promotionDetail;
    }

    public Boolean getOneTimeUsage() {
        return oneTimeUsage;
    }

    public void setOneTimeUsage(Boolean oneTimeUsage) {
        this.oneTimeUsage = oneTimeUsage;
    }
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof PromotionCoupon)) {
            return false;
        }
        PromotionCoupon other = (PromotionCoupon) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IslandFurniture.Entities.PromotionCoupon[ id=" + id + " ]";
    }
    
}
