/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.StorageBin;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Kamilul Ashraf
 *
 * List of Attributes for Storage Location
 *
 * 1. Plant Number - Integer Plant/Store Number (eg. 1, 2, 3)
 *
 * 2. Storage Area Number - Integer Area Number (eg. 1, 2.. )
 *
 * 3. Storage Area Name - String Area Name (eg. Staging Area, Receiving Area)
 *
 * 4. Storage ID - String Storage ID (eg. A12, B55, C42.. )
 *
 * 5. Storage Type - String Type of Storage (eg. Public or Private)
 *
 * 6. Storage Description - String Description of Storage Space (eg. Usually
 * store SKU 1234)
 *
 */
@Stateful
public class ManageStorageLocation implements ManageStorageLocationLocal {

    @PersistenceContext
    EntityManager em;

    private StorageBin storageLocation;

    @Override
    public StorageBin getStorageLocation(Long id) {
        storageLocation = (StorageBin) em.find(StorageBin.class, id);
        System.out.println("this is the getStorageLocation id "+ id);
        return storageLocation;
    }

    @Override
    public void createStorageLocation(Integer plantNumber, Integer storageAreaNumber, String storageAreaName, String storageID, String storageType, String storageDescription) {
        storageLocation = new StorageBin();
        storageLocation.setPlantNumber(plantNumber);
        storageLocation.setStorageAreaNumber(storageAreaNumber);
        storageLocation.setStorageAreaName(storageAreaName);
        storageLocation.setStorageID(storageID);
        storageLocation.setStorageType(storageType);
        storageLocation.setStorageDescription(storageDescription);
        storageLocation.setStockUnits(null);
        em.persist(storageLocation);
    }

    @Override
    public void editStorageLocation(Long id, Integer plantNumber, Integer storageAreaNumber, String storageAreaName, String storageID, String storageType, String storageDescription) {
        storageLocation = getStorageLocation(id);
        storageLocation.setPlantNumber(plantNumber);
        storageLocation.setStorageAreaNumber(storageAreaNumber);
        storageLocation.setStorageAreaName(storageAreaName);
        storageLocation.setStorageID(storageID);
        storageLocation.setStorageType(storageType);
        storageLocation.setStorageDescription(storageDescription);
        storageLocation.setStockUnits(null);
        em.merge(storageLocation);
        em.flush();
    }

    @Override
    public void deleteStorageLocation(Long id) {
        storageLocation = getStorageLocation(id);
        em.remove(storageLocation);
        em.flush();
    }

    @Override
    public List<StorageBin> viewStorageLocation() {
        Query q = em.createQuery("SELECT s " + "FROM StorageLocation s");
        return q.getResultList();

    }
    

    // Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method")
}
