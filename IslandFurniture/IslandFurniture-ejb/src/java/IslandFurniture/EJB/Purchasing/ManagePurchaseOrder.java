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
import java.util.Iterator;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Zee
 */
@Stateful
@LocalBean
public class ManagePurchaseOrder implements ManagePurchaseOrderLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
    
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderDetail purchaseOrderDetail;
    private ProcuredStock procuredStock;   
    private Plant plant;  
    private Supplier supplier;
    
    private Long plantId;
    
    @Override
    public PurchaseOrder getPurchaseOrder(Long id) {
        purchaseOrder = (PurchaseOrder) em.find(PurchaseOrder.class, id);
        return purchaseOrder;
    }    

    @Override
    public PurchaseOrderDetail getPurchaseOrderDetail(Long id) {
        purchaseOrderDetail = (PurchaseOrderDetail) em.find(PurchaseOrderDetail.class, id);
        return purchaseOrderDetail;
    }     

    @Override
    public ProcuredStock getProcuredStock(Long id) {
        procuredStock = (ProcuredStock) em.find(ProcuredStock.class, id);
        return procuredStock;
    }
    
    @Override
    public Long getPlantOfOrder(Long orderId) {
        Query query = em.createQuery("SELECT p.shipsTo FROM PurchaseOrder p where p.id=" + orderId);
        return Long.valueOf(query.getFirstResult());
    }
    
    @Override
    public PurchaseOrder createPurchaseOrder(Calendar orderDate, String status) {
        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setStatus(status);
        em.persist(purchaseOrder);
        em.flush();
        return purchaseOrder;
    }
    
    @Override
    public PurchaseOrder createNewPurchaseOrder(String status, Supplier supplier, Long plantId, Calendar orderDate) {
        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setStatus(status);
        purchaseOrder.setSupplier(supplier);
        plant = (Plant) em.find(Plant.class, plantId);
        purchaseOrder.setShipsTo(plant);        
        em.persist(purchaseOrder);
        em.flush();
        return purchaseOrder;
    }
    
    @Override
    public void createNewPurchaseOrderDetail(Long poId, Long stockId, int quantity) {
        purchaseOrderDetail = new PurchaseOrderDetail();
        purchaseOrder = getPurchaseOrder(poId);
        procuredStock = getProcuredStock(stockId);
        purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
        purchaseOrderDetail.setProcuredStock(procuredStock);
        purchaseOrderDetail.setQuantity(quantity);
        
        purchaseOrder.getPurchaseOrderDetails().add(purchaseOrderDetail);
        em.persist(purchaseOrderDetail);

        em.flush();
    }
    
    @Override
    public void editPurchaseOrder(Long poId, Long plantId, Calendar orderDate, String status) {
        System.out.println("MEOW: edit purchase order");
        purchaseOrder = getPurchaseOrder(poId);
        plant = (Plant) em.find(Plant.class, plantId);
        purchaseOrder.setShipsTo(plant);
        purchaseOrder.setStatus(status);
        purchaseOrder.setOrderDate(orderDate);
        em.persist(purchaseOrder);
        System.out.println("MEOW: purchase order persisted");
        em.flush();        
    }
    
    @Override
    public void updatePurchaseOrder(Long poId, String status, Calendar orderDate) {
        purchaseOrder = getPurchaseOrder(poId);
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setStatus(status);
        em.persist(purchaseOrder);
        em.flush();
    }
    
    @Override
    public void updatePurchaseOrderDetail(PurchaseOrderDetail pod, Long psId, Integer qty) {
        Long podId = pod.getId();
        purchaseOrderDetail = (PurchaseOrderDetail) em.find(PurchaseOrderDetail.class, podId);
        procuredStock = getProcuredStock(psId);
        purchaseOrderDetail.setProcuredStock(procuredStock);
        purchaseOrderDetail.setQuantity(qty);
        em.flush();        
    }
    
    @Override
    public List<PurchaseOrder> viewPurchaseOrders() {
        Query q = em.createQuery("SELECT s " + "FROM PurchaseOrder s");
        return q.getResultList();
    }    
    
    @Override
    public List<PurchaseOrderDetail> viewPurchaseOrderDetails(Long orderId) {
        purchaseOrder = (PurchaseOrder) em.find(PurchaseOrder.class, orderId);
        em.refresh(purchaseOrder);
        
        List<PurchaseOrderDetail> stockOrders = purchaseOrder.getPurchaseOrderDetails();
        return stockOrders;
    }     
    
    @Override
    public List<ProcuredStock> viewProcuredStocks() {
        Query q = em.createQuery("SELECT s " + "FROM ProcuredStock s");
        return q.getResultList();
    }
    
    @Override
    public List<ProcuredStock> viewSupplierProcuredStocks(Long orderId, ManufacturingFacility mf) {
        purchaseOrder = (PurchaseOrder) em.find(PurchaseOrder.class, orderId);
        supplier = purchaseOrder.getSupplier();
        
        Query q = em.createNamedQuery("getStockList");
        q.setParameter("supplier", supplier);
        q.setParameter("mf", mf);
        return (List<ProcuredStock>) q.getResultList();
    }    
    
    @Override
    public List<Plant> viewPlants() {
        Query q = em.createQuery("SELECT s " + "FROM Plant s");
        return q.getResultList();
    }    
    
    @Override
    public List<Supplier> viewSuppliers() {
        Query q = em.createQuery("SELECT s " + "FROM Supplier s");
        return q.getResultList();
    }     
    
    @Override
    public void deletePurchaseOrder(Long poId) {
        purchaseOrder = getPurchaseOrder(poId);
        List<PurchaseOrderDetail> oldList = purchaseOrder.getPurchaseOrderDetails();
        Iterator<PurchaseOrderDetail> iterator = oldList.iterator();
        while (iterator.hasNext()) {
            em.remove(iterator.next());
        }
        em.remove(purchaseOrder);
        em.flush();
    }    
    
    @Override
    public void deletePurchaseOrderDetail(Long podId) {
        purchaseOrderDetail = getPurchaseOrderDetail(podId);
        em.remove(purchaseOrderDetail);
        em.flush();
    } 
    
    @Override
    public List<Supplier> viewContractedSuppliers(ManufacturingFacility mf) {
        Query q = em.createNamedQuery("getSupplierList");        
        q.setParameter("mf", mf);
        return (List<Supplier>)q.getResultList();
    }    
    
    @Override
    public List<PurchaseOrder> viewPlannedPurchaseOrders() {
        Query q = em.createQuery("SELECT s " + "FROM PurchaseOrder s WHERE s.status=:status");
        q.setParameter("status", "planned");
        return q.getResultList();
    }  
    
    @Override
    public List<PurchaseOrder> viewConfirmedPurchaseOrders() {
        Query q = em.createQuery("SELECT s " + "FROM PurchaseOrder s WHERE s.status=:status");
        q.setParameter("status", "confirmed");
        return q.getResultList();
    }    
}
