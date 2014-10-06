/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Purchasing;

import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.PurchaseOrder;
import IslandFurniture.Entities.PurchaseOrderDetail;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Entities.Supplier;
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

    PurchaseOrder createNewPurchaseOrder(PurchaseOrderStatus status, Supplier supplier, Plant plant, Plant shipsTo, Calendar orderDate);

    void createNewPurchaseOrderDetail(Long poId, Long stockId, int quantity) throws DuplicateEntryException;

    void deletePurchaseOrder(Long poId);

    void deletePurchaseOrderDetail(Long podId);

    void updatePurchaseOrder(Long poId, PurchaseOrderStatus status, Calendar orderDate);

    void updatePurchaseOrderDetail(PurchaseOrderDetail pod, Integer qty);

    ProcuredStock getProcuredStock(Long id);

    PurchaseOrder getPurchaseOrder(Long id);

    PurchaseOrderDetail getPurchaseOrderDetail(Long id);

    List<Supplier> viewContractedSuppliers(ManufacturingFacility mf);

    List<PurchaseOrderDetail> viewPurchaseOrderDetails(Long orderId);

    List<ProcuredStock> viewSupplierProcuredStocks(Long orderId, ManufacturingFacility mf);

    List<PurchaseOrder> viewPlannedPurchaseOrders(Plant staffPlant);

    List<PurchaseOrder> viewConfirmedPurchaseOrders(Plant staffPlant);
}
