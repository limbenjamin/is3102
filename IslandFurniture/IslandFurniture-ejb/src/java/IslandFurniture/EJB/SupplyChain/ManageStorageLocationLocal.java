/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.StorageLocation;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageStorageLocationLocal {

    void createStorageLocation(String rackNumber);

    List<StorageLocation> viewStorageLocation();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
