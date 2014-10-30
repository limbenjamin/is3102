/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.MembershipTier;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kamilul Ashraf
 */
@Stateless
public class ManageMembership implements ManageMembershipLocal {

    @PersistenceContext
    EntityManager em;

    Customer customer;
    MembershipTier membershipTier;

    // Function: Create a New Customer
    @Override
    public void createCustomerAccount(String emailAddress, String password, String name, String phoneNo, String address, String dateOfBirth) {
        Customer customer = new Customer();
        customer.setActive(Boolean.TRUE);
        //generate salt
        String salt = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        customer.setSalt(salt);
        customer.setEmailAddress(emailAddress);
        customer.setPassword(null);
        customer.setName(name);
        customer.setPhoneNo(phoneNo);
        customer.setAddress(address);
        customer.setDateOfBirth(dateOfBirth);
        customer.setCumulativePoints(0);
        customer.setCurrentPoints(0);
        em.persist(customer);
        em.flush();
    }

    // Function: Edit a Customer
    @Override
    public void editCustomerAccount(Customer customerUpdated) {
        System.out.println("ManageMembership.editCustomerAccount()");
        customer = (Customer) em.find(Customer.class, customerUpdated.getId());
        customer.setEmailAddress(customerUpdated.getEmailAddress());
        customer.setName(customerUpdated.getName());
        customer.setPhoneNo(customerUpdated.getPhoneNo());
        customer.setAddress(customerUpdated.getAddress());
        customer.setDateOfBirth(customerUpdated.getDateOfBirth());
        customer.setLoyaltyCardId(customerUpdated.getLoyaltyCardId());
        em.merge(customer);
        em.flush();
    }
    
     // Function: Edit a Customer's Points after Redemption
    @Override
    public void editCustomerAccountPoints(Customer customerUpdated, int points) {
        System.out.println("ManageMembership.editCustomerAccount()");
        customer = (Customer) em.find(Customer.class, customerUpdated.getId());
        customer.setCurrentPoints(points);
        em.merge(customer);
        em.flush();
    }
    
    // Function: Get Customer
    @Override
    public Customer getCustomer(Long customerID) {
        return em.find(Customer.class, customerID);
    }

    // Function: View the All the Customers
    @Override
    public List<Customer> viewCustomers() {
        Query q = em.createQuery("SELECT s FROM Customer s");
        return q.getResultList();
    }

    // Function: Create Membership Tier
    @Override
    public void createMembershipTier(String title, Integer points) {
        MembershipTier membershipTier = new MembershipTier();
        membershipTier.setTitle(title);
        membershipTier.setPoints(points);
        em.persist(membershipTier);
        em.flush();
    }

    // Function: Edit Membership Tier
    @Override
    public void editMembershipTier(MembershipTier updatedMembershipTier) {
        membershipTier = (MembershipTier) em.find(MembershipTier.class, updatedMembershipTier.getId());
        membershipTier.setTitle(updatedMembershipTier.getTitle());
        membershipTier.setPoints(updatedMembershipTier.getPoints());
        em.merge(membershipTier);
        em.flush();
    }

    // Function: Delete Membership Tier
    @Override
    public void deleteMembershipTier(MembershipTier membershipTier) {
        membershipTier = (MembershipTier) em.find(MembershipTier.class, membershipTier.getId());
        em.remove(membershipTier);
        em.flush();

    }

    //  Function: View list of Membership Tier
    @Override
    public List<MembershipTier> viewMembershipTier() {
        Query q = em.createQuery("SELECT s FROM MembershipTier s");
        return q.getResultList();
    }

    //  Function: To check if there is no Membership Tier with the Same Title
    @Override
    public boolean checkIfNoMembershipTierSameName(String title) {
        Query q = em.createQuery("SELECT s FROM MembershipTier s WHERE s.title=:title");
        q.setParameter("title", title);
        return q.getResultList().isEmpty();
    }
    
    //  Function: To check if there is no Customers in the Tier
    @Override
    public boolean checkIfNoCustomersInTheTier(MembershipTier mt) {
        Query q = em.createQuery("SELECT s FROM Customer s WHERE s.membershipTier.id=:id");
        q.setParameter("id", mt.getId());
        return q.getResultList().isEmpty();
    }

}
