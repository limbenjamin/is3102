/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ReplenishmentTransferOrder;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageBin;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageInventoryTransferLocal {

    void confirmStockUnitMovement(Long stockUnitId);

    void createStockUnit(Stock stock, String batchNo, Long quantity, StorageBin storageBin);

    void createStockUnit2(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, GoodsIssuedDocument gid);

    void createStockUnitMovement1(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, StorageBin newStorageBin);

    void deleteStockUnit(Long stockUnitId);

    void editStockUnitLocationDefault(Long stockUnitId, Long storageBinId);

    void editStockUnitQuantity(Long stockUnitId, Long qty);

    Stock getStock(Long stockId);

    StockUnit getStockUnit(Long stockUnitId);

    void updateBatchNumber(Long id, String batchNumber);

    List<Stock> viewStock();

    List<StockUnit> viewStockUnit(Plant plant);

    List<StockUnit> viewStockUnitByStockId(Long id, Long storageBinId);

    List<StockUnit> viewStockUnitByStorageBin(Plant plant, StorageBin storageBin);

    List<StockUnit> viewStockUnitDistinctName(Plant plant);

    List<StockUnit> viewStockUnitMovement(Plant plant, Stock stock);

    List<StockUnit> viewStockUnitMovementAll(Plant plant);

    List<StockUnit> viewStockUnitMovementCheck(StorageBin storageBin, Stock stock, String batchNo) throws Exception;

    List<StockUnit> viewStockUnitMovementbyStorageBin(StorageBin storageBin);

    List<StockUnit> viewStockUnitsOfAStock(Plant plant, Stock stock);

    List<StorageBin> viewStorageBin(Plant plant);

//  Function: To display list of Replenishment Transfer Order (Requested)    
    List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderRequested(Plant plant);

//  Function: To display list of Replenishment Transfer Order (Fulfilled)    
    List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderFulfilled(Plant plant);

//  Function: To display list of Replenishment Transfer Order (Requested) -- For a particular Stock  
    List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderRequestedForAParticularStock(Plant plant, Stock stock);
    
//  Function: To create a Replenishment Transfer Order (Status: Requested)
    void createReplenishmentTransferOrder(Plant plant, Stock stock, Integer quantity);

//  Function: To delete a Replenishment Transfer Order
    void deleteReplenishmentTransferOrder(Long id);
    
//  Function: To edit the Quantity of a Replenishment Transfer Order (Requested)
    void editReplenishmentTransferOrderQuantity(Long id, Integer qty);
}
