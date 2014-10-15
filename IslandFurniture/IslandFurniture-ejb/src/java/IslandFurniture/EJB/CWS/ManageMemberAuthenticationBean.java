/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CWS;

import IslandFurniture.Entities.Customer;
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
}
