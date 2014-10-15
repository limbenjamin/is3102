/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.Entities.StorefrontInventory;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageStorefrontInventoryLocal {

    //  Function: To add Storefront Inventory
    void createStorefrontInventory(Plant plant, Long stockId, int rQty, int mQty, Long storeSectionId);

    //  Function: To delete Storefront Inventory
    void deleteStorefrontInventory(StorefrontInventory storefrontInventory);

    //  Function: To edit Storefront Inventory
    void editStorefrontInventory(StorefrontInventory storefrontInventoryUpdated);

    //  Function: To display list of StorefrontInventory
    List<StorefrontInventory> viewStorefrontInventory(Plant plant);
    
}
