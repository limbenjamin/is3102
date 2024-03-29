/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Purchasing;

import IslandFurniture.Exceptions.NoLotsException;
import IslandFurniture.Exceptions.InvalidStatusException;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Zee
 */
@Stateless
public class ManagePurchaseOrder implements ManagePurchaseOrderLocal,ManagePurchaseOrderRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    @Override
    public ProcuredStockPurchaseOrder getPurchaseOrder(Long id) {
        return (ProcuredStockPurchaseOrder) em.find(ProcuredStockPurchaseOrder.class, id);
    }
    
    @Override
    public ProcuredStockPurchaseOrder createNewPurchaseOrder(PurchaseOrderStatus status, ProcuredStockSupplier supplier, ManufacturingFacility mf, Plant shipsTo, Calendar orderDate) throws InvalidStatusException {
        if (status == null){
            throw new InvalidStatusException();
        }
        ProcuredStockPurchaseOrder purchaseOrder = new ProcuredStockPurchaseOrder();
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setStatus(status);
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setShipsTo(shipsTo);
        purchaseOrder.setManufacturingFacility(mf);
        
        em.persist(purchaseOrder);
        return purchaseOrder;
    }

    @Override
    public ProcuredStockPurchaseOrderDetail createNewPurchaseOrderDetail(Long poId, Long stockId, int numberOfLots) throws DuplicateEntryException, NoLotsException {
        if (numberOfLots == 0)
            throw new NoLotsException();
        ProcuredStockPurchaseOrder purchaseOrder = (ProcuredStockPurchaseOrder) em.find(ProcuredStockPurchaseOrder.class, poId);
        ProcuredStock procuredStock = (ProcuredStock) em.find(ProcuredStock.class, stockId);
        ManufacturingFacility mf = purchaseOrder.getManufacturingFacility();
        Integer totalQuantity = getLotSize(procuredStock, mf) * numberOfLots;
        ProcuredStockPurchaseOrderDetail purchaseOrderDetail;
        if (!purchaseOrder.hasProcuredStock(procuredStock)) {
            purchaseOrderDetail = new ProcuredStockPurchaseOrderDetail();
            purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
            purchaseOrderDetail.setProcuredStock(procuredStock);
            purchaseOrderDetail.setNumberOfLots(numberOfLots);
            purchaseOrderDetail.setQuantity(totalQuantity);
            purchaseOrder.getPurchaseOrderDetails().add(purchaseOrderDetail);

            em.persist(purchaseOrderDetail);
            em.flush();
        } else {
            throw new DuplicateEntryException("Entry for " + procuredStock.getName() + " already exists in this Purchase Order!");
        }
        return purchaseOrderDetail;
    }

    @Override
    public void updatePurchaseOrder(Long poId, PurchaseOrderStatus status, Calendar orderDate) throws InvalidStatusException {
        if (status == null){
            throw new InvalidStatusException();
        }
        ProcuredStockPurchaseOrder purchaseOrder = (ProcuredStockPurchaseOrder) em.find(ProcuredStockPurchaseOrder.class, poId);
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setStatus(status);
        em.persist(purchaseOrder);
    }

    @Override
    public void updatePurchaseOrderDetail(ProcuredStockPurchaseOrderDetail pod) throws NoLotsException{
        if (pod.getNumberOfLots() == 0)
            throw new NoLotsException();
        ProcuredStock procuredStock = pod.getProcuredStock();
        ManufacturingFacility mf = pod.getPurchaseOrder().getManufacturingFacility();
        Integer totalQuantity = getLotSize(procuredStock, mf) * pod.getNumberOfLots();
        pod.setQuantity(totalQuantity);
        em.merge(pod);
    }

    @Override
    public List<ProcuredStockPurchaseOrderDetail> viewPurchaseOrderDetails(Long orderId) {
        ProcuredStockPurchaseOrder purchaseOrder = (ProcuredStockPurchaseOrder) em.find(ProcuredStockPurchaseOrder.class, orderId);
        em.refresh(purchaseOrder);

        List<ProcuredStockPurchaseOrderDetail> stockOrders = purchaseOrder.getPurchaseOrderDetails();
        return stockOrders;
    }

    @Override
    public List<ProcuredStock> viewSupplierProcuredStocks(Long orderId, ManufacturingFacility mf) {
        ProcuredStockPurchaseOrder purchaseOrder = (ProcuredStockPurchaseOrder) em.find(ProcuredStockPurchaseOrder.class, orderId);
        ProcuredStockSupplier supplier = purchaseOrder.getSupplier();

        Query q = em.createNamedQuery("getStockList");
        q.setParameter("supplier", supplier);
        q.setParameter("mf", mf);
        List<ProcuredStock> availableStocks = (List<ProcuredStock>) q.getResultList();
        /*List<ProcuredStockPurchaseOrderDetail> orderedItems = viewPurchaseOrderDetails(orderId);
        
        //remove items from list of available stocks if already ordered
        Iterator<ProcuredStockPurchaseOrderDetail> iterator = orderedItems.iterator();
        while (iterator.hasNext()) {
            ProcuredStockPurchaseOrderDetail orderDetail = iterator.next();
            ProcuredStock item = (ProcuredStock) em.find(ProcuredStock.class, orderDetail.getProcuredStock().getId());
            availableStocks.remove(item);
        }*/        
        
        return availableStocks;
    }
    
    @Override
    public Integer getLotSize(ProcuredStock stock, ManufacturingFacility mf) {
        Query q = em.createNamedQuery("getProcurementContractDetailByStockAndMF");
        q.setParameter("stock", stock);
        q.setParameter("mf", mf);
        
        ProcuredStockContractDetail contract = (ProcuredStockContractDetail) q.getSingleResult();
        return contract.getLotSize();
    }

    @Override
    public void deletePurchaseOrder(Long poId) {
        em.remove((ProcuredStockPurchaseOrder) em.find(ProcuredStockPurchaseOrder.class, poId));
    }

    @Override
    public void deletePurchaseOrderDetail(Long podId) {
        em.remove((ProcuredStockPurchaseOrderDetail) em.find(ProcuredStockPurchaseOrderDetail.class, podId));
    }

    @Override
    public List<ProcuredStockSupplier> viewContractedSuppliers(ManufacturingFacility mf) {
        Query q = em.createNamedQuery("getSupplierList");
        q.setParameter("mf", mf);
        return (List<ProcuredStockSupplier>) q.getResultList();
    }

    @Override
    public List<ProcuredStockPurchaseOrder> viewPlannedPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM ProcuredStockPurchaseOrder s WHERE s.status=:status AND s.manufacturingFacility=:plant");
        q.setParameter("status", PurchaseOrderStatus.PLANNED);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }

    @Override
    public List<ProcuredStockPurchaseOrder> viewConfirmedPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM ProcuredStockPurchaseOrder s WHERE s.status=:status AND s.manufacturingFacility=:plant");
        q.setParameter("status", PurchaseOrderStatus.CONFIRMED);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }
    
    @Override
    public List<ProcuredStockPurchaseOrder> viewDeliveredPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM ProcuredStockPurchaseOrder s WHERE s.status=:status AND s.manufacturingFacility=:plant");
        q.setParameter("status", PurchaseOrderStatus.DELIVERED);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }    
    
    @Override
    public List<ProcuredStockPurchaseOrder> viewPaidPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM ProcuredStockPurchaseOrder s WHERE s.status=:status AND s.manufacturingFacility=:plant");
        q.setParameter("status", PurchaseOrderStatus.PAID);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }    
    
}
