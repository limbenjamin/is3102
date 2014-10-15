/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.Enums.StorageAreaType;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageStorageLocationLocal {

    //  Function: To check if there is any Storage Area with same name - to not allow duplicates
    boolean checkIfNoStorageAreaWithSameName(Plant plant, String name);

    //  Function: To check if there is any Storage Bin with same name - to not allow duplicates
    boolean checkIfNoStorageBinWithSameName(Plant plant, Long areaId, String binName);

    //  Function: To add Storage Area
    void createStorageArea(Plant plant, String name, String typeName);

    //  Function: To add Storage Bin
    void createStorageBin(StorageArea storageArea, String name);

    //  Function: To delete Storage Area
    void deleteStorageArea(Long storageAreaId);

    //  Function: To delete Storage Bin
    void deleteStorageBin(Long storageBinId);

    //  Function: To edit Storage Area
    void editStorageArea(Long storageAreaId, String name, String typeName);

    //  Function: To edit Storage Bin
    void editStorageBin(Long storageAreaId, Long storageBinId, String name);

    //  Function: To get StorageArea entity based on StorageAreaId
    StorageArea getStorageArea(Long storageAreaId);

    //  Function: To get StorageBin entity based on StorageBinId
    StorageBin getStorageBin(Long storageBinId);

    //  Function: To view Storage Areas in the Plant
    List<StorageArea> viewStorageArea(Plant plant);

    //  Function: To view Storage Area Type
    List<StorageAreaType> viewStorageAreaType(Plant plant);

    //  Function: To view Storage Bins in the Plant
    List<StorageBin> viewStorageBin(Plant plant);

    //  Function: To view Storage Bins in Receiving Area only
    List<StorageBin> viewStorageBinsAtReceivingOnly(Plant plant);

    //  Function: To view Storage Bins in Shipping Area only
    List<StorageBin> viewStorageBinsAtShippingOnly(Plant plant);

    //  Function: To view Storage Bins in a particular Storage Area - For AJAX purposes
    List<StorageBin> viewStorageBinsOfAStorageArea(Long id);

    //  Function: To view Storage Bins in a particular Storage Area and a particular Stock - For AJAX purposes
    List<StorageBin> viewStorageBinsOfAStorageAreaOfAStock(Long id, Long stockId);

    //  Function: To view Storage Bins in a particular Stock - For AJAX purposes
    List<StorageBin> viewStorageBinsOfAStock(Plant plant, Long stockId);
    
}
