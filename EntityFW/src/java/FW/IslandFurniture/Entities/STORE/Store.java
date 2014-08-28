/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FW.IslandFurniture.Entities.STORE;


import FW.IslandFurniture.Entities.INFRA.Staff;
import FW.IslandFurniture.Entities.MANUFACTURING.GoodsIssuedDocument;
import FW.IslandFurniture.Entities.MANUFACTURING.MonthlyStockSupplyReq;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Benjamin
 */
@Entity
public class Store extends Plant implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    @OneToMany(mappedBy="store")
    private List<Staff> employees;
    @OneToMany(mappedBy="store")
    private List<GoodsIssuedDocument> goodsIssuedDocument;
    @OneToMany(mappedBy="store")
    private List<MonthlyStockSupplyReq> monthlyStockSupplyReqs;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Staff> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Staff> employees) {
        this.employees = employees;
    }

    public List<GoodsIssuedDocument> getGoodsIssuedDocument() {
        return goodsIssuedDocument;
    }

    public void setGoodsIssuedDocument(List<GoodsIssuedDocument> goodsIssuedDocument) {
        this.goodsIssuedDocument = goodsIssuedDocument;
    }

    public List<MonthlyStockSupplyReq> getMonthlyStockSupplyReqs() {
        return monthlyStockSupplyReqs;
    }

    public void setMonthlyStockSupplyReqs(List<MonthlyStockSupplyReq> monthlyStockSupplyReqs) {
        this.monthlyStockSupplyReqs = monthlyStockSupplyReqs;
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
        if (!(object instanceof Store)) {
            return false;
        }
        Store other = (Store) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Store[ id=" + id + " ]";
    }
    
}
