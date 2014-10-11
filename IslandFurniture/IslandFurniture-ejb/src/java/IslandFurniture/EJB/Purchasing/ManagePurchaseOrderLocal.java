/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Purchasing;

import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Zee
 */
@Local
public interface ManagePurchaseOrderLocal {

    ProcuredStockPurchaseOrder createNewPurchaseOrder(PurchaseOrderStatus status, ProcuredStockSupplier supplier, ManufacturingFacility mf, Plant shipsTo, Calendar orderDate);

    void createNewPurchaseOrderDetail(Long poId, Long stockId, int quantity) throws DuplicateEntryException;

    void deletePurchaseOrder(Long poId);

    void deletePurchaseOrderDetail(Long podId);

    void updatePurchaseOrder(Long poId, PurchaseOrderStatus status, Calendar orderDate);

    void updatePurchaseOrderDetail(ProcuredStockPurchaseOrderDetail pod);

    ProcuredStockPurchaseOrder getPurchaseOrder(Long id);

    List<ProcuredStockSupplier> viewContractedSuppliers(ManufacturingFacility mf);

    List<ProcuredStockPurchaseOrderDetail> viewPurchaseOrderDetails(Long orderId);
    
    Integer getLotSize(ProcuredStock stock, ManufacturingFacility mf);

    List<ProcuredStock> viewSupplierProcuredStocks(Long orderId, ManufacturingFacility mf);

    List<ProcuredStockPurchaseOrder> viewPlannedPurchaseOrders(Plant staffPlant);

    List<ProcuredStockPurchaseOrder> viewConfirmedPurchaseOrders(Plant staffPlant);
    
    List<ProcuredStockPurchaseOrder> viewDeliveredPurchaseOrders(Plant staffPlant);
    
    List<ProcuredStockPurchaseOrder> viewPaidPurchaseOrders(Plant staffPlant);
}
