/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockUnit;
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
public class ManageInventoryMovement implements ManageInventoryMovementLocal  {

    @PersistenceContext
    EntityManager em;

    private Stock stock;
    private StockUnit stockUnit;
    private StorageBin storageBin;
    private StorageArea storageArea;


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
    public StockUnit getStockUnit(Long stockUnitId) {
        stockUnit = (StockUnit) em.find(StockUnit.class, stockUnitId);
        return stockUnit;
    }

   
    @Override
    public Stock getStock(Long stockId) {
        stock = (Stock) em.find(Stock.class, stockId);
        return stock;
    }

   
    @Override
    public void createStockUnit(Stock stock, Long batchNo, Long quantity, StorageBin storageBin) {
        stockUnit = new StockUnit();
        stockUnit.setStock(stock);
        stockUnit.setBatchNo(batchNo);
        stockUnit.setQty(quantity);
        stockUnit.setLocation(storageBin);
        em.persist(stockUnit);
        em.flush();
    }
  
    @Override
    public void editStockUnitLocationDefault(Long stockUnitId, Long storageBinId) {
        stockUnit = getStockUnit(stockUnitId);
        storageBin = getStorageBin(storageBinId);
        stockUnit.setLocation(storageBin);
        em.merge(stockUnit);
        em.flush();
    }

   
    @Override
    public List<StockUnit> viewStockUnit(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.plant.id=" + plant.getId());
        return q.getResultList();
    }

 
    @Override
    public List<StorageBin> viewStorageBin(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }

}
