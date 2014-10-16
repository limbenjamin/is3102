/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Customer;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageMembershipLocal {

    // Function: Create a New Customer
    void createCustomerAccount(String emailAddress, String password, String name, String phoneNo, String address, String dateOfBirth);

    // Function: Edit a Customer
    void editCustomerAccount(Customer customerUpdated);

    // Function: View the All the Customers
    List<Customer> viewCustomers();

}
