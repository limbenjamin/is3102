/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.ExternalTransferOrder;
import IslandFurniture.Entities.ExternalTransferOrderDetail;
import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ReplenishmentTransferOrder;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageBin;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageInventoryTransferLocal {

    void confirmStockUnitMovement(Long stockUnitId);

    //  Function: To create a External Transfer Order (Status: Requested)
    ExternalTransferOrder createExternalTransferOrder(Plant plant);

    //  Function: To create a Replenishment Transfer Order (Status: Requested)
    void createReplenishmentTransferOrder(Plant plant, Stock stock, Integer quantity);

    void createStockUnit(Stock stock, String batchNo, Long quantity, StorageBin storageBin);

    void createStockUnit2(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, GoodsIssuedDocument gid);

    void createStockUnitMovement1(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, StorageBin newStorageBin);

    //  Function: To delete a External Transfer Order
    void deleteExternaTransferOrder(Long id);

    //  Function: To delete a Replenishment Transfer Order
    void deleteReplenishmentTransferOrder(Long id);

    void deleteStockUnit(Long stockUnitId);

    //  Function: To edit the Quantity of a External Transfer Order Detail (Requested)
    void editExternalTransferOrderDetailQuantity(Long id, Integer qty);

    //  Function: To edit the Quantity of a Replenishment Transfer Order (Requested)
    void editReplenishmentTransferOrderQuantity(Long id, Integer qty);

    void editStockUnitLocationDefault(Long stockUnitId, Long storageBinId);

    void editStockUnitQuantity(Long stockUnitId, Long qty);

    Stock getStock(Long stockId);

    StockUnit getStockUnit(Long stockUnitId);

    void updateBatchNumber(Long id, String batchNumber);

    //  Need to edit this one!
    //  Function: To display list of External Transfer Order (Requested) -- For a particular Stock
    List<ExternalTransferOrder> viewExternalTransferOrderDetailRequestedForAParticularStock(Plant plant, Stock stock);

    //  Function: To display list of External Transfer Order (Fulfilled) Pending   
    List<ExternalTransferOrder> viewExternalTransferOrderFulfilledPending(Plant plant);

    //  Function: To display list of External Transfer Order (Fulfilled) Posted   
    List<ExternalTransferOrder> viewExternalTransferOrderFulfilledPosted(Plant plant);

    //  Function: To display list of External Transfer Order (Requested) Pending   
    List<ExternalTransferOrder> viewExternalTransferOrderRequestedPending(Plant plant);

    //  Function: To display list of External Transfer Order (Requested) Posted   
    List<ExternalTransferOrder> viewExternalTransferOrderRequestedPosted(Plant plant);

    //  Function: To display list of Replenishment Transfer Order (Fulfilled)
    List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderFulfilled(Plant plant);

    //  Function: To display list of Replenishment Transfer Order (Requested)
    List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderRequested(Plant plant);

    //  Function: To display list of Replenishment Transfer Order (Requested) -- For a particular Stock
    List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderRequestedForAParticularStock(Plant plant, Stock stock);

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

    //  Function: To obtain the ExternalTransferOrder entity;
    ExternalTransferOrder getExternalTransferOrder(Long id);

    //  Function: To display list of External Transfer Order Details in a External Transfer Order    
    List<ExternalTransferOrderDetail> viewExternalTransferOrderDetail(Long id);

    //  Function: To edit/add the quantity of a External Transfer Order Detail, when a same Stock is added to the External Transfer Order
    void editExternalTransferOrderDetailQtyWhenSameStockIdIsAdded(Long id, Integer qty);

    //  Function: To create External Transfer Order Detail    
    void createExternalTransferOrderDetail(Long id, Long stockId, Integer quantity);

    //  Function: To edit External Transfer Order Detail    
    void editExternalTransferOrderDetail(ExternalTransferOrderDetail externalTransferOrderDetail, Long stockId);

    //  Function: To delete a External Transfer Order Detail
    void deleteExternaTransferOrderDetail(ExternalTransferOrderDetail externalTransferOrderDetail);

    //  Function: To edit External Transfer Order  
    void editExternalTransferOrder(ExternalTransferOrder externalTransferOrder, Calendar cal);

    //  Function: To edit External Transfer Order Request to Posted  
    void editExternalTransferOrderStatusToRequestPosted(ExternalTransferOrder externalTransferOrder);

    //  Function: To edit External Transfer Order Request to Fulfilled 
    void editExternalTransferOrderStatusToRequestFulfilled(ExternalTransferOrder externalTransferOrder, Plant plant);

    //  Function: To obtain the ReplenishmentTransferOrder entity;
    ReplenishmentTransferOrder getReplenishmentTransferOrder(Long id);

    //  Function: To edit Replenishment Transfer Order Request to Fulfilled 
    void editReplenishmentTransferOrderStatusToRequestFulfilled(ReplenishmentTransferOrder replenishmentTransferOrder);

}
