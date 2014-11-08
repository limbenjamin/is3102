/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RetailItemTransaction;
import java.util.List;

/**
 *
 * @author Zee
 */
public interface ManageCustomerTransactionsLocal {

    List<FurnitureTransaction> getFurnitureTransactions(Customer customer);

    List<RestaurantTransaction> getRestaurantTransactions(Customer customer);

    List<RetailItemTransaction> getRetailTransactions(Customer customer);
    
}
