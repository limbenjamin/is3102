/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageInventoryMovementLocal {

    void createStockUnit(Stock stock, String batchNo, Long quantity, StorageBin storageBin);

    void createStockUnit2(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, GoodsIssuedDocument gid);

    void deleteStockUnit(Long stockUnitId);

    void editStockUnitLocationDefault(Long stockUnitId, Long storageBinId);

    Stock getStock(Long stockId);

    StockUnit getStockUnit(Long stockUnitId);

    StorageArea getStorageArea(Long storageAreaId);

    StorageBin getStorageBin(Long storageBinId);

    void editStockUnitQuantity(Long stockUnitId, Long qty);

    List<StockUnit> viewStockUnit(Plant plant);

    List<StorageBin> viewStorageBin(Plant plant);

    void createStockUnitMovement1(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, StorageBin newStorageBIn);

    List<StockUnit> viewStockUnitMovement(Plant plant, Stock stock);

    void confirmStockUnitMovement(Long stockUnitId);

    List<StockUnit> viewStockUnitMovementAll(Plant plant);

    List<StockUnit> viewStockUnitByStorageBin(Plant plant, StorageBin storageBin);

    List<StockUnit> viewStockUnitMovementbyStorageBin(StorageBin storageBin);
    
    void updateBatchNumber(Long id, String batchNumber);
    
//    StockUnit viewStockUnitMovementCheck(StorageBin storageBin, Stock stock, String batchNo) throws Exception;
    
    List<StockUnit> viewStockUnitMovementCheck(StorageBin storageBin, Stock stock, String batchNo) throws Exception;
    
    List<StockUnit> viewStockUnitByStockId(Long id, Long storageBinId);

}
