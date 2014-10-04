/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
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
    
    List<StorageBin> viewStorageBinExcludeTheBin(Long id, Long currentId);
    
    List<StorageArea> viewStorageAreaReceiving(Plant plant);
    
    List<StockUnit> viewStockUnitbyStock(Stock stock);
    
    List<StockUnit> viewStockUnitAll(Plant plant);

}
