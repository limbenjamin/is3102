/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import static IslandFurniture.Entities.Staff.AESDecrypt;
import static IslandFurniture.Entities.Staff.AESEncrypt;
import static IslandFurniture.Entities.Staff.SHA1Hash;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
 * @author James
 */
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
        private CustomerSegment customerSegment;
    private String emailAddress;
    private String password;
    private String salt;
    private String name;
    private String phoneNo;
    private String address;
    private String dateOfBirth;
    private Boolean active;
    private String forgottenPasswordCode;
    private String loyaltyCardId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogon;
    @ManyToOne
    private MembershipTier membershipTier;
    @ManyToOne
    private Country country;
    @ManyToMany(mappedBy = "customers")
    private List<ShoppingList> shoppingLists;
    @OneToMany(mappedBy="member")
    private List<Feedback> feedbacks;
    @OneToMany(mappedBy = "customer")
    private List<Redemption> redemptions;

    public CustomerSegment getCustomerSegment() {
        return customerSegment;
    }

    public void setCustomerSegment(CustomerSegment customerSegment) {
        this.customerSegment = customerSegment;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MembershipTier getMembershipTier() {
        return membershipTier;
    }

    public void setMembershipTier(MembershipTier membershipTier) {
        this.membershipTier = membershipTier;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<Redemption> getRedemptions() {
        return redemptions;
    }

    public void setRedemptions(List<Redemption> redemptions) {
        this.redemptions = redemptions;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public String getPhoneNo() {
        String decPhoneNo = AESDecrypt(phoneNo);
        return decPhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        String encPhoneNo = AESEncrypt(phoneNo);
        this.phoneNo = encPhoneNo;
    }

    public String getAddress() {
        String decAddress = AESDecrypt(address);
        return decAddress;
    }

    public void setAddress(String address) {
        String encAddress = AESEncrypt(address);
        this.address = encAddress;
    }

    public String getDateOfBirth() {
        String decDateOfBirth = AESDecrypt(dateOfBirth);
        return decDateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        String encDateOfBirth = AESEncrypt(dateOfBirth);
        this.dateOfBirth = encDateOfBirth;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getLoyaltyCardId() {
        return loyaltyCardId;
    }

    public void setLoyaltyCardId(String loyaltyCardId) {
        this.loyaltyCardId = loyaltyCardId;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer[ id=" + id + " ]";
    }
    
}
