/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.StockUnit;
import IslandFurniture.EJB.Entities.StorageBin;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageInventoryMonitoringLocal {

    List<StockUnit> viewStockUnit(Plant plant);

    List<StorageBin> viewStorageBin(Plant plant);
    
    StorageBin getStorageBin(Long storageBinId);
}
