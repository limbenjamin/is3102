/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.MembershipTier;
import static IslandFurniture.StaticClasses.EncryptMethods.SHA1Hash;
import IslandFurniture.StaticClasses.SendEmailByPost;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
public class ManageMemberAuthenticationBean implements ManageMemberAuthenticationBeanLocal {
    
    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Customer customer;
    
    @Override
    public List<Country> getCountries() {
        Query q = em.createNamedQuery("getAllCountry");
        return q.getResultList();
    }    
    
    @Override
    public Customer authenticate(String emailAddress, String password){
        System.out.println("Authenticating customer...");
        Customer customer = null;
        Query query = em.createQuery("SELECT c FROM Customer c where c.emailAddress=:email");
        query.setParameter("email", emailAddress);
        try{
            customer = (Customer) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){
            System.out.println("No such customer");
            return null;
        }
        String correctPassword = customer.getPassword();
        String hashedPassword = SHA1Hash(customer.getSalt() + password);
        if (!correctPassword.equals(hashedPassword)){
            System.out.println("Wrong password");
            return null;
        }else{
            customer.setLastLogon(new Date());
            System.out.println("Logged in at ejb");
            return customer;
        }
    }
    
    @Override
    public Long createCustomerAccount(String emailAddress, String password, String name, String phoneNo, String address, String dateOfBirth, Country country) {
        Customer customer = new Customer();
        customer.setActive(Boolean.TRUE);
        //generate salt
        String salt = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        customer.setSalt(salt);
        customer.setEmailAddress(emailAddress);
        customer.setPassword(password);
        customer.setName(name);
        customer.setPhoneNo(phoneNo);
        customer.setAddress(address);
        customer.setDateOfBirth(dateOfBirth);
        customer.setCumulativePoints(0);
        customer.setCurrentPoints(0);
        customer.setCountry(country);
        em.persist(customer);
        em.flush();
        try {
            SendEmailByPost.sendEmail("customerservices", customer.getEmailAddress(), "Account created", "Your new account has been created.");
        } catch (Exception ex) {
            Logger.getLogger(ManageMemberAuthenticationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customer.getId();
    }
    
    @Override
    public Long createCustomerAccountNoEmail(String emailAddress, String password, String name, String phoneNo, String address, String dateOfBirth, Country country) {
        Customer customer = new Customer();
        customer.setActive(Boolean.TRUE);
        //generate salt
        String salt = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        customer.setSalt(salt);
        customer.setEmailAddress(emailAddress);
        customer.setPassword(password);
        customer.setName(name);
        customer.setPhoneNo(phoneNo);
        customer.setAddress(address);
        customer.setDateOfBirth(dateOfBirth);
        customer.setCumulativePoints(0);
        customer.setCurrentPoints(0);
        customer.setCountry(country);
        em.persist(customer);
        em.flush();
        return customer.getId();
    }
    
    @Override
    public boolean forgotPassword(String emailAddress, String dateOfBirth) {
        Customer customer;
        try{
            Query query = em.createQuery("SELECT c FROM Customer c WHERE c.emailAddress=:email");
            query.setParameter("email", emailAddress);
            customer = (Customer) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){
            return false;
        }
        if (!customer.getDateOfBirth().equals(dateOfBirth)){
            return false;
        }
        String forgottenPasswordCode = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        customer.setForgottenPasswordCode(forgottenPasswordCode);
        try {
            SendEmailByPost.sendEmail("techsupport", emailAddress, "Password Reset Request", "Click this link to reset your password: https://localhost/cws/sg/resetpassword.xhtml?code="+forgottenPasswordCode);
        } catch (Exception ex) {
            Logger.getLogger(ManageMemberAuthenticationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean resetPassword(String code, String password){
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.forgottenPasswordCode=:code");
        query.setParameter("code", code);
        Customer customer;
        try{
            customer = (Customer) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){
            return false;
        }
        customer.setPassword(password);
        customer.setForgottenPasswordCode(null);
        return true;
    }
    
    @Override
    public Customer getCustomerFromLoyaltyCardId(String loyaltyCardId){
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.loyaltyCardId=:id");
        query.setParameter("id", loyaltyCardId);
        return (Customer) query.getSingleResult();
    }
    
    @Override
    public void setCustomerLoyaltyCardId(Customer customer, String loyaltyCardId){
        customer.setLoyaltyCardId(loyaltyCardId);
    }
    
    @Override
    public Customer getCustomer(String emailAddress){
        Query query = em.createQuery("FROM Customer s where s.emailAddress=:emailAddress");
        query.setParameter("emailAddress", emailAddress);
        return (Customer) query.getSingleResult();
    }    
    
    @Override
    public void modifyPersonalParticulars(String emailAddress, String phoneNo, String name, String address, Long countryId){
        Query query = em.createQuery("SELECT c FROM Country c WHERE c.id=:id");
        query.setParameter("id", countryId);
        Country country = (Country)query.getSingleResult();
        customer = getCustomer(emailAddress);
        customer.setPhoneNo(phoneNo);
        customer.setName(name);
        customer.setAddress(address);
        customer.setCountry(country);
    }
    
    @Override
    public void setCustomerMembershipTier(Customer customer, String membershipTier){
        Query query = em.createQuery("SELECT m FROM MembershipTier m WHERE m.title=:title");
        query.setParameter("title", membershipTier);
        MembershipTier m = (MembershipTier) query.getSingleResult();
        customer.setMembershipTier(m);
    }
    
    @Override
    public void changePassword(String emailAddress, String newPassword){
        customer = getCustomer(emailAddress);
        customer.setPassword(newPassword);
    }
    
    @Override
    public void removeCustomerAccount(String emailAddress) {
        customer = getCustomer(emailAddress);
        customer.setActive(Boolean.FALSE);
        customer.setEmailAddress(null);
        customer.setPassword("");
        customer.setName("");
        customer.setDateOfBirth("");
        customer.setAddress("");
        customer.setPhoneNo("");
        customer.setLastLogon(null);
        customer.setMembershipTier(null);
        customer.setLoyaltyCardId(null);
    }
}
