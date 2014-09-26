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
    private Plant plant;

    @Override
    public StorageArea getStorageArea(Long storageAreaId) {
        storageArea = (StorageArea) em.find(StorageArea.class, storageAreaId);
        return storageArea;
    }

    @Override
    public StorageBin getStorageBin(Long storageBinId) {
        storageBin = (StorageBin) em.find(StorageBin.class, storageBinId);
        return storageBin;
    }

    @Override
    public void createStorageArea(Plant plant, String name) {
        storageArea = new StorageArea();
        storageArea.setPlant(plant);
        storageArea.setName(name);
        em.persist(storageArea);
        em.flush();
    }

    @Override
    public void createStorageBin(StorageArea storageArea, String name) {
        storageBin = new StorageBin();
        storageBin.setStorageArea(storageArea);
        storageBin.setName(name);
        em.persist(storageBin);
        em.flush();
    }

    @Override
    public void editStorageArea(Long storageAreaId, String name) {
        storageArea = getStorageArea(storageAreaId);
        storageArea.setName(name);
        em.merge(storageArea);
        em.flush();
    }

    @Override
    public void editStorageBin(Long storageAreaId, Long storageBinId, String name) {
        storageArea = getStorageArea(storageAreaId);
        storageBin = getStorageBin(storageBinId);
        storageBin.setStorageArea(storageArea);
        storageBin.setName(name);
        em.merge(storageBin);
        em.flush();
    }

    @Override
    public List<StorageArea> viewStorageArea(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageArea s WHERE s.plant.id=" + plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StorageArea> viewStorageAreaSameName(Plant plant, String name) {
        Query q = em.createQuery("SELECT s FROM StorageArea s WHERE s.plant.id=:plantId AND s.name=:name");
        q.setParameter("plantId", plant.getId());
        q.setParameter("name", name);
        return q.getResultList();
    }

    @Override
    public List<StorageBin> viewStorageBinSameName(Plant plant, Long areaId, String binName) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=:plantId AND s.storageArea.id=:areaId AND s.name=:binName");
        q.setParameter("plantId", plant.getId());
        q.setParameter("areaId", areaId);
        q.setParameter("binName", binName);
        return q.getResultList();
    }

    @Override
    public List<StorageBin> viewStorageBin(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }

    @Override
    public void deleteStorageArea(Long storageAreaId) {
        storageArea = getStorageArea(storageAreaId);
        em.remove(storageArea);
        em.flush();
    }

    @Override
    public void deleteStorageBin(Long storageBinId) {
        storageBin = getStorageBin(storageBinId);
        em.remove(storageBin);
        em.flush();
    }

}
