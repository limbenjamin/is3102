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
import IslandFurniture.StaticClasses.TimeMethods;
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
public class ManagePurchaseOrder implements ManagePurchaseOrderLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    @Override
    public PurchaseOrder getPurchaseOrder(Long id) {
        return (PurchaseOrder) em.find(PurchaseOrder.class, id);
    }
    
    @Override
    public PurchaseOrder createNewPurchaseOrder(PurchaseOrderStatus status, Supplier supplier, ManufacturingFacility mf, Plant shipsTo, Calendar orderDate) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderDate(TimeMethods.convertToUtcTime(mf, orderDate));
        purchaseOrder.setStatus(status);
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setShipsTo(shipsTo);
        purchaseOrder.setManufacturingFacility(mf);
        
        em.persist(purchaseOrder);
        return purchaseOrder;
    }

    @Override
    public void createNewPurchaseOrderDetail(Long poId, Long stockId, int quantity) throws DuplicateEntryException {
        PurchaseOrder purchaseOrder = (PurchaseOrder) em.find(PurchaseOrder.class, poId);
        ProcuredStock procuredStock = (ProcuredStock) em.find(ProcuredStock.class, stockId);

        if (!purchaseOrder.hasProcuredStock(procuredStock)) {
            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
            purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
            purchaseOrderDetail.setProcuredStock(procuredStock);
            purchaseOrderDetail.setQuantity(quantity);
            purchaseOrder.getPurchaseOrderDetails().add(purchaseOrderDetail);

            em.persist(purchaseOrderDetail);
            em.flush();
        } else {
            throw new DuplicateEntryException("Entry for " + procuredStock.getName() + " already exists in this Purchase Order!");
        }

    }

    @Override
    public void updatePurchaseOrder(Long poId, PurchaseOrderStatus status, Calendar orderDate) {
        PurchaseOrder purchaseOrder = (PurchaseOrder) em.find(PurchaseOrder.class, poId);
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setStatus(status);
        em.persist(purchaseOrder);
    }

    @Override
    public void updatePurchaseOrderDetail(PurchaseOrderDetail pod) {
        em.merge(pod);
    }

    @Override
    public List<PurchaseOrderDetail> viewPurchaseOrderDetails(Long orderId) {
        PurchaseOrder purchaseOrder = (PurchaseOrder) em.find(PurchaseOrder.class, orderId);
        em.refresh(purchaseOrder);

        List<PurchaseOrderDetail> stockOrders = purchaseOrder.getPurchaseOrderDetails();
        return stockOrders;
    }

    @Override
    public List<ProcuredStock> viewSupplierProcuredStocks(Long orderId, ManufacturingFacility mf) {
        PurchaseOrder purchaseOrder = (PurchaseOrder) em.find(PurchaseOrder.class, orderId);
        Supplier supplier = purchaseOrder.getSupplier();

        Query q = em.createNamedQuery("getStockList");
        q.setParameter("supplier", supplier);
        q.setParameter("mf", mf);
        List<ProcuredStock> availableStocks = (List<ProcuredStock>) q.getResultList();
        List<PurchaseOrderDetail> orderedItems = viewPurchaseOrderDetails(orderId);
        
        //remove items from list of available stocks if already ordered
        Iterator<PurchaseOrderDetail> iterator = orderedItems.iterator();
        while (iterator.hasNext()) {
            PurchaseOrderDetail orderDetail = iterator.next();
            ProcuredStock item = (ProcuredStock) em.find(ProcuredStock.class, orderDetail.getProcuredStock().getId());
            availableStocks.remove(item);
        }        
        
        return availableStocks;
    }

    @Override
    public void deletePurchaseOrder(Long poId) {
        em.remove((PurchaseOrder) em.find(PurchaseOrder.class, poId));
    }

    @Override
    public void deletePurchaseOrderDetail(Long podId) {
        em.remove((PurchaseOrderDetail) em.find(PurchaseOrderDetail.class, podId));
    }

    @Override
    public List<Supplier> viewContractedSuppliers(ManufacturingFacility mf) {
        Query q = em.createNamedQuery("getSupplierList");
        q.setParameter("mf", mf);
        return (List<Supplier>) q.getResultList();
    }

    @Override
    public List<PurchaseOrder> viewPlannedPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM PurchaseOrder s WHERE s.status=:status AND s.manufacturingFacility=:plant");
        q.setParameter("status", PurchaseOrderStatus.PLANNED);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }

    @Override
    public List<PurchaseOrder> viewConfirmedPurchaseOrders(Plant staffPlant) {
        Query q = em.createQuery("SELECT s FROM PurchaseOrder s WHERE s.status=:status AND s.manufacturingFacility=:plant");
        q.setParameter("status", PurchaseOrderStatus.CONFIRMED);
        q.setParameter("plant", staffPlant);
        return q.getResultList();
    }
}
