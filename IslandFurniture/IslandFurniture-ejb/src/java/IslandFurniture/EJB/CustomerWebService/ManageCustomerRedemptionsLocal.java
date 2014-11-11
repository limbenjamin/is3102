/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Redemption;
import java.util.List;

/**
 *
 * @author Zee
 */
public interface ManageCustomerRedemptionsLocal {

    List<Redemption> getRedemptions(Customer customer);
    
}
