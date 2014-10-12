/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientPurchaseOrder;
import IslandFurniture.Entities.IngredientPurchaseOrderDetail;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Zee
 */
public interface IngredientProcurementManagerLocal {

    IngredientPurchaseOrder createIngredPurchaseOrder(PurchaseOrderStatus status, IngredientSupplier supplier, Store store, Plant shipsTo, Calendar orderDate);

    void createNewIngredPurchaseOrderDetail(Long poId, Long stockId, int numberOfLots) throws DuplicateEntryException;

    void deleteIngredPurchaseOrder(Long poId);

    void deleteIngredPurchaseOrderDetail(Long podId);

    Integer getLotSize(Ingredient ingredient, Store store);

    IngredientPurchaseOrder getPurchaseOrder(Long id);

    void updateIngredPurchaseOrder(Long poId, PurchaseOrderStatus status, Calendar orderDate);

    void updateIngredPurchaseOrderDetail(IngredientPurchaseOrderDetail pod);

    List<IngredientPurchaseOrder> viewConfirmedIngredPurchaseOrders(Plant staffPlant);

    List<IngredientPurchaseOrder> viewDeliveredIngredPurchaseOrders(Plant staffPlant);

    List<IngredientSupplier> viewIngredContractedSuppliers(Store store);

    List<IngredientPurchaseOrderDetail> viewIngredPurchaseOrderDetails(Long orderId);

    List<Ingredient> viewIngredSupplierProcuredStocks(Long orderId, Store store);

    List<IngredientPurchaseOrder> viewPaidIngredPurchaseOrders(Plant staffPlant);

    List<IngredientPurchaseOrder> viewPlannedIngredPurchaseOrders(Plant staffPlant);
    
}
