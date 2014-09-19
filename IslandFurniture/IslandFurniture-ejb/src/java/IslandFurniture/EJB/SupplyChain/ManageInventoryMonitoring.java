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
    public List<StockUnit> viewStockUnit(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StorageBin> viewStorageBin(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }

}
