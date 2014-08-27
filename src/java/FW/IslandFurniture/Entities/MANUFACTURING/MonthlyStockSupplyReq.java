/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.MANUFACTURING;

import FW.IslandFurniture.Entities.Enums.Month;
import FW.IslandFurniture.Entities.INFRA.Store;
import FW.IslandFurniture.Entities.Keys.MonthlyStockSupplyReqPK;
import FW.IslandFurniture.Entities.STORE.Stock;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author asus
 */
@Entity
@IdClass(MonthlyStockSupplyReqPK.class)
public class MonthlyStockSupplyReq implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private Stock stock;
    @Id
    @ManyToOne
    private Store store;
    @Id
    private Month month;
    @Id
    private Integer year;
    @OneToMany(mappedBy="monthlyStockSupplyReq")
    private List<GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += ((stock != null && store != null && month != null && year != null) ? (stock.hashCode() ^ store.hashCode() ^ month.hashCode() ^ year.hashCode()) : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthlyStockSupplyReq)) {
            return false;
        }
        MonthlyStockSupplyReq other = (MonthlyStockSupplyReq) object;
        if (this.stock.equals(other.stock) && this.store.equals(other.store) && this.month == other.month && this.year == other.year) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.MANUFACTURING.MonthlyStockSupplyReq[ id=" + stock.getId() + ", " + store.getId() + ", " + month + ", " + year + " ]";
    }
    
}
