/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
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

    void editStorageBin(Long storageAreaId, Long storageBinId, String name);

    StorageArea getStorageArea(Long storageAreaId);

    StorageBin getStorageBin(Long storageBinId);

    List<StorageArea> viewStorageArea(Plant plant);

    List<StorageBin> viewStorageBin(Plant plant);

    List<StorageArea> viewStorageAreaSameName(Plant plant, String name);

    List<StorageBin> viewStorageBinSameName(Plant plant, Long areaId, String binName);

}
