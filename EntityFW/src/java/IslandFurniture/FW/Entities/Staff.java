/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    private String username;
    private String password;
    private String salt;
    private String name;
    @Column(unique=true)
    private String emailAddress;
    @JoinColumn(nullable = true)
    private String forgottenPasswordCode;
    @Temporal(javax.persistence.TemporalType.DATE)
    @JoinColumn(nullable = true)
    private Date lastLogon;
    @OneToMany(mappedBy="staff")
    private List<Todo> todoList;
    @OneToMany(mappedBy="recipient")
    @JoinColumn(nullable = true) //TODO : remove later once Thread settled
    private List<Thread> inbox;
    @OneToMany(mappedBy="sender")
    @JoinColumn(nullable = true) //TODO : remove later once Thread settled
    private List<Thread> outbox;
    @ManyToOne
    @JoinColumn(nullable = true) //TODO : remove later once Plant settled
    private Plant plant;
    @ManyToMany(mappedBy="staffs")
    @JoinColumn(nullable = true) //TODO : remove later when role created
    private List<Role> roles;
    @OneToOne(cascade={CascadeType.ALL})
    private Preference preference;
    @OneToMany(cascade={CascadeType.ALL})
    private List<Notification> notifications;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="creator")
    private List<Announcement> announcements;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="creator")
    private List<Event> events;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getForgottenPasswordCode() {
        return forgottenPasswordCode;
    }

    public void setForgottenPasswordCode(String forgottenPasswordCode) {
        this.forgottenPasswordCode = forgottenPasswordCode;
    }

    public Date getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(Date lastLogon) {
        this.lastLogon = lastLogon;
    }

    public List<Todo> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public List<Thread> getInbox() {
        return inbox;
    }

    public void setInbox(List<Thread> inbox) {
        this.inbox = inbox;
    }

    public List<Thread> getOutbox() {
        return outbox;
    }

    public void setOutbox(List<Thread> outbox) {
        this.outbox = outbox;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
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
