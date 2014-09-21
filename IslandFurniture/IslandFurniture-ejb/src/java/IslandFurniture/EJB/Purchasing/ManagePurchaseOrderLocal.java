/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Purchasing;

import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.PurchaseOrderDetail;
import IslandFurniture.EJB.Entities.Supplier;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;
/**
 *
 * @author Zee
 */
@Local
public interface ManagePurchaseOrderLocal {

    PurchaseOrder createPurchaseOrder(Calendar orderDate, String status);

    void createPurchaseOrderDetail(Long poId, Long psId);

    void deletePurchaseOrder(Long poId);

    void deletePurchaseOrderDetail(Long podId);

    void editPurchaseOrder(Long poId, Long plantId, Calendar orderDate, String status);

    void editPurchaseOrderDetail(Long podId, Long psId, Integer qty);

    ProcuredStock getProcuredStock(Long id);

    PurchaseOrder getPurchaseOrder(Long id);

    PurchaseOrderDetail getPurchaseOrderDetail(Long id);

    List<ProcuredStock> viewProcuredStocks();
    
    List<Plant> viewPlants();
    
    List<Supplier> viewSuppliers();

    List<PurchaseOrderDetail> viewPurchaseOrderDetails();

    List<PurchaseOrder> viewPurchaseOrders();
    
    List<PurchaseOrder> viewPlannedPurchaseOrders();
    
    List<PurchaseOrder> viewConfirmedPurchaseOrders();    
}
