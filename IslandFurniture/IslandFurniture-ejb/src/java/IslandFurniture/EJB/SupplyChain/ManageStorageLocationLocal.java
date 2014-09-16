/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.StorageBin;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageStorageLocationLocal {

    void createStorageLocation (Integer plantNumber, Integer storageAreaNumber, String storageAreaName, String storageID, String storageType, String storageDescription);
    void editStorageLocation (Long id, Integer plantNumber, Integer storageAreaNumber, String storageAreaName, String storageID, String storageType, String storageDescription);
    void deleteStorageLocation(Long id);
    
    List<StorageBin> viewStorageLocation();
    StorageBin getStorageLocation(Long id);
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
