/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CountryHQEntity;

import OVERLAPS.Store;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
public class Transfer_Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Date Request_Date;
    private Boolean Approved=false;
    private String DeliveryID;
    @OneToOne
    private Store FromStore;
    @OneToOne
    private Store ToStore;
    @OneToMany(cascade = {CascadeType.ALL})
    private Collection<TransferOrderDetail> details=new ArrayList<TransferOrderDetail>();
    
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transfer_Order)) {
            return false;
        }
        Transfer_Order other = (Transfer_Order) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CountryHQEntity.Transfer_Order[ id=" + getId() + " ]";
    }

    /**
     * @return the Request_Date
     */
    public Date getRequest_Date() {
        return Request_Date;
    }

    /**
     * @param Request_Date the Request_Date to set
     */
    public void setRequest_Date(Date Request_Date) {
        this.Request_Date = Request_Date;
    }

    /**
     * @return the Approved
     */
    public Boolean getApproved() {
        return Approved;
    }

    /**
     * @param Approved the Approved to set
     */
    public void setApproved(Boolean Approved) {
        this.Approved = Approved;
    }

    /**
     * @return the DeliveryID
     */
    public String getDeliveryID() {
        return DeliveryID;
    }

    /**
     * @param DeliveryID the DeliveryID to set
     */
    public void setDeliveryID(String DeliveryID) {
        this.DeliveryID = DeliveryID;
    }

    /**
     * @return the FromStore
     */
    public Store getFromStore() {
        return FromStore;
    }

    /**
     * @param FromStore the FromStore to set
     */
    public void setFromStore(Store FromStore) {
        this.FromStore = FromStore;
    }

    /**
     * @return the ToStore
     */
    public Store getToStore() {
        return ToStore;
    }

    /**
     * @param ToStore the ToStore to set
     */
    public void setToStore(Store ToStore) {
        this.ToStore = ToStore;
    }

    /**
     * @return the details
     */
    public Collection<TransferOrderDetail> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(Collection<TransferOrderDetail> details) {
        this.details = details;
    }
    
}
