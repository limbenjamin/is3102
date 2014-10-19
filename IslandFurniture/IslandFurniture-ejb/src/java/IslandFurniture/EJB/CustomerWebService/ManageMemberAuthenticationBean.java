/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.MembershipTier;
import static IslandFurniture.Entities.Staff.SHA1Hash;
import IslandFurniture.StaticClasses.SendEmailByPost;
import java.util.Date;
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
    public Customer authenticate(String emailAddress, String password){
        Customer customer = null;
        Query query = em.createQuery("SELECT c FROM Customer c where c.emailAddress=:email");
        query.setParameter("email", emailAddress);
        try{
            customer = (Customer) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){
            return null;
        }
        String correctPassword = customer.getPassword();
        String hashedPassword = SHA1Hash(customer.getSalt() + password);
        if (!correctPassword.equals(hashedPassword)){
            return null;
        }else{
            customer.setLastLogon(new Date());
            return customer;
        }
    }
    
    @Override
    public Long createCustomerAccount(String emailAddress, String password, String name, String phoneNo, String address, String dateOfBirth) {
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
    public void setCustomerMembershipTier(Customer customer, String membershipTier){
        Query query = em.createQuery("SELECT m FROM MembershipTier m WHERE m.title=:title");
        query.setParameter("title", membershipTier);
        MembershipTier mt = (MembershipTier) query.getSingleResult();
        customer.setMembershipTier(mt);
        mt.getMembers().add(customer);
    }
    
    @Override
    public Customer getCustomer(String emailAddress){
        Query query = em.createQuery("FROM Customer s where s.emailAddress=:emailAddress");
        query.setParameter("emailAddress", emailAddress);
        return (Customer) query.getSingleResult();
    }    
    
    @Override
    public void modifyPersonalParticulars(String emailAddress, String phoneNo, String name){
        customer = getCustomer(emailAddress);
        customer.setPhoneNo(phoneNo);
        customer.setName(name);
    }
    
    
    
}
