/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.RetailItemTransaction;
import IslandFurniture.Entities.RetailItemTransactionDetail;
import IslandFurniture.Entities.Transaction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kamilul
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

    // Function: To check if the member is eligible for promotion, and if yes, promote
    @Override
    public String checkMembershipUpgrade(Long customerID, Long transactionID) {
        Customer customer = (Customer) em.find(Customer.class, customerID);
        Transaction transaction = (Transaction) em.find(Transaction.class, transactionID);

        if (!transaction.equals(null)) {
            String newTier = "";
            Integer totalPoints = 0;
            Integer points = 0;

            if (transaction.getMember().equals(null)) {
                transaction.setMember(customer);

                if (transaction instanceof FurnitureTransaction) {
                    FurnitureTransaction furnitureTransaction = (FurnitureTransaction) transaction;
                    for (FurnitureTransactionDetail f : furnitureTransaction.getFurnitureTransactionDetails()) {
                        points = points + f.getTotalPoints().intValue();
                    }
                } else if (transaction instanceof RetailItemTransaction) {
                    RetailItemTransaction RetailItemTransaction = (RetailItemTransaction) transaction;
                    for (RetailItemTransactionDetail f : RetailItemTransaction.getRetailItemTransactionDetails()) {
                        points = points + f.getTotalPoints().intValue();
                    }
                } else {
                    RestaurantTransaction RestaurantTransaction = (RestaurantTransaction) transaction;
                    for (RestaurantTransactionDetail f : RestaurantTransaction.getRestaurantTransactionDetails()) {
                        points = points + f.getTotalPoints().intValue();
                    }
                }
            } else {
                return "exist";
            }

            totalPoints = points + customer.getCumulativePoints();
            customer.setCumulativePoints(totalPoints);
            customer.setCurrentPoints(points + customer.getCurrentPoints());
            List<MembershipTier> membershipTierList = viewMembershipTier();
            membershipTierList.sort(null);
            for (MembershipTier membershipTier : membershipTierList) {
                if (totalPoints > membershipTier.getPoints() || totalPoints == membershipTier.getPoints()) {
                    customer.setMembershipTier(membershipTier);
                    newTier = membershipTier.getTitle();
                }
            }

            em.merge(transaction);
            em.merge(customer);
            em.flush();

            return points.toString() + "," + totalPoints.toString() + "," + newTier;

        } else {
            return "fail";
        }
    }

    @Override
    public void checkMembershipUpgradeATPOS(Long customerID, Long transactionID) {
        Customer customer = (Customer) em.find(Customer.class, customerID);
        List<MembershipTier> membershipTierList = viewMembershipTier();
        membershipTierList.sort(null);
        for (MembershipTier membershipTier : membershipTierList) {
            if (customer.getCumulativePoints() > membershipTier.getPoints() || customer.getCumulativePoints() == membershipTier.getPoints()) {
                customer.setMembershipTier(membershipTier);
            }
        }

        em.merge(customer);
        em.flush();
    }

    // Function: Get Customer
    @Override
    public Customer getCustomer(Long customerID) {
        return em.find(Customer.class, customerID);
    }

    //  Function: Get Customer from Card
    @Override
    public Customer getCustomerByCard(String cardID) {
        try {
            Query q = em.createQuery("SELECT s FROM Customer s WHERE s.loyaltyCardId=:id");
            q.setParameter("id", cardID);
            return (Customer) q.getSingleResult();
        } catch (NoResultException NRE) {
            return null;
        }
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

    /* Testing Ground */
    @Override
    public List<Customer> searchMemberByEmail(String email) {
        try {
            System.out.println("ManageMembership.searchCustomerByEmail()");
            Query q = em.createQuery("SELECT a FROM Customer a WHERE a.emailAddress LIKE :email");
            q.setParameter("email", "%" + email + "%");

            List<Customer> results = (List<Customer>) q.getResultList();
            return results;
        } catch (NoResultException NRE) {
            return null;
        }
    }
}
