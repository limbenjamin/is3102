/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.RetailItemTransaction;
import IslandFurniture.Entities.RetailItemTransactionDetail;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Transaction;
import java.util.List;
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
    
    void linkReceipt(String id, FurnitureTransaction ft);
    
    Stock getStock(Long id);
    
    void persistFTD(FurnitureTransactionDetail ftd);
    
    void persistFT(FurnitureTransaction ft);
    
    void persistRTD(RetailItemTransactionDetail rtd);
    
    void persistRT(RetailItemTransaction rt);

    void persistRST(RestaurantTransaction rt);

    void persistRSTD(RestaurantTransactionDetail rtd);

    List<ShoppingList> getShoppingListList(String customerCardId);

    void expendCoupon(String string);
    
}
