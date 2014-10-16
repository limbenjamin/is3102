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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class GoodsIssuedDocument extends Document implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    private Plant plant;
    @ManyToOne
    private Plant deliverTo;
    @OneToMany(mappedBy = "goodsIssuedDocument")
    private List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar postingDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar issuedDate;
    private Boolean confirm;
    private Boolean received;

    public Boolean isReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Plant getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(Plant deliverTo) {
        this.deliverTo = deliverTo;
    }

    public List<GoodsIssuedDocumentDetail> getGoodsIssuedDocumentDetails() {
        return goodsIssuedDocumentDetails;
    }

    public void setGoodsIssuedDocumentDetails(List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails) {
        this.goodsIssuedDocumentDetails = goodsIssuedDocumentDetails;
    }

    public Calendar getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Calendar postingDate) {
        this.postingDate = postingDate;
    }

    public Calendar getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Calendar issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
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
        if (!(object instanceof GoodsIssuedDocument)) {
            return false;
        }
        GoodsIssuedDocument other = (GoodsIssuedDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.GoodsIssuedDocument[ id=" + id + " ]";
    }

}