/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.StorageArea;
import IslandFurniture.EJB.Entities.StorageBin;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageStorageLocationLocal {

    void createStorageArea(Plant plant, String name);

    void createStorageBin(StorageArea storageArea, String name);

    void deleteStorageArea(Long storageAreaId);

    void deleteStorageBin(Long storageBinId);

    void editStorageArea(Long storageAreaId, String name);

    void editStorageBin(Long storageBinId, String name);

    StorageArea getStorageArea(Long storageAreaId);

    StorageBin getStorageBin(Long storageBinId);

    List<StorageArea> viewStorageArea();

    List<StorageBin> viewStorageBin();
    
}
