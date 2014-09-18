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

/**
 *
 * @author Zee
 */
public interface ManagePurchaseOrderLocal {

    void createPurchaseOrder(Plant plant, Calendar orderDate, Supplier supplier);

    void createPurchaseOrderDetail(Long poId, Long psId);

    void deletePurchaseOrder(Long poId);

    void deletePurchaseOrderDetail(Long podId);

    void editPurchaseOrder(Long poId, Plant plant, Calendar orderDate, Supplier supplier);

    void editPurchaseOrderDetail(Long podId, Long psId, Integer qty);

    ProcuredStock getProcuredStock(Long id);

    PurchaseOrder getPurchaseOrder(Long id);

    PurchaseOrderDetail getPurchaseOrderDetail(Long id);

    List<ProcuredStock> viewProcuredStocks();
    
    List<Plant> viewPlants();
    
    List<Supplier> viewSuppliers();

    List<PurchaseOrderDetail> viewPurchaseOrderDetails();

    List<PurchaseOrder> viewPurchaseOrders();
    
}
