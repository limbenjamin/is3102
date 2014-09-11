/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import javax.ejb.Remote;

/**
 *
 * @author KamilulAshraf
 */
@Remote
public interface ManageStorageLocationRemote {
     public void createStorageLocation (String rackNumber);
     //  void createStorageLocation (String rackNumber, Integer storageLevel, Double volume);
}
