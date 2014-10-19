/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import IslandFurniture.Entities.Stock;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManagePOSLocal {

    int getVoucher(String id);

    int getReceipt(String receiptId);
    
    void useVoucher(String voucherId);
    
    void linkReceipt(String receipt,FurnitureTransaction ft);
    
    Stock getStock(Long id);
    
    void persistFTD(FurnitureTransactionDetail ftd);
    
    void persistFT(FurnitureTransaction ft);
    
}
