/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientContractDetail;
import IslandFurniture.Entities.IngredientPurchaseOrder;
import IslandFurniture.Entities.IngredientPurchaseOrderDetail;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Zee
 */
@Stateless
@LocalBean
public class IngredientProcurementManager implements IngredientProcurementManagerLocal {

    @PersistenceContext
    EntityManager em;
    
    @Override
    public IngredientPurchaseOrder getPurchaseOrder(Long id) {
        return (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, id);
    }    
    
    @Override
    public IngredientPurchaseOrder createIngredPurchaseOrder(PurchaseOrderStatus status, IngredientSupplier supplier, Store store, Plant shipsTo, Calendar orderDate) {
        IngredientPurchaseOrder purchaseOrder = new IngredientPurchaseOrder();
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setStatus(status);
        purchaseOrder.setIngredSupplier(supplier);
        purchaseOrder.setShipsTo(shipsTo);
        purchaseOrder.setStore(store);
        
        em.persist(purchaseOrder);
        return purchaseOrder;
    }
    
    @Override
    public void createNewIngredPurchaseOrderDetail(Long poId, Long stockId, int numberOfLots) throws DuplicateEntryException {
        IngredientPurchaseOrder purchaseOrder = (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, poId);
        Ingredient ingredient = (Ingredient) em.find(Ingredient.class, stockId);
        Store store = purchaseOrder.getStore();
        Integer totalQuantity = getLotSize(ingredient, store) * numberOfLots;

        if (!purchaseOrder.hasIngredient(ingredient)) {
            IngredientPurchaseOrderDetail purchaseOrderDetail = new IngredientPurchaseOrderDetail();
            purchaseOrderDetail.setIngredPurchaseOrder(purchaseOrder);
            purchaseOrderDetail.setIngredient(ingredient);
            purchaseOrderDetail.setNumberOfLots(numberOfLots);
            purchaseOrderDetail.setQuantity(totalQuantity);
            purchaseOrder.getIngredPurchaseOrderDetails().add(purchaseOrderDetail);

            em.persist(purchaseOrderDetail);
            em.flush();
        } else {
            throw new DuplicateEntryException("Entry for " + ingredient.getName() + " already exists in this Ingredient Purchase Order!");
        }
    }
    
    @Override
    public void updateIngredPurchaseOrder(Long poId, PurchaseOrderStatus status, Calendar orderDate) {
        IngredientPurchaseOrder purchaseOrder = (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, poId);
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setStatus(status);
        em.persist(purchaseOrder);
    }   
    
    @Override
    public void updateIngredPurchaseOrderDetail(IngredientPurchaseOrderDetail pod) {
        Ingredient ingredient = pod.getIngredient();
        Store store = pod.getIngredPurchaseOrder().getStore();
        Integer totalQuantity = getLotSize(ingredient, store) * pod.getNumberOfLots();
        pod.setQuantity(totalQuantity);
        em.merge(pod);
    }    
    
    @Override
    public List<IngredientPurchaseOrderDetail> viewIngredPurchaseOrderDetails(Long orderId) {
        IngredientPurchaseOrder purchaseOrder = (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, orderId);
        em.refresh(purchaseOrder);

        List<IngredientPurchaseOrderDetail> stockOrders = purchaseOrder.getIngredPurchaseOrderDetails();
        return stockOrders;
    }    
    
    @Override
    public List<Ingredient> viewIngredSupplierProcuredStocks(Long orderId, Store store) {
        IngredientPurchaseOrder purchaseOrder = (IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, orderId);
        IngredientSupplier supplier = purchaseOrder.getIngredSupplier();

        Query q = em.createNamedQuery("getIngredientList");
        q.setParameter("supplier", supplier);
        q.setParameter("co", store.getCountryOffice());
        List<Ingredient> availableStocks = (List<Ingredient>) q.getResultList();     
        
        return availableStocks;
    }    
    
    @Override
    public Integer getLotSize(Ingredient ingredient, Store store) {
        Query q = em.createNamedQuery("getIngredientContractDetailByIngredAndCo");
        q.setParameter("ingredient", ingredient);
        q.setParameter("co", store.getCountryOffice());
        
        IngredientContractDetail contract = (IngredientContractDetail) q.getSingleResult();
        return contract.getLotSize();
    }

    @Override
    public void deleteIngredPurchaseOrder(Long poId) {
        em.remove((IngredientPurchaseOrder) em.find(IngredientPurchaseOrder.class, poId));
    }    
    
    @Override
    public void deleteIngredPurchaseOrderDetail(Long podId) {
        em.remove((IngredientPurchaseOrderDetail) em.find(IngredientPurchaseOrderDetail.class, podId));
    }    
    
    @Override
    public List<IngredientSupplier> viewIngredContractedSuppliers(Store store) {
        Query q = em.createNamedQuery("getIngredSupplierList");
        q.setParameter("co", store.getCountryOffice());
        return (List<IngredientSupplier>) q.getResultList();
    } 
    
    @Override
    public List<IngredientPurchaseOrder> viewPlannedIngredPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM IngredientPurchaseOrder s WHERE s.status=:status AND s.store=:plant");
        q.setParameter("status", PurchaseOrderStatus.PLANNED);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }

    @Override
    public List<IngredientPurchaseOrder> viewConfirmedIngredPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM IngredientPurchaseOrder s WHERE s.status=:status AND s.store=:plant");
        q.setParameter("status", PurchaseOrderStatus.CONFIRMED);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }
    
    @Override
    public List<IngredientPurchaseOrder> viewDeliveredIngredPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM IngredientPurchaseOrder s WHERE s.status=:status AND s.store=:plant");
        q.setParameter("status", PurchaseOrderStatus.DELIVERED);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }     
    
    @Override
    public List<IngredientPurchaseOrder> viewPaidIngredPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM IngredientPurchaseOrder s WHERE s.status=:status AND s.store=:plant");
        q.setParameter("status", PurchaseOrderStatus.PAID);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }     
}
