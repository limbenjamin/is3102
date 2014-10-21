/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Exceptions.InvalidInputException;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class CustChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar msgTime;

    @ManyToOne
    private CustChatThread thread;
    
    @ManyToOne
    private Staff staff;
    
    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Calendar msgTime) {
        this.msgTime = msgTime;
    }

    public CustChatThread getThread() {
        return thread;
    }

    public void setThread(CustChatThread thread) {
        this.thread = thread;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        if (!(object instanceof CustChatMessage)) {
            return false;
        }
        CustChatMessage other = (CustChatMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustChatMessage[ id=" + id + " ]";
    }

    // Entity callback checks
    @PrePersist
    @PreUpdate
    public void checkValid() throws InvalidInputException{
        if(this.customer != null && this.staff != null){
            throw new InvalidInputException("The creation of a message associated with both staff and customer was attempted.");
        }
    }
}
