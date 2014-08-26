/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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
 * @author Benjamin
 */
@Entity
public class Thread implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Staff Sender;
    @ManyToOne
    private Staff Recipient;
    @OneToMany(mappedBy="Thread")
    private List<Message> Messages;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getSender() {
        return Sender;
    }

    public void setSender(Staff Sender) {
        this.Sender = Sender;
    }

    public Staff getRecipient() {
        return Recipient;
    }

    public void setRecipient(Staff Recipient) {
        this.Recipient = Recipient;
    }

    public List<Message> getMessages() {
        return Messages;
    }

    public void setMessages(List<Message> Messages) {
        this.Messages = Messages;
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
        if (!(object instanceof Thread)) {
            return false;
        }
        Thread other = (Thread) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Thread[ id=" + id + " ]";
    }
    
}
