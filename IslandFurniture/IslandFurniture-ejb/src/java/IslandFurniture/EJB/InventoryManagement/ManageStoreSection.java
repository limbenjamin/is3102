/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StoreSection;
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
}
