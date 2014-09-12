/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.StorageLocation;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author KamilulAshraf
 */
@Stateful
public class ManageStorageLocation implements ManageStorageLocationLocal  {

    @PersistenceContext
    EntityManager em;
    
    private StorageLocation storageLocation;
    
    
    //public void createStorageLocation (String rackNumber, Integer storageLevel, Double volume) {
   

 
    @Override
    public void createStorageLocation (String rackNumber) {
        storageLocation = new StorageLocation();
        storageLocation.setRackNumber(rackNumber);
        storageLocation.setStockUnits(null);
        /*
        storageLocation.setStockUnits(null);
        storageLocation.setRackNumber(rackNumber);
        storageLocation.setStorageLevel(storageLevel);
        storageLocation.setVolume(volume);
                */
        em.persist(storageLocation);
    }
    
   
    @Override
    public List<StorageLocation> viewStorageLocation() {
        Query q = em.createQuery("SELECT s " + "FROM StorageLocation s");
        return q.getResultList();
        
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
