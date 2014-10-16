/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StoreSection;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageStoreSectionLocal {

    //  Function: To add Store Section
    void createStoreSection(Plant plant, int level, String name, String desc);

    //  Function: To delete Store Section
    void deleteStoreSection(StoreSection storeSection);

    //  Function: To edit Store Section
    void editStoreSection(StoreSection storeSectionUpdated);

    //  Function: To display list of Store Section
    List<StoreSection> viewStoreSectionList(Plant plant);

    //  Function: To check if there is any Storage Bin with same name - to not allow duplicates   
    boolean checkIfNoStoreSectionWithSameNameAndLevel(Plant plant, String name, int level);

    //  Function: To check if there is no Stock Inventory with a particular stock when deleting Store Section
    boolean checkIfNoStorefrontInventoryInThisStoreSection(StoreSection storeSection);

}
