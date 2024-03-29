/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.MembershipTier;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KamilulAshraf
 */
@Local
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

    //  Function: To check if there is no Membership Tier with the Same Title
    boolean checkIfNoMembershipTierSamePoints(Integer points);
    
    //  Function: To check if there is no Membership Tier with the Same Title
    boolean checkIfNoMembershipTierSameName(String name);

    //  Function: To check if there is no Customers in the Tier
    boolean checkIfNoCustomersInTheTier(MembershipTier mt);

    // Function: To get Customer with customerID
    public Customer getCustomer(Long customerID);
    
     //  Function: Get Customer from Card
    public Customer getCustomerByCard(String cardID);

    // Function: Edit a Customer's Points after Redemption
    void editCustomerAccountPoints(Customer customerUpdated, int points);
    
    // Function: To check if the member is eligible for promotion, and if yes, promote
    String checkMembershipUpgrade (Long customerID, Long transactionID);
    
    // Function: To check if the member is eligible for promotion, and if yes, promote [AT POS]
    void checkMembershipUpgradeATPOS(Long customerID, Long transactionID);

    public List<Customer> searchMemberByEmail(String email);

    public MembershipTier promoteCustomer(Customer c);

}
