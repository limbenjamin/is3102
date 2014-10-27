/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StorefrontInventory;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KamilulAshraf
 */
@Local
public interface ManageStorefrontInventoryLocal {

    //  Function: To add Storefront Inventory
    void createStorefrontInventory(Plant plant, Long stockId, int rQty, int mQty, Long storeSectionId);

    //  Function: To delete Storefront Inventory
    void deleteStorefrontInventory(StorefrontInventory storefrontInventory);

    //  Function: To edit Storefront Inventory
    void editStorefrontInventory(StorefrontInventory storefrontInventoryUpdated, Long storeSectionId, int rep, int max);

    //  Function: To display list of StorefrontInventory
    List<StorefrontInventory> viewStorefrontInventory(Plant plant);

    //  Function: To get Storefront Inventory Entity
    StorefrontInventory getStorefrontInventory(Plant plant, Long stockId);

    //  Function: To reduce Storefront Inventory from Transaction
    void reduceStockfrontInventoryFromTransaction(Plant plant, Stock stock, int qty);
    
    //  Function: To edit Storefront Inventory quantity
    void editStorefrontInventoryQty(StorefrontInventory si, int qty);

}
