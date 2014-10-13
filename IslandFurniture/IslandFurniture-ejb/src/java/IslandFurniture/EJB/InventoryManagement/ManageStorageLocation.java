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
import static IslandFurniture.Enums.StorageAreaType.RECEIVING;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Kamilul Ashraf
 *
 */
@Stateful
public class ManageStorageLocation implements ManageStorageLocationLocal {

    @PersistenceContext
    EntityManager em;

    private StorageArea storageArea;
    private StorageBin storageBin;
    private StorageAreaType storageAreaType;

//  Function: To get StorageArea entity based on StorageAreaId
    @Override
    public StorageArea getStorageArea(Long storageAreaId) {
        storageArea = (StorageArea) em.find(StorageArea.class, storageAreaId);
        return storageArea;
    }

//  Function: To get StorageBin entity based on StorageBinId
    @Override
    public StorageBin getStorageBin(Long storageBinId) {
        storageBin = (StorageBin) em.find(StorageBin.class, storageBinId);
        return storageBin;
    }

//  Function: To add Storage Area
    @Override
    public void createStorageArea(Plant plant, String name, String typeName) {
        storageArea = new StorageArea();
        storageArea.setPlant(plant);
        storageArea.setName(name);
        
        switch (typeName) {
                case "RECEIVING":
                    storageArea.setType(StorageAreaType.RECEIVING);
                    break;
                case "SHIPPING":
                    storageArea.setType(StorageAreaType.SHIPPING);
                    break;
                case "STORAGE":
                    storageArea.setType(StorageAreaType.STORAGE);
                    break;
                case "STOREFRONT":
                    storageArea.setType(StorageAreaType.STOREFRONT);
                    break;
                case "HOLDING":
                    storageArea.setType(StorageAreaType.HOLDING);
                    break;
                case "PRODUCTION":
                    storageArea.setType(StorageAreaType.PRODUCTION);
                    break;
                default:
                    break;
            }
        
        em.persist(storageArea);
        em.flush();
    }

//  Function: To add Storage Bin
    @Override
    public void createStorageBin(StorageArea storageArea, String name) {
        storageBin = new StorageBin();
        storageBin.setStorageArea(storageArea);
        storageBin.setName(name);
        em.persist(storageBin);
        em.flush();
    }

//  Function: To edit Storage Area
    @Override
    public void editStorageArea(Long storageAreaId, String name, String typeName) {
        storageArea = getStorageArea(storageAreaId);
        storageArea.setName(name);
        
        switch (typeName) {
                case "RECEIVING":
                    storageArea.setType(StorageAreaType.RECEIVING);
                    break;
                case "SHIPPING":
                    storageArea.setType(StorageAreaType.SHIPPING);
                    break;
                case "STORAGE":
                    storageArea.setType(StorageAreaType.STORAGE);
                    break;
                case "STOREFRONT":
                    storageArea.setType(StorageAreaType.STOREFRONT);
                    break;
                case "HOLDING":
                    storageArea.setType(StorageAreaType.HOLDING);
                    break;
                case "PRODUCTION":
                    storageArea.setType(StorageAreaType.PRODUCTION);
                    break;
                default:
                    break;
            }
        
        em.merge(storageArea);
        em.flush();
    }

//  Function: To edit Storage Bin
    @Override
    public void editStorageBin(Long storageAreaId, Long storageBinId, String name) {
        storageArea = getStorageArea(storageAreaId);
        storageBin = getStorageBin(storageBinId);
        storageBin.setStorageArea(storageArea);
        storageBin.setName(name);
        em.merge(storageBin);
        em.flush();
    }

//  Function: To delete Storage Area
    @Override
    public void deleteStorageArea(Long storageAreaId) {
        storageArea = getStorageArea(storageAreaId);
        em.remove(storageArea);
        em.flush();
    }

//  Function: To delete Storage Bin
    @Override
    public void deleteStorageBin(Long storageBinId) {
        storageBin = getStorageBin(storageBinId);
        em.remove(storageBin);
        em.flush();
    }

//  Function: To view Storage Areas in the Plant
    @Override
    public List<StorageArea> viewStorageArea(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageArea s WHERE s.plant.id=" + plant.getId());
        return q.getResultList();
    }

//  Function: To view Storage Bins in the Plant
    @Override
    public List<StorageBin> viewStorageBin(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }

//  Function: To view Storage Bins in Receiving Area only    
    @Override
    public List<StorageBin> viewStorageBinsAtReceivingOnly(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=:plantId AND s.storageArea.type=:type");
        q.setParameter("plantId", plant.getId());
        q.setParameter("type", RECEIVING);
        return q.getResultList();
    }

//  Function: To view Storage Bins in a particular Storage Area - For AJAX purposes
    @Override
    public List<StorageBin> viewStorageBinsOfAStorageArea(Long id) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.id=:id");
        q.setParameter("id", id);
        return q.getResultList();
    }

//  Function: To view Storage Bins in a particular Storage Area and a particular Stock - For AJAX purposes 
    @Override
    public List<StorageBin> viewStorageBinsOfAStorageAreaOfAStock(Long id, Long stockId) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.id=:id AND s.stockUnits.stock.id=:stockId");
        q.setParameter("id", id);
        q.setParameter("stockId", stockId);
        return q.getResultList();
    }

//  Function: To view Storage Area Type
    @Override
    public List<StorageAreaType> viewStorageAreaType(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageAreaType s");
        return q.getResultList();
    }

//  Function: To check if there is any Storage Area with same name - to not allow duplicates    
    @Override
    public boolean checkIfNoStorageAreaWithSameName(Plant plant, String name) {
        Query q = em.createQuery("SELECT s FROM StorageArea s WHERE s.plant.id=:plantId AND s.name=:name");
        q.setParameter("plantId", plant.getId());
        q.setParameter("name", name);
        return q.getResultList().isEmpty();
    }

//  Function: To check if there is any Storage Bin with same name - to not allow duplicates   
    @Override
    public boolean checkIfNoStorageBinWithSameName(Plant plant, Long areaId, String binName) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=:plantId AND s.storageArea.id=:areaId AND s.name=:binName");
        q.setParameter("plantId", plant.getId());
        q.setParameter("areaId", areaId);
        q.setParameter("binName", binName);
        return q.getResultList().isEmpty();
    }

}
