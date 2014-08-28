/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Benjamin
 */
@Entity
public class Log implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String EntityName;
    private long EntityId;
    private String UserAction;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date Timestamp;
    private String ChangeMessage;
    @ManyToOne
    private Staff Staff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return EntityName;
    }

    public void setEntityName(String EntityName) {
        this.EntityName = EntityName;
    }

    public long getEntityId() {
        return EntityId;
    }

    public void setEntityId(long EntityId) {
        this.EntityId = EntityId;
    }

    public String getUserAction() {
        return UserAction;
    }

    public void setUserAction(String UserAction) {
        this.UserAction = UserAction;
    }

    public Date getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Date Timestamp) {
        this.Timestamp = Timestamp;
    }

    public String getChangeMessage() {
        return ChangeMessage;
    }

    public void setChangeMessage(String ChangeMessage) {
        this.ChangeMessage = ChangeMessage;
    }

    public Staff getStaff() {
        return Staff;
    }

    public void setStaff(Staff Staff) {
        this.Staff = Staff;
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
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Log[ id=" + id + " ]";
    }
    
}
