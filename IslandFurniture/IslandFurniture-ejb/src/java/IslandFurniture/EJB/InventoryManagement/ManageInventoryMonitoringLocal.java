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

/**
 *
 * @author KamilulAshraf
 */
public interface ManageInventoryMonitoringLocal {

    //  Function: To edit Stock Unit Quantity during Stock Take
    void editStockUnitQuantity(Long stockUnitId, Long qty);

    //  Function: To display distinct Stock Units that with status 'Available' -- For selection at Dropdown Menu
    List<StockUnit> viewStockUnit(Plant plant);

    //  Function: To display all Stock Units, regardless of Status -- For Inventory Report
    List<StockUnit> viewStockUnitAll(Plant plant);

    //  Function: To display Stock Units of a particular Stock
    List<StockUnit> viewStockUnitbyStock(Stock stock);

    //  Function: To display Stock Units stored in a particular Storage Bin
    List<StockUnit> viewStockUnitsInStorageBin(StorageBin storageBin);
    
}
