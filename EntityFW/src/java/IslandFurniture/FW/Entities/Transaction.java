/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Keys.TransactionPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 *
 * @author James
 */
@Entity
@IdClass(TransactionPK.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Id
    @ManyToOne
    protected Store store;
    @ManyToOne
    protected StoreMember member;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StoreMember getMember() {
        return member;
    }

    public void setMember(StoreMember member) {
        this.member = member;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.store);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        return this.id.equals(other.id) && this.store.equals(other.store);
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.Transaction[ id=" + this.getStore().getId() + ", " + id + " ]";
    }
    
}
