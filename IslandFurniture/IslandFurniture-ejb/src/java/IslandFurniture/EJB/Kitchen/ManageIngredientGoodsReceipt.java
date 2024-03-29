/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanLocal;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientGoodsReceiptDocument;
import IslandFurniture.Entities.IngredientGoodsReceiptDocumentDetail;
import IslandFurniture.Entities.IngredientInventory;
import IslandFurniture.Entities.IngredientInventoryPK;
import IslandFurniture.Entities.IngredientPurchaseOrder;
import IslandFurniture.Entities.IngredientPurchaseOrderDetail;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.PurchaseOrderStatus;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kamilul Ashraf
 */
@Stateless
public class ManageIngredientGoodsReceipt implements ManageIngredientGoodsReceiptLocal {

    @PersistenceContext
    EntityManager em;

    @EJB
    private ManageNotificationsBeanLocal manageNotificationsBean;
    @EJB
    private ManagePrivilegesBeanLocal managePrivilegesBean;

    private IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument;
    private IngredientGoodsReceiptDocumentDetail ingredientGoodsReceiptDocumentDetail;
    private IngredientPurchaseOrder ingredientPurchaseOrder;
    private IngredientInventory ingredientInventory;

    // Function: Create Ingredient Goods Receipt Document
    @Override
    public IngredientGoodsReceiptDocument createIngredientGoodsReceiptDocument(Staff staff, Calendar createTime, Store store) {
        IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument = new IngredientGoodsReceiptDocument();
        ingredientGoodsReceiptDocument.setStore(store);
        ingredientGoodsReceiptDocument.setCreatedBy(staff);
        ingredientGoodsReceiptDocument.setCreationTime(createTime);
        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(createTime);
        ingredientGoodsReceiptDocument.setReceiptDate(createTime);
        ingredientGoodsReceiptDocument.setDeliveryNote("Note Number: ");
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
        ingredientGoodsReceiptDocument.setReceiptDate(time);
        ingredientGoodsReceiptDocument.setDeliveryNote(updatedIngredientGoodsReceiptDocument.getDeliveryNote());
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
        for (IngredientGoodsReceiptDocumentDetail ii : ingredientGoodsReceiptDocument.getIngredGoodsReceiptDocumentDetails()) {
            IngredientInventoryPK pk = new IngredientInventoryPK(ingredientGoodsReceiptDocument.getStore().getId(), ii.getIngredient().getId());
            ingredientInventory = (IngredientInventory) em.find(IngredientInventory.class, pk);
            ingredientInventory.setQty(ingredientInventory.getQty() + ii.getQty());
            em.merge(ingredientInventory);
        }

        if (ingredientGoodsReceiptDocument.getIngredientPurchaseOrder() != null) {
            ingredientGoodsReceiptDocument.getIngredientPurchaseOrder().setStatus(PurchaseOrderStatus.DELIVERED);
            manageNotificationsBean.createNewNotificationForPrivilegeFromPlant("Pending Payment", "Purchase Order #" + ingredientGoodsReceiptDocument.getIngredientPurchaseOrder().getId().toString() + " was delivered at " + ingredientGoodsReceiptDocument.getStore().getName() + ingredientGoodsReceiptDocument.getStore().getClass().getSimpleName(), "/kitchen/ingredientpo.xhtml", "Make Payment to Supplier", managePrivilegesBean.getPrivilegeFromName("Ingredient Planner"), ingredientGoodsReceiptDocument.getStore());
        }

        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(time);
        ingredientGoodsReceiptDocument.setPostingDate(time);
        ingredientGoodsReceiptDocument.setPosted(true);
        em.merge(ingredientGoodsReceiptDocument);
        em.flush();
    }

    // Function: Delete Ingredient Goods Receipt Document
    @Override
    public void deleteIngredientGoodsReceiptDocument(IngredientGoodsReceiptDocument ingredientGoodsReceiptDocument) {
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, ingredientGoodsReceiptDocument.getId());
        if (ingredientGoodsReceiptDocument.getIngredientPurchaseOrder() != null) {
            ingredientPurchaseOrder = (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, ingredientGoodsReceiptDocument.getIngredientPurchaseOrder());
            ingredientPurchaseOrder.setIngredGoodsReceiptDoc(null);
            em.merge(ingredientPurchaseOrder);
        }
        em.remove(ingredientGoodsReceiptDocument);
        em.flush();
    }

    //  Function: View list of Ingredient Goods Receipt Document
    @Override
    public List<IngredientGoodsReceiptDocument> viewIngredientGooodsReceiptDocument(Store store, boolean status) {
        Query q = em.createQuery("SELECT s FROM IngredientGoodsReceiptDocument s WHERE s.store.id=:storeId AND s.posted=:status");
        q.setParameter("storeId", store.getId());
        q.setParameter("status", status);
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
        ingredientGoodsReceiptDocument = (IngredientGoodsReceiptDocument) em.find(IngredientGoodsReceiptDocument.class, updatedIngredientGoodsReceiptDocumentDetail.getIngredGoodsReceiptDocument().getId());
        ingredientGoodsReceiptDocumentDetail = (IngredientGoodsReceiptDocumentDetail) em.find(IngredientGoodsReceiptDocumentDetail.class, updatedIngredientGoodsReceiptDocumentDetail.getId());
        ingredientGoodsReceiptDocumentDetail.setQty(updatedIngredientGoodsReceiptDocumentDetail.getQty());
        ingredientGoodsReceiptDocumentDetail.setIngredient(updatedIngredientGoodsReceiptDocumentDetail.getIngredient());

        System.out.println("The ingredient is " + updatedIngredientGoodsReceiptDocumentDetail.getIngredient().getName());

        ingredientGoodsReceiptDocument.setLastModBy(staff);
        ingredientGoodsReceiptDocument.setLastModTime(time);
        em.merge(ingredientGoodsReceiptDocumentDetail);
        em.merge(ingredientGoodsReceiptDocument);
        em.flush();
        em.refresh(ingredientGoodsReceiptDocument);
        em.refresh(ingredientGoodsReceiptDocumentDetail);
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
    public List<IngredientPurchaseOrder> viewIngredientPurchaseOrder(Store store) {
        Query q = em.createQuery("SELECT s FROM IngredientPurchaseOrder s WHERE s.store.id=:storeId AND s.status=:status");
        q.setParameter("storeId", store.getId());
        q.setParameter("status", PurchaseOrderStatus.CONFIRMED);
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

    //  Function: To get the Purchase Order entity from Id
    @Override
    public IngredientPurchaseOrder getPurchaseOrder(Long id) {
        ingredientPurchaseOrder = (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, id);
        return ingredientPurchaseOrder;
    }

    //  Function: To return list the Purchase Order Details of a Purchase Order which will be used to create Goods Receipt Document Details    
    @Override
    public List<IngredientPurchaseOrderDetail> viewPurchaseOrderDetail(IngredientPurchaseOrder po) {
        Query q = em.createQuery("SELECT s FROM IngredientPurchaseOrderDetail s WHERE s.ingredPurchaseOrder.id=:id");
        q.setParameter("id", po.getId());
        return q.getResultList();
    }

    //  @Need to check --  Function: To set the Goods Receipt Document to the Purchase Order    
    @Override
    public void setGoodsReceiptDocumentToThePurchaseOrder(IngredientGoodsReceiptDocument goodsReceiptDocument, IngredientPurchaseOrder po, Calendar date) {
        po.setIngredGoodsReceiptDoc(ingredientGoodsReceiptDocument);
        em.merge(po);
        em.flush();
        goodsReceiptDocument.setReceiptDate(date);
        em.merge(goodsReceiptDocument);
        em.flush();
    }
}
