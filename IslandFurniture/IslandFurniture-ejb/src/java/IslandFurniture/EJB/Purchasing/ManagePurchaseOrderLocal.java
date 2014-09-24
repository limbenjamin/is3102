/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Purchasing;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
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
    
    PurchaseOrder createNewPurchaseOrder(String status, Supplier supplier, Long plantId, Calendar orderDate);

    void createNewPurchaseOrderDetail(Long poId, Long stockId, int quantity);

    void deletePurchaseOrder(Long poId);

    void deletePurchaseOrderDetail(Long podId);

    void editPurchaseOrder(Long poId, Long plantId, Calendar orderDate, String status);
    
    void updatePurchaseOrder(Long poId, String status, Calendar orderDate);    

    void updatePurchaseOrderDetail(PurchaseOrderDetail pod, Long psId, Integer qty);

    ProcuredStock getProcuredStock(Long id);

    PurchaseOrder getPurchaseOrder(Long id);

    PurchaseOrderDetail getPurchaseOrderDetail(Long id);
    
    Long getPlantOfOrder(Long orderId);

    List<ProcuredStock> viewProcuredStocks();
    
    List<Plant> viewPlants();
    
    List<Supplier> viewSuppliers();
    
    List<Supplier> viewContractedSuppliers(ManufacturingFacility mf);

    List<PurchaseOrderDetail> viewPurchaseOrderDetails(Long orderId);
    
    List<ProcuredStock> viewSupplierProcuredStocks(Long orderId, ManufacturingFacility mf);

    List<PurchaseOrder> viewPurchaseOrders();
    
    List<PurchaseOrder> viewPlannedPurchaseOrders();
    
    List<PurchaseOrder> viewConfirmedPurchaseOrders();    
}
