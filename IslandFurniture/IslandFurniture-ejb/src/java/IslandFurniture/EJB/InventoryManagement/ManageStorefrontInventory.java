/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ReplenishmentTransferOrder;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.Entities.StorefrontInventory;
import IslandFurniture.Entities.StorefrontInventoryPK;
import IslandFurniture.Enums.TransferOrderStatus;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Kamilul Ashraf
 *
 */
@Stateful
public class ManageStorefrontInventory implements ManageStorefrontInventoryLocal {

    @PersistenceContext
    EntityManager em;

    private StorefrontInventory storefrontInventory;
    private StorefrontInventoryPK storefrontInventoryPK;
    private ReplenishmentTransferOrder replenishmentTransferOrder;

    @EJB
    public ManageInventoryTransferLocal transferBean;

//  Function: To add Storefront Inventory
    @Override
    public void createStorefrontInventory(Plant plant, Long stockId, int rQty, int mQty, Long storeSectionId) {
        storefrontInventory = new StorefrontInventory();
        storefrontInventory.setStore((Store) plant);
        storefrontInventory.setStock((Stock) em.find(Stock.class, stockId));
        storefrontInventory.setRepQty(rQty);
        storefrontInventory.setQty(0);
        storefrontInventory.setMaxQty(mQty);
        storefrontInventory.setLocationInStore((StoreSection) em.find(StoreSection.class, storeSectionId));
        em.persist(storefrontInventory);
        em.flush();
    }

//  Function: To edit Storefront Inventory
    @Override
    public void editStorefrontInventory(StorefrontInventory storefrontInventoryUpdated) {
        StorefrontInventoryPK pk = new StorefrontInventoryPK(storefrontInventoryUpdated.getStore().getId(), storefrontInventoryUpdated.getStock().getId());
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, pk);
        storefrontInventory.setRepQty(storefrontInventoryUpdated.getRepQty());
        storefrontInventory.setMaxQty(storefrontInventoryUpdated.getMaxQty());
        storefrontInventory.setLocationInStore((StoreSection) em.find(StoreSection.class, storefrontInventoryUpdated.getLocationInStore().getId()));
        em.merge(storefrontInventory);
        em.flush();
    }

//  Function: To delete Storefront Inventory
    @Override
    public void deleteStorefrontInventory(StorefrontInventory storefrontInventory) {
        storefrontInventoryPK = new StorefrontInventoryPK(storefrontInventory.getStore().getId(), storefrontInventory.getStock().getId());
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, storefrontInventoryPK);
        em.remove(storefrontInventory);
        em.flush();
    }

    //  Function: To display list of StorefrontInventory
    @Override
    public List<StorefrontInventory> viewStorefrontInventory(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorefrontInventory s WHERE s.store.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Function: To get Storefront Inventory Entity
    @Override
    public StorefrontInventory getStorefrontInventory(Plant plant, Long stockId) {
        storefrontInventoryPK = new StorefrontInventoryPK(plant.getId(), stockId);
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, storefrontInventoryPK);
        return storefrontInventory;
    }

    //  Function: To reduce Storefront Inventory from Transaction
    @Override
    public void reduceStorefrontInventoryFromTransaction(Plant plant, Stock stock, int qty) {
        // Start: Reduce Qty from Transaction : Current - Qty        
        storefrontInventoryPK = new StorefrontInventoryPK(plant.getId(), stock.getId());
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, storefrontInventoryPK);
        storefrontInventory.setQty(storefrontInventory.getQty() - qty);
        em.merge(storefrontInventory);
        em.flush();
        em.refresh(storefrontInventory);
        // End: Reduce Qty from Transaction : Current - Qty 

        // Start: If curr < replenishment, then create Replenishment Transfer Order
        if (storefrontInventory.getQty() < storefrontInventory.getRepQty()) {
            if (transferBean.checkIfReplenishmentTransferOrderforStockDoNotExists(plant, stock)) {
                replenishmentTransferOrder = new ReplenishmentTransferOrder();
                replenishmentTransferOrder.setRequestingPlant(plant);
                replenishmentTransferOrder.setStock(stock);
                replenishmentTransferOrder.setQty(storefrontInventory.getMaxQty() - storefrontInventory.getQty());
                replenishmentTransferOrder.setStatus(TransferOrderStatus.REQUESTED_PENDING);
                em.persist(replenishmentTransferOrder);
                em.flush();
            }
        }
        // End: If curr < replenishment, then create Replenishment Transfer Order 
    }
}
