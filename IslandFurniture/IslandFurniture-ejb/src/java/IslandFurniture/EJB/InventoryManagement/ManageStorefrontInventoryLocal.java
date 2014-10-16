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

    //  Function: To get Storefront Inventory Entity
    StorefrontInventory getStorefrontInventory(Plant plant, Long stockId);

    //  Function: To edit Storefront Inventory Quantity
    void editStorefrontInventoryQty(StorefrontInventory storefrontInventoryUpdated, int qty);

    //  Function: To check if current quantity is below replenishment levels
    boolean checkIfStorefrontInventoryCurrentQtyBelowReplenishmentQtyPlant(Plant plant, Long stockId);

}
