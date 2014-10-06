/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageBin;
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

    private StockUnit stockUnit;

//  Function: To edit Stock Unit Quantity during Stock Take    
    @Override
    public void editStockUnitQuantity(Long stockUnitId, Long qty) {
        stockUnit = (StockUnit) em.find(StockUnit.class, stockUnitId);
        stockUnit.setQty(qty);
        em.merge(stockUnit);
        em.flush();
    }

//  Function: To display distinct Stock Units that with status 'Available' -- For selection at Dropdown Menu
    @Override
    public List<StockUnit> viewStockUnit(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE (s.location.storageArea.plant.id=:plantId AND s.available=TRUE) GROUP BY s.stock.name");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Function: To display all Stock Units, regardless of Status -- For Inventory Report    
    @Override
    public List<StockUnit> viewStockUnitAll(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Function: To display Stock Units of a particular Stock    
    @Override
    public List<StockUnit> viewStockUnitbyStock(Stock stock) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.stock.id=:stockId");
        q.setParameter("stockId", stock.getId());
        return q.getResultList();
    }

//  Function: To display Stock Units stored in a particular Storage Bin    
    @Override
    public List<StockUnit> viewStockUnitsInStorageBin(StorageBin storageBin) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE (s.available=TRUE AND s.location.id=:storageBinId)");
        q.setParameter("storageBinId", storageBin.getId());
        return q.getResultList();
    }

}


