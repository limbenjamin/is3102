/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.MANUFACTURING;

import FW.IslandFurniture.Entities.INFRA.Store;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author asus
 */
@Entity
public class GoodsIssuedDocument implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Store store;
    @OneToMany(mappedBy="goodsIssuedDocument")
    private List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<GoodsIssuedDocumentDetail> getGoodsIssuedDocumentDetails() {
        return goodsIssuedDocumentDetails;
    }

    public void setGoodsIssuedDocumentDetails(List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails) {
        this.goodsIssuedDocumentDetails = goodsIssuedDocumentDetails;
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
