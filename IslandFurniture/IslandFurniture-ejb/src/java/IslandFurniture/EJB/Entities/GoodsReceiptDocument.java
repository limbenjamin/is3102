/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class GoodsReceiptDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(mappedBy = "goodsReceiptDocument")
    private PurchaseOrder receiveFrom;
    @OneToMany(mappedBy = "goodsReceiptDocument")
    private List<GoodsReceiptDocumentDetail> goodsReceiptDocumentDetails;
    @Temporal(TemporalType.TIMESTAMP)
    protected Calendar postingDate;
    @Temporal(TemporalType.TIMESTAMP)
    protected Calendar documentDate;
    private String deliveryNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrder getReceiveFrom() {
        return receiveFrom;
    }

    public void setReceiveFrom(PurchaseOrder receiveFrom) {
        this.receiveFrom = receiveFrom;
    }

    public List<GoodsReceiptDocumentDetail> getGoodsReceiptDocumentDetails() {
        return goodsReceiptDocumentDetails;
    }

    public void setGoodsReceiptDocumentDetails(List<GoodsReceiptDocumentDetail> goodsReceiptDocumentDetails) {
        this.goodsReceiptDocumentDetails = goodsReceiptDocumentDetails;
    }

    public Calendar getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Calendar postingDate) {
        this.postingDate = postingDate;
    }

    public Calendar getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Calendar documentDate) {
        this.documentDate = documentDate;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
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
        if (!(object instanceof GoodsReceiptDocument)) {
            return false;
        }
        GoodsReceiptDocument other = (GoodsReceiptDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.GoodsReceiptDocument[ id=" + id + " ]";
    }

}
