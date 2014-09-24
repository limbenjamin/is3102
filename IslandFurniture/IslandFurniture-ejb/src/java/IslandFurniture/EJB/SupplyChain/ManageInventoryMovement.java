/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockUnit;
import IslandFurniture.EJB.Entities.StorageArea;
import IslandFurniture.EJB.Entities.StorageBin;
import java.util.Calendar;
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
public class ManageInventoryMovement implements ManageInventoryMovementLocal {

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
    public void updateBatchNumber(Long id, String batchNumber) {
        stockUnit = getStockUnit(id);
        stockUnit.setBatchNo(batchNumber);
        em.merge(stockUnit);
        em.flush();
    }

    @Override
    public void createStockUnit(Stock stock, String batchNo, Long quantity, StorageBin storageBin) {
        stockUnit = new StockUnit();
        stockUnit.setStock(stock);
        stockUnit.setBatchNo(batchNo);
        stockUnit.setQty(quantity);
        stockUnit.setLocation(storageBin);
        stockUnit.setAvailable(true);
        em.persist(stockUnit);
        em.flush();
    }

    @Override
    public void createStockUnit2(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, GoodsIssuedDocument gid) {
        stockUnit = new StockUnit();
        stockUnit.setStock(stock);
        stockUnit.setBatchNo(batchNo);
        stockUnit.setQty(quantity);
        stockUnit.setLocation(storageBin);
        stockUnit.setAvailable(false);
        stockUnit.setCommitStockUnitId(stockUnitId);
        stockUnit.setGoodsIssuedDocument(gid);
        em.persist(stockUnit);
        em.flush();
    }

    @Override
    public void createStockUnitMovement1(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, StorageBin newStorageBin) {
        stockUnit = new StockUnit();
        stockUnit.setStock(stock);
        stockUnit.setBatchNo(batchNo);
        stockUnit.setQty(quantity);
        stockUnit.setLocation(storageBin);
        stockUnit.setPendingLocation(newStorageBin);
        stockUnit.setAvailable(false);
        stockUnit.setCommitStockUnitId(stockUnitId);
        stockUnit.setGoodsIssuedDocument(null);
        em.persist(stockUnit);
        em.flush();
    }

    @Override
    public List<StockUnit> viewStockUnitMovement(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId AND s.stock.id=:stockId AND s.available=FALSE AND s.goodsIssuedDocument=NULL");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitMovementbyStorageBin(StorageBin storageBin) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.id=:id AND s.available=FALSE AND s.goodsIssuedDocument=NULL");
        q.setParameter("id", storageBin.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitMovementAll(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId AND s.available=FALSE AND s.goodsIssuedDocument=NULL");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public void confirmStockUnitMovement(Long stockUnitId) {
        stockUnit = getStockUnit(stockUnitId);
        stockUnit.setLocation(stockUnit.getPendingLocation());
        stockUnit.setPendingLocation(null);
        stockUnit.setAvailable(true);
        em.merge(stockUnit);
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
    public void deleteStockUnit(Long stockUnitId) {
        stockUnit = getStockUnit(stockUnitId);
        em.remove(stockUnit);
        em.flush();
    }

    @Override
    public void editStockUnitQuantity(Long stockUnitId, Long qty) {
        stockUnit = getStockUnit(stockUnitId);
        stockUnit.setQty(qty);
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

    @Override
    public List<StockUnit> viewStockUnitByStorageBin(Plant plant, StorageBin storageBin) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE (s.location.storageArea.plant.id=:plantId AND s.location.id=:storageBinId AND s.available=TRUE)");
        q.setParameter("plantId", plant.getId());
        q.setParameter("storageBinId", storageBin.getId());
        return q.getResultList();
    }

}
