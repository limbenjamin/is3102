/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Purchasing;

import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Exceptions.DuplicateEntryException;
import IslandFurniture.Exceptions.InvalidStatusException;
import IslandFurniture.Exceptions.NoLotsException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Zee
 */
@Remote
public interface ManagePurchaseOrderRemote {

    ProcuredStockPurchaseOrder createNewPurchaseOrder(PurchaseOrderStatus status, ProcuredStockSupplier supplier, ManufacturingFacility mf, Plant shipsTo, Calendar orderDate)  throws InvalidStatusException;

    ProcuredStockPurchaseOrderDetail createNewPurchaseOrderDetail(Long poId, Long stockId, int numberOfLots) throws DuplicateEntryException, Exception;

    void deletePurchaseOrder(Long poId);

    void deletePurchaseOrderDetail(Long podId);

    void updatePurchaseOrder(Long poId, PurchaseOrderStatus status, Calendar orderDate) throws InvalidStatusException;

    void updatePurchaseOrderDetail(ProcuredStockPurchaseOrderDetail pod) throws NoLotsException;

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
