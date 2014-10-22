/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientGoodsReceiptDocument;
import IslandFurniture.Entities.IngredientGoodsReceiptDocumentDetail;
import IslandFurniture.Entities.IngredientPurchaseOrder;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageIngredientGoodsReceiptLocal {

    //  Function: To check if there is any Ingredient Goods Receipt Document Detail with same Ingredient
    boolean checkIfNoIngredientGooodsReceiptDocumentDetailSameIngredient(IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument, Long id);

    // Function: Create Ingredient Goods Receipt Document
    IngredientGoodsReceiptDocument createIngredientGoodsReceiptDocument(Staff staff, Calendar createTime, Store store);

    // Function: Create Ingredient Goods Receipt Document Detail
    void createIngredientGoodsReceiptDocumentDetail(Staff staff, Calendar time, IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument, Ingredient ingredient, Integer qty);

    // Function: Delete Ingredient Goods Receipt Document
    void deleteIngredientGoodsReceiptDocument(IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument);

    // Function: Delete Ingredient Goods Receipt Document Detail
    void deleteIngredientGoodsReceiptDocumentDetail(IngredientGoodsReceiptDocumentDetail ingredientGoodsReceiptDocumentDetail);

    // Function: Edit Ingredient Goods Receipt Document
    void editIngredientGoodsReceiptDocument(Staff staff, Calendar time, IngredientGoodsReceiptDocument updatedIngredientGoodsReceiptDocument);

    // Function: Edit Ingredient Goods Receipt Document Detail
    void editIngredientGoodsReceiptDocumentDetail(Staff staff, Calendar time, IngredientGoodsReceiptDocumentDetail updatedIngredientGoodsReceiptDocumentDetail);

    // Function: Edit Ingredient Goods Receipt with Purchase Order
    void editIngredientGoodsReceiptDocumentWithPO(Staff staff, Calendar time, IngredientGoodsReceiptDocument updatedIngredientGoodsReceiptDocument, Long purchaseOrderId);

    //  Function: Return IngredientGoodsReceiptDocument entity
    IngredientGoodsReceiptDocument getIngredientGoodsReceiptDocument(Long id);

    // Function: Post Ingredient Goods Receipt Document
    void postIngredientGoodsReceiptDocument(Staff staff, Calendar time, IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument);

    //  Function: View list of Ingredient
    List<Ingredient> viewIngredient();

    //  Function: View list of Ingredient Goods Receipt Document
    List<IngredientGoodsReceiptDocument> viewIngredientGooodsReceiptDocument(Store store);

    //  Function: View list of Ingredient Goods Receipt Document Detail
    List<IngredientGoodsReceiptDocumentDetail> viewIngredientGooodsReceiptDocumentDetail(IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument);

    //  Function: View list of Ingredient Purchase Order
    List<IngredientPurchaseOrder> viewIngredientPurchaseOrder(Store store, Boolean status);
    
}
