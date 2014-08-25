/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Benjamin
 */
@Entity
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique=true)
    private String Username;
    private String Password;
    private String Salt;
    private String Name;
    @Column(unique=true)
    private String EmailAddress;
    private String ForgottenPasswordCode;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogon;
    @OneToMany(mappedBy="Staff")
    private List<Todo> TodoList;
    @OneToMany(mappedBy="Recipient")
    private List<Message> Inbox;
    @OneToMany(mappedBy="Sender")
    private List<Message> Outbox;
    @ManyToOne
    private Store Store;
    @ManyToMany(mappedBy="Staffs")
    private List<Role> Roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getSalt() {
        return Salt;
    }

    public void setSalt(String Salt) {
        this.Salt = Salt;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String EmailAddress) {
        this.EmailAddress = EmailAddress;
    }

    public String getForgottenPasswordCode() {
        return ForgottenPasswordCode;
    }

    public void setForgottenPasswordCode(String ForgottenPasswordCode) {
        this.ForgottenPasswordCode = ForgottenPasswordCode;
    }

    public Date getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(Date lastLogon) {
        this.lastLogon = lastLogon;
    }

    public List<Todo> getTodoList() {
        return TodoList;
    }

    public void setTodoList(List<Todo> TodoList) {
        this.TodoList = TodoList;
    }

    public List<Message> getInbox() {
        return Inbox;
    }

    public void setInbox(List<Message> Inbox) {
        this.Inbox = Inbox;
    }

    public List<Message> getOutbox() {
        return Outbox;
    }

    public void setOutbox(List<Message> Outbox) {
        this.Outbox = Outbox;
    }

    public Store getStore() {
        return Store;
    }

    public void setStore(Store Store) {
        this.Store = Store;
    }

    public List<Role> getRoles() {
        return Roles;
    }

    public void setRoles(List<Role> Roles) {
        this.Roles = Roles;
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
        if (!(object instanceof Staff)) {
            return false;
        }
        Staff other = (Staff) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "commonInfrastructure.entities.Staff[ id=" + id + " ]";
    }
    
}
