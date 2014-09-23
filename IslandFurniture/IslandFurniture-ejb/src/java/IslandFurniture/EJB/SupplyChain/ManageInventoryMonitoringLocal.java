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

/**
 *
 * @author KamilulAshraf
 */
public interface ManageInventoryMonitoringLocal {

    List<StockUnit> viewStockUnit(Plant plant);

    List<StorageBin> viewStorageBin(Long id);

    StorageBin getStorageBin(Long storageBinId);

    Stock getStock(Long id);
    
    void editStockUnitQuantity(Long stockUnitId, Long qty);
    
    StockUnit getStockUnit(Long id);
    
    List<StockUnit> viewStockUnitBin(StorageBin storageBin);
    
    List<StorageArea> viewStorageArea(Plant plant);
    
    
    
    
}
