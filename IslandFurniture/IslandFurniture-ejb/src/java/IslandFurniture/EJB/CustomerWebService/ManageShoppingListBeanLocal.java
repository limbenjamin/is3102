/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.util.List;

/**
 *
 * @author Zee
 */
public interface ManageShoppingListBeanLocal {
    
    ShoppingList getShoppingList(Long id);
    
    ShoppingListDetail getShoppingListDetail(Long id);

    ShoppingList createShoppingList(String emailAddress, Long storeId, String name);
    
    void updateListTotalPrice (Long listId, Customer customer);

    void createShoppingListDetail(Long listId, Long stockId, int quantity, Double discountedPrice) throws DuplicateEntryException;

    void deleteShoppingList(Long listId, String emailAddress);

    void deleteShoppingListDetail(Long listId);

    Customer getCustomer(String emailAddress);

    List<ShoppingListDetail> getShoppingListDetails(Long listId);

    void updateShoppingList(Long listId, Store store, String name);

    void updateShoppingListDetail(ShoppingListDetail listDetail);
    
}
