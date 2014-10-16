/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.Entities.StorefrontInventory;
import IslandFurniture.Entities.StorefrontInventoryPK;
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
public class ManageStoreSection implements ManageStoreSectionLocal {

    @PersistenceContext
    EntityManager em;

    private StoreSection storeSection;
    private StorefrontInventory storefrontInventory;
    private StorefrontInventoryPK storefrontInventoryPK;

//  Function: To add Store Section
    @Override
    public void createStoreSection(Plant plant, int level, String name, String desc) {
        storeSection = new StoreSection();
        storeSection.setStore((Store) plant);
        storeSection.setStoreLevel(level);
        storeSection.setName(name);
        storeSection.setDescription(desc);
        em.persist(storeSection);
        em.flush();
    }

//  Function: To edit Store Section
    @Override
    public void editStoreSection(StoreSection storeSectionUpdated) {
        storeSection = (StoreSection) em.find(StoreSection.class, storeSectionUpdated.getId());
        storeSection.setStoreLevel(storeSectionUpdated.getStoreLevel());
        storeSection.setName(storeSectionUpdated.getName());
        storeSection.setDescription(storeSectionUpdated.getDescription());
        em.merge(storeSection);
        em.flush();
    }

//  Function: To delete Store Section
    @Override
    public void deleteStoreSection(StoreSection storeSection) {
        storeSection = (StoreSection) em.find(StoreSection.class, storeSection.getId());
        em.remove(storeSection);
        em.flush();
    }

    //  Function: To display list of Store Section
    @Override
    public List<StoreSection> viewStoreSectionList(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StoreSection s WHERE s.store.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    //  Function: To check if there is any Store Section with same name and level - to not allow duplicates    
    @Override
    public boolean checkIfNoStoreSectionWithSameNameAndLevel(Plant plant, String name, int level) {
        Query q = em.createQuery("SELECT s FROM StoreSection s WHERE s.store.id=:plantId AND s.name=:name AND s.storeLevel=:level");
        q.setParameter("plantId", plant.getId());
        q.setParameter("name", name);
        q.setParameter("level", level);
        return q.getResultList().isEmpty();
    }

    //  Function: To check if there is no Stock Inventory with a particular stock when deleting Store Section
    @Override
    public boolean checkIfNoStorefrontInventoryInThisStoreSection(StoreSection storeSection) {
        Query q = em.createQuery("SELECT s FROM StorefrontInventory s WHERE s.locationInStore.id=:id");
        q.setParameter("id", storeSection.getId());
        return q.getResultList().isEmpty();
    }

}
