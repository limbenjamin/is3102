/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import static IslandFurniture.StaticClasses.EncryptMethods.AESDecrypt;
import static IslandFurniture.StaticClasses.EncryptMethods.AESEncrypt;
import static IslandFurniture.StaticClasses.EncryptMethods.SHA1Hash;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @Column(unique = true)
    private String username;
    private String password;
    private String salt;
    private String name;
    private String notes;
    //phone no. has to be string because it is encrypted 
    private String phoneNo;
    private Boolean active;
    private Integer invalidPasswordCount;
    private String emailAddress;
    private String forgottenPasswordCode;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogon;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "staff", fetch = FetchType.EAGER)
    private List<Todo> todoList;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<MessageThread> inbox;
    @ManyToOne(fetch = FetchType.EAGER)
    private Plant plant;
    @ManyToMany(mappedBy = "staffs")
    private List<Role> roles;
    @OneToOne(cascade = {CascadeType.ALL})
    private Preference preference;
    @OneToMany(mappedBy = "staff", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Notification> notifications;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "creator", fetch = FetchType.EAGER)
    private List<Announcement> announcements;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "creator", fetch = FetchType.EAGER)
    private List<Event> events;
    private String cardId;

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
        String fullPassword = salt + "" + password;
        String hashedPassword = SHA1Hash(fullPassword);
        this.password = hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        String decName = AESDecrypt(name);
        return decName;
    }

    public void setName(String name) {
        String encName = AESEncrypt(name);
        this.name = encName;
    }

    public String getEmailAddress() {
        String decEmailAddress = AESDecrypt(emailAddress);
        return decEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        String encEmailAddress = AESEncrypt(emailAddress);
        this.emailAddress = encEmailAddress;
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

    public List<MessageThread> getInbox() {
        return inbox;
    }

    public void setInbox(List<MessageThread> inbox) {
        this.inbox = inbox;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhoneNo() {
        String decPhoneNo = AESDecrypt(phoneNo);
        return decPhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        String encPhoneNo = AESEncrypt(phoneNo);
        this.phoneNo = encPhoneNo;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getInvalidPasswordCount() {
        return invalidPasswordCount;
    }

    public void setInvalidPasswordCount(Integer invalidPasswordCount) {
        this.invalidPasswordCount = invalidPasswordCount;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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
        return "Staff[ id=" + id + " ]";
    }

}
