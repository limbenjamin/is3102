/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
import org.apache.commons.codec.binary.Base64;

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
    private String notes;
    //phone no. has to be string because it is encrypted 
    private String phoneNo;
    private Boolean active;
    private Integer invalidPasswordCount;
    @Column(unique=true)
    private String emailAddress;
    private String forgottenPasswordCode;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogon;
    @OneToMany(cascade={CascadeType.ALL},mappedBy="staff")
    private List<Todo> todoList;
    @ManyToMany(cascade={CascadeType.ALL})
    private List<MessageThread> inbox;
    @ManyToOne
    private Plant plant;
    @ManyToMany(mappedBy="staffs")
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
    
    static byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5 };
    static IvParameterSpec ivspec = new IvParameterSpec(iv);
    static String encryptionKey = "0123456789abcdef"; //TODO : move the keys into store entity
    
    // Used to encrypt name, email, phone no. to comply with PDPA's "reasonable security arrangements"
    private String AESEncrypt(String plaintext){
        String ciphertext = "";
        try {
            Key key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
            byte[] plainTextByte = cipher.doFinal(plaintext.getBytes());
            ciphertext = Base64.encodeBase64String(plainTextByte);
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ciphertext;
    }
    
    public static String AESDecrypt(String ciphertext){
        String plaintext = "";
        try{
            Key key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
            byte[] cipherTextByte = Base64.decodeBase64(ciphertext);
            byte[] plainTextByte = cipher.doFinal(cipherTextByte);
            plaintext = new String(plainTextByte);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | IOException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
        return plaintext;
    }
    
    public static String SHA1Hash(String fullPassword){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //use SHA256 hashing for passwords
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(fullPassword.getBytes());
            byte byteArray[] = messageDigest.digest();
            //convert to hex to store in db
            for (int i = 0; i < byteArray.length; i++) {
             stringBuilder.append(Integer.toString((byteArray[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringBuilder.toString();
    }
}
