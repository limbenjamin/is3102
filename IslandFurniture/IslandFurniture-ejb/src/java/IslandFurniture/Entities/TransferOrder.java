/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.TransferOrderStatus;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TransferOrder extends Document implements Serializable {

    private static final long serialVersionUID = 1L;
    protected TransferOrderStatus status;
    @Temporal(TemporalType.DATE)
    protected Calendar transferDate;
    protected String remark;

    @ManyToOne
    protected Plant requestingPlant;

    public Plant getRequestingPlant() {
        return requestingPlant;
    }

    public void setRequestingPlant(Plant requestingPlant) {
        this.requestingPlant = requestingPlant;
    }

    public TransferOrderStatus getStatus() {
        return status;
    }

    public void setStatus(TransferOrderStatus status) {
        this.status = status;
    }

    public Calendar getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Calendar transferDate) {
        this.transferDate = transferDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof TransferOrder)) {
            return false;
        }
        TransferOrder other = (TransferOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TransferOrder[ id=" + id + " ]";
    }

}
