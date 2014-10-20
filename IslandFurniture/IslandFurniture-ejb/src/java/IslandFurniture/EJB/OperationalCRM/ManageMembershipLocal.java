/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.MembershipTier;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageMembershipLocal {

    // Function: Create a New Customer
    void createCustomerAccount(String emailAddress, String password, String name, String phoneNo, String address, String dateOfBirth);

    // Function: Create Membership Tier
    void createMembershipTier(String title, Integer points);

    // Function: Delete Membership Tier
    void deleteMembershipTier(MembershipTier updatedMembershipTier);

    // Function: Edit a Customer
    void editCustomerAccount(Customer customerUpdated);

    // Function: Edit Membership Tier
    void editMembershipTier(MembershipTier updatedMembershipTier);

    // Function: View the All the Customers
    List<Customer> viewCustomers();

    //  Function: View list of Membership Tier
    List<MembershipTier> viewMembershipTier();
    
}
