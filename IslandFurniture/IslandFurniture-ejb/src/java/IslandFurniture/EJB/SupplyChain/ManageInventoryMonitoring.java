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
import IslandFurniture.EJB.Entities.StorageAreaType;
import static IslandFurniture.EJB.Entities.StorageAreaType.RECEIVING;
import IslandFurniture.EJB.Entities.StorageBin;
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
public class ManageInventoryMonitoring implements ManageInventoryMonitoringLocal {

    @PersistenceContext
    EntityManager em;

    private Stock stock;
    private StockUnit stockUnit;
    private StorageBin storageBin;
    private StorageArea storageArea;
    private Plant plant;

    @Override
    public StorageBin getStorageBin(Long storageBinId) {
        storageBin = (StorageBin) em.find(StorageBin.class, storageBinId);
        return storageBin;
    }

    @Override
    public Stock getStock(Long id) {
        stock = (Stock) em.find(Stock.class, id);
        return stock;
    }

    @Override
    public StockUnit getStockUnit(Long id) {
        stockUnit = (StockUnit) em.find(StockUnit.class, id);
        return stockUnit;
    }

    @Override
    public List<StockUnit> viewStockUnit(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE (s.location.storageArea.plant.id=:plantId AND s.available=TRUE) GROUP BY s.stock.name");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }
    
      @Override
    public List<StockUnit> viewStockUnitAll(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitbyStock(Stock stock) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.stock.id=:stockId");
        q.setParameter("stockId", stock.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitBin(StorageBin storageBin) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE (s.available=TRUE AND s.location.id=:storageBinId)");
        q.setParameter("storageBinId", storageBin.getId());
        return q.getResultList();
    }

    @Override
    public List<StorageBin> viewStorageBin(Long id) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.id=:id");
        q.setParameter("id", id);
        return q.getResultList();
    }

    @Override
    public List<StorageBin> viewStorageBinExcludeTheBin(Long id, Long currentId) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.id=:id AND s.id!=:currentId");
        q.setParameter("id", id);
        q.setParameter("currentId", currentId);
        return q.getResultList();
    }

    @Override
    public List<StorageArea> viewStorageArea(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageArea s WHERE s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StorageArea> viewStorageAreaReceiving(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageArea s WHERE s.plant.id=:plantId AND s.type=:type");
        q.setParameter("plantId", plant.getId());
        q.setParameter("type", RECEIVING);
        return q.getResultList();
    }

    @Override
    public void editStockUnitQuantity(Long stockUnitId, Long qty) {
        stockUnit = getStockUnit(stockUnitId);
        stockUnit.setQty(qty);
        em.merge(stockUnit);
        em.flush();
    }

}
