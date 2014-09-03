/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import IslandFurniture.EJB.Entities.LeadTimePK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@IdClass(LeadTimePK.class)
public class LeadTime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private ProcuredStock procuredStock;
    @Id
    @ManyToOne
    private Supplier supplier;
    private Integer weeks;

    public ProcuredStock getProcuredStock() {
        return procuredStock;
    }

    public void setProcuredStock(ProcuredStock procuredStock) {
        this.procuredStock = procuredStock;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Integer getWeeks() {
        return weeks;
    }

    public void setWeeks(Integer weeks) {
        this.weeks = weeks;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.procuredStock);
        hash = 97 * hash + Objects.hashCode(this.supplier);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LeadTime)) {
            return false;
        }
        LeadTime other = (LeadTime) object;
        return this.procuredStock.equals(other.procuredStock) && this.supplier.equals(other.supplier);
    }

    @Override
    public String toString() {
        return "IslandFurniture.FW.Entities.LeadTime[ id=" + this.procuredStock.getId() + ", " + this.supplier.getId() + " ]";
    }
    
}
