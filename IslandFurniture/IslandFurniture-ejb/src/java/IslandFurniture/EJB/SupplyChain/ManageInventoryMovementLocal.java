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

/**
 *
 * @author KamilulAshraf
 */
public interface ManageInventoryMovementLocal {

    void createStockUnit(Stock stock, Long batchNo, Long quantity, StorageBin storageBin);

    void editStockUnitLocationDefault(Long stockUnitId, Long storageBinId);

    Stock getStock(Long stockId);

    StockUnit getStockUnit(Long stockUnitId);

    StorageArea getStorageArea(Long storageAreaId);

    StorageBin getStorageBin(Long storageBinId);

    List<StockUnit> viewStockUnit(Plant plant);

    List<StorageBin> viewStorageBin(Plant plant);

    void deleteStockUnit(Long stockUnitId);
    
    void deleteStockUnitQty(Long stockUnitId, Long qty);
    
    void createStockUnit2(Stock stock, Long stockUnitId, Long batchNo, Long quantity, StorageBin storageBin, Calendar commitTime, GoodsIssuedDocument gid);

}
