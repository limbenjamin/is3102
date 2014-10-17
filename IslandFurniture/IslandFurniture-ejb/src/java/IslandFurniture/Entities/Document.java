/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Document implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Temporal(TemporalType.TIMESTAMP)
    protected Calendar creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    protected Calendar lastModTime;
    
    @ManyToOne
    protected Staff createdBy;
    
    @ManyToOne
    protected Staff lastModBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public Calendar getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(Calendar lastModTime) {
        this.lastModTime = lastModTime;
    }

    public Staff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Staff createdBy) {
        this.createdBy = createdBy;
    }

    public Staff getLastModBy() {
        return lastModBy;
    }

    public void setLastModBy(Staff lastModBy) {
        this.lastModBy = lastModBy;
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
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Document[ id=" + id + " ]";
    }
    
    // Entity Callbacks
    @PreUpdate
    public void preUpdate(){
        this.lastModTime = Calendar.getInstance();
    }
    
    @PrePersist
    public void prePersist(){
        this.creationTime = Calendar.getInstance();
        this.lastModTime = Calendar.getInstance();
    }
}
