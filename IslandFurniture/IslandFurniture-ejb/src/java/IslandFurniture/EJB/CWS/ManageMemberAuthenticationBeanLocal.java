/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CWS;

import IslandFurniture.Entities.Customer;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageMemberAuthenticationBeanLocal {

    Customer authenticate(String emailAddress, String password);

    Long createCustomerAccount(String emailAddress, String password, String name, String phoneNo, String address, String dateOfBirth);
    
}
