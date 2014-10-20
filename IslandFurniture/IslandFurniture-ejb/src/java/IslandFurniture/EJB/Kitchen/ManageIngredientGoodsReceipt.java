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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
public class ManageIngredientGoodsReceipt implements ManageIngredientGoodsReceiptLocal {

    @PersistenceContext
    EntityManager em;

    IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument;
    IngredientGoodsReceiptDocumentDetail ingredientGoodsReceiptDocumentDetail;
    IngredientPurchaseOrder ingredientPurchaseOrder;

    // Function: Create Ingredient Goods Receipt Document
    @Override
    public IngredientGoodsReceiptDocument createIngredientGoodsReceiptDocument(Staff staff, Calendar createTime) {
        IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument = new IngredientGoodsReceiptDocument();
        // Need to setStore here
        ingredientGoodsReceiptDocument.setCreatedBy(staff);
        ingredientGoodsReceiptDocument.setCreationTime(createTime);
        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(createTime);
        ingredientGoodsReceiptDocument.setReceiptDate(createTime);
        ingredientGoodsReceiptDocument.setPosted(false);
        em.persist(ingredientGoodsReceiptDocument);
        em.flush();
        return ingredientGoodsReceiptDocument;
    }

    // Function: Edit Ingredient Goods Receipt Document
    @Override
    public void editIngredientGoodsReceiptDocument(Staff staff, Calendar time, IngredientGoodsReceiptDocument updatedIngredientGoodsReceiptDocument) {
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, updatedIngredientGoodsReceiptDocument.getId());
        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(time);
        ingredientGoodsReceiptDocument.setReceiptDate(updatedIngredientGoodsReceiptDocument.getReceiptDate());
        em.merge(ingredientGoodsReceiptDocument);
        em.flush();
    }

    // Function: Edit Ingredient Goods Receipt with Purchase Order
    @Override
    public void editIngredientGoodsReceiptDocumentWithPO(Staff staff, Calendar time, IngredientGoodsReceiptDocument updatedIngredientGoodsReceiptDocument, Long purchaseOrderId) {
        ingredientPurchaseOrder = (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, purchaseOrderId);
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, updatedIngredientGoodsReceiptDocument.getId());
        ingredientPurchaseOrder.setIngredGoodsReceiptDoc(ingredientGoodsReceiptDocument);
        em.merge(ingredientPurchaseOrder);
        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(time);
        ingredientGoodsReceiptDocument.setIngredientPurchaseOrder(ingredientPurchaseOrder);
//        ingredientGoodsReceiptDocument.setSupplier(ingredientPurchaseOrder.getIngredSupplier());
        em.merge(ingredientGoodsReceiptDocument);
        em.flush();
    }

    // Function: Post Ingredient Goods Receipt Document
    @Override
    public void postIngredientGoodsReceiptDocument(Staff staff, Calendar time, IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument) {
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, ingredientGoodsReceiptDocument.getId());
        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(time);
        ingredientGoodsReceiptDocument.setPosted(true);
        em.merge(ingredientGoodsReceiptDocument);
        em.flush();
    }

    // Function: Delete Ingredient Goods Receipt Document
    @Override
    public void deleteIngredientGoodsReceiptDocument(IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument) {
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, ingredientGoodsReceiptDocument.getId());
        ingredientPurchaseOrder = (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, ingredientGoodsReceiptDocument.getIngredientPurchaseOrder());
        ingredientPurchaseOrder.setIngredGoodsReceiptDoc(null);
        em.merge(ingredientPurchaseOrder);
        em.remove(ingredientGoodsReceiptDocument);
        em.flush();
    }

    //  Function: View list of Ingredient Goods Receipt Document
    @Override
    public List<IngredientGoodsReceiptDocument> viewIngredientGooodsReceiptDocument(Store store) {
        Query q = em.createQuery("SELECT s FROM IngredientGoodsReceiptDocument s WHERE s.store.id=:storeId");
        q.setParameter("storeId", store.getId());
        return q.getResultList();
    }

    // Function: Create Ingredient Goods Receipt Document Detail
    @Override
    public void createIngredientGoodsReceiptDocumentDetail(Staff staff, Calendar time, IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument, Ingredient ingredient, Integer qty) {
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, ingredientGoodsReceiptDocument.getId());
        IngredientGoodsReceiptDocumentDetail ingredientGoodsReceiptDocumentDetail = new IngredientGoodsReceiptDocumentDetail();
        ingredientGoodsReceiptDocumentDetail.setIngredGoodsReceiptDocument(ingredientGoodsReceiptDocument);
        ingredientGoodsReceiptDocumentDetail.setIngredient(ingredient);
        ingredientGoodsReceiptDocumentDetail.setQty(qty);
        em.persist(ingredientGoodsReceiptDocumentDetail);
        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(time);
        em.merge(ingredientGoodsReceiptDocument);
        em.flush();
    }

    // Function: Edit Ingredient Goods Receipt Document Detail
    @Override
    public void editIngredientGoodsReceiptDocumentDetail(Staff staff, Calendar time, IngredientGoodsReceiptDocumentDetail updatedIngredientGoodsReceiptDocumentDetail) {
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, ingredientGoodsReceiptDocumentDetail.getIngredGoodsReceiptDocument().getId());
        ingredientGoodsReceiptDocumentDetail = (IngredientGoodsReceiptDocumentDetail) em.find(IngredientGoodsReceiptDocumentDetail.class, updatedIngredientGoodsReceiptDocumentDetail.getId());
        ingredientGoodsReceiptDocumentDetail.setIngredient(updatedIngredientGoodsReceiptDocumentDetail.getIngredient());
        ingredientGoodsReceiptDocumentDetail.setQty(updatedIngredientGoodsReceiptDocumentDetail.getQty());
        em.merge(ingredientGoodsReceiptDocumentDetail);
        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(time);
        em.merge(ingredientGoodsReceiptDocument);
        em.flush();
    }

    // Function: Delete Ingredient Goods Receipt Document Detail
    @Override
    public void deleteIngredientGoodsReceiptDocumentDetail(IngredientGoodsReceiptDocumentDetail ingredientGoodsReceiptDocumentDetail) {
        ingredientGoodsReceiptDocumentDetail = (IngredientGoodsReceiptDocumentDetail) em.find(IngredientGoodsReceiptDocumentDetail.class, ingredientGoodsReceiptDocumentDetail.getId());
        em.remove(ingredientGoodsReceiptDocumentDetail);
        em.flush();
    }

    //  Function: View list of Ingredient Goods Receipt Document Detail
    @Override
    public List<IngredientGoodsReceiptDocumentDetail> viewIngredientGooodsReceiptDocumentDetail(IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument) {
        Query q = em.createQuery("SELECT s FROM IngredientGoodsReceiptDocumentDetail s WHERE s.ingredGoodsReceiptDocument.id=:id");
        q.setParameter("id", ingredientGoodsReceiptDocument.getId());
        return q.getResultList();
    }

    //  Function: To check if there is any Ingredient Goods Receipt Document Detail with same Ingredient
    @Override
    public boolean checkIfNoIngredientGooodsReceiptDocumentDetailSameIngredient(IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument, Long id) {
        Query q = em.createQuery("SELECT s FROM IngredientGoodsReceiptDocumentDetail s WHERE s.ingredGoodsReceiptDocument.id=:id1 AND s.ingredient.id=:id2");
        q.setParameter("id1", ingredientGoodsReceiptDocument.getId());
        q.setParameter("id2", id);
        return q.getResultList().isEmpty();
    }

    //  Function: View list of Ingredient Purchase Order
    @Override
    public List<IngredientPurchaseOrder> viewIngredientPurchaseOrder(Store store, Boolean status) {
        Query q = em.createQuery("SELECT s FROM IngredientPurchaseOrder s WHERE s.store.id=:storeId AND s.status=:status");
        q.setParameter("storeId", store.getId());
        q.setParameter("status", status);
        return q.getResultList();
    }

    //  Function: View list of Ingredient
    @Override
    public List<Ingredient> viewIngredient() {
        Query q = em.createQuery("SELECT * FROM Ingredient");
        return q.getResultList();
    }
    
    //  Function: Return IngredientGoodsReceiptDocument entity
    @Override
    public IngredientGoodsReceiptDocument getIngredientGoodsReceiptDocument(Long id) {
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, id);
        return ingredientGoodsReceiptDocument;
    }
}
