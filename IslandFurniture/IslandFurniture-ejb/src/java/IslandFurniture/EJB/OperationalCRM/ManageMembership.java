/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
public class ManageMembership implements ManageMembershipLocal {

    @PersistenceContext
    EntityManager em;

    Customer customer;

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
        em.persist(customer);
        em.flush();
    }

    // Function: Edit a Customer
    @Override
    public void editCustomerAccount(Customer customerUpdated) {
        customer = (Customer) em.find(Customer.class, customerUpdated.getId());
        customer.setEmailAddress(customerUpdated.getEmailAddress());
        customer.setName(customerUpdated.getName());
        customer.setPhoneNo(customerUpdated.getPhoneNo());
        customer.setAddress(customerUpdated.getAddress());
        customer.setDateOfBirth(customerUpdated.getDateOfBirth());
        em.merge(customer);
        em.flush();
    }

    // Function: View the All the Customers
    @Override
    public List<Customer> viewCustomers() {
        Query q = em.createQuery("SELECT * FROM Customer");
        return q.getResultList();
    }
}
