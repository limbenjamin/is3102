/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanLocal;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ReplenishmentTransferOrder;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.Entities.StorefrontInventory;
import IslandFurniture.Entities.StorefrontInventoryPK;
import IslandFurniture.Enums.TransferOrderStatus;
import IslandFurniture.StaticClasses.SendSMSBean;
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
public class ManageStorefrontInventory implements ManageStorefrontInventoryLocal, ManageStorefrontInventoryRemote {

    @PersistenceContext
    EntityManager em;

    private StorefrontInventory storefrontInventory;
    private StorefrontInventoryPK storefrontInventoryPK;
    private ReplenishmentTransferOrder replenishmentTransferOrder;

    @EJB
    public ManageInventoryTransferLocal transferBean;
    @EJB
    private ManageNotificationsBeanLocal manageNotificationsBean;
    @EJB
    private ManagePrivilegesBeanLocal managePrivilegesBean;

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
    public void editStorefrontInventory(StorefrontInventory updatedStorefrontInventory, Long storeSectionId, int rep, int max) {
        StoreSection storeSection = (StoreSection) em.find(StoreSection.class, storeSectionId);
        StorefrontInventoryPK pk = new StorefrontInventoryPK(updatedStorefrontInventory.getStore().getId(), updatedStorefrontInventory.getStock().getId());
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, pk);

        System.out.println("3. SI is: " + storeSection);
        storefrontInventory.setRepQty(rep);
        storefrontInventory.setMaxQty(max);
        storefrontInventory.setLocationInStore(storeSection);
        em.merge(storefrontInventory);
        em.flush();
        em.refresh(storefrontInventory);
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

//  Function: To edit Storefront Inventory quantity
    @Override
    public void editStorefrontInventoryQty(StorefrontInventory si, int qty) {
        storefrontInventoryPK = new StorefrontInventoryPK(si.getStore().getId(), si.getStock().getId());
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, storefrontInventoryPK);
        storefrontInventory.setQty(qty);
        em.merge(storefrontInventory);
        em.flush();
    }

    //  Function: To reduce Storefront Inventory from Transaction
    @Override
    public void reduceStockfrontInventoryFromTransaction(Plant plant, Stock stock, int qty) {
        // Start: Reduce Qty from Transaction : Current - Qty        
        storefrontInventoryPK = new StorefrontInventoryPK(plant.getId(), stock.getId());
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, storefrontInventoryPK);
        storefrontInventory.setQty(storefrontInventory.getQty() - qty);
        em.merge(storefrontInventory);
        em.flush();
        em.refresh(storefrontInventory);

        // End: Reduce Qty from Transaction : Current - Qty 
        // Start: If curr < replenishment, then create Replenishment Transfer Order
        storefrontInventoryPK = new StorefrontInventoryPK(plant.getId(), stock.getId());
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, storefrontInventoryPK);

        if (storefrontInventory.getQty() < storefrontInventory.getRepQty()) {
            if (transferBean.checkIfReplenishmentTransferOrderforStockDoNotExists(plant, stock)) {
                replenishmentTransferOrder = new ReplenishmentTransferOrder();
                replenishmentTransferOrder.setRequestingPlant(plant);
                replenishmentTransferOrder.setStock(stock);
                replenishmentTransferOrder.setQty(storefrontInventory.getMaxQty() - storefrontInventory.getQty());
                replenishmentTransferOrder.setStatus(TransferOrderStatus.REQUESTED);
                em.persist(replenishmentTransferOrder);
                em.flush();
                manageNotificationsBean.createNewNotificationForPrivilegeFromPlant("Pending Replenishment", "New replenishment transfer order", "/inventorymgt/inventorytransfer_replenish.xhtml", "Fulfill Replenishment", managePrivilegesBean.getPrivilegeFromName("Inventory Replenishment"), plant);
                SendSMSBean.sendSMSByPrivilegeFromPlant("New Action: Pending Replenishment for " + stock.getName() + " at " + storefrontInventory.getLocationInStore().getName(), managePrivilegesBean.getPrivilegeFromName("Inventory Replenishment"), plant);
            }
        }
        // End: If curr < replenishment, then create Replenishment Transfer Order 
    }

    //  Function: To the Stock Level of a Stock stored in a Plant
    @Override
    public String viewStorefrontInventoryStockLevelPerPlant(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM StorefrontInventory s WHERE s.store.id=:plantId AND s.stock.id=:stockId");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        System.out.println("Stock is " + stock.getName());
        System.out.println("Plant is " + plant.getName());
        try {
            storefrontInventory = (StorefrontInventory) q.getResultList().get(0);
        } catch (Exception e) {
            return "Out of Stock";
        }

        Query t = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId AND s.stock.id=:stockId AND s.available=TRUE AND s.goodsIssuedDocument=NULL");
        t.setParameter("plantId", plant.getId());
        t.setParameter("stockId", stock.getId());
        List<StockUnit> stockUnitList = t.getResultList();

        Integer stockUnitQty = 0;
        for (StockUnit s : stockUnitList) {
            stockUnitQty = stockUnitQty + s.getQty().intValue();
        }

        if (stockUnitQty + storefrontInventory.getQty() == 0) {
            return "Out of Stock";
        } else if (stockUnitQty + storefrontInventory.getQty() < storefrontInventory.getRepQty()) {
            return "Selling Fast";
        } else {
            return "Stock Available";
        }
    }

    //  Function: To return the Qty of StorefrontInventory
    @Override
    public Integer viewStorefrontInventoryStockQty(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM StorefrontInventory s WHERE s.store.id=:plantId AND s.stock.id=:stockId");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());

        List<StorefrontInventory> sfInvList = (List<StorefrontInventory>) q.getResultList();
        if (sfInvList != null && !sfInvList.isEmpty()) {
            storefrontInventory = sfInvList.get(0);
            return storefrontInventory.getQty();
        }else{
            return 0;
        }
    }

    //  Function: To return the Qty of StockUnit
    @Override
    public Integer viewStockUnitStockQty(Plant plant, Stock stock) {
        Query t = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId AND s.stock.id=:stockId AND s.available=TRUE AND s.goodsIssuedDocument=NULL");
        t.setParameter("plantId", plant.getId());
        t.setParameter("stockId", stock.getId());
        List<StockUnit> stockUnitList = t.getResultList();

        Integer stockUnitQty = 0;
        for (StockUnit s : stockUnitList) {
            stockUnitQty = stockUnitQty + s.getQty().intValue();
        }

        return stockUnitQty;
    }
}
