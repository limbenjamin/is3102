package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanLocal;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.Entities.GoodsReceiptDocument;
import IslandFurniture.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Entities.Stock;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author KamilulAshraf
 */
@Stateful
public class ManageGoodsReceipt implements ManageGoodsReceiptLocal {

    @PersistenceContext
    EntityManager em;

    private GoodsReceiptDocument goodsReceiptDocument;
    private GoodsIssuedDocument goodsIssuedDocument;
    private GoodsReceiptDocumentDetail goodsReceiptDocumentDetail;
    private Stock stock;
    private ProcuredStockPurchaseOrder purchaseOrder;
    
    @EJB
    private ManageNotificationsBeanLocal manageNotificationsBean;
    @EJB
    private ManagePrivilegesBeanLocal managePrivilegesBean;

//  Function: To get the Goods Recipt Document entity from Id
    @Override
    public GoodsReceiptDocument getGoodsReceiptDocument(Long id) {
        goodsReceiptDocument = (GoodsReceiptDocument) em.find(GoodsReceiptDocument.class, id);
        return goodsReceiptDocument;
    }

//  Function: To get the Goods Issued Document entity from Id    
    @Override
    public GoodsIssuedDocument getGoodsIssuedDocument(Long id) {
        goodsIssuedDocument = (GoodsIssuedDocument) em.find(GoodsIssuedDocument.class, id);
        return goodsIssuedDocument;
    }

//  Function: To get the Goods Recipt Document Detail entity from Id    
    @Override
    public GoodsReceiptDocumentDetail getGoodsReceiptDocumentDetail(Long id) {
        goodsReceiptDocumentDetail = (GoodsReceiptDocumentDetail) em.find(GoodsReceiptDocumentDetail.class, id);
        return goodsReceiptDocumentDetail;
    }

//  Function: To get the Purchase Order entity from Id
    @Override
    public ProcuredStockPurchaseOrder getPurchaseOrder(Long id) {
        purchaseOrder = (ProcuredStockPurchaseOrder) em.find(ProcuredStockPurchaseOrder.class, id);
        return purchaseOrder;
    }

//  Function: To get the Stock entity from Id    
    @Override
    public Stock getStock(Long id) {
        stock = (Stock) em.find(Stock.class, id);
        return stock;
    }

//  Function: To create a Goods Receipt Document    
    @Override
    public GoodsReceiptDocument createGoodsReceiptDocument(Plant plant, Calendar cal) {
        goodsReceiptDocument = new GoodsReceiptDocument();
        goodsReceiptDocument.setPlant(plant);
        goodsReceiptDocument.setReceiptDate(cal);
        goodsReceiptDocument.setConfirm(false);
        em.persist(goodsReceiptDocument);
        em.flush();
        return goodsReceiptDocument;
    }

//  Function: To create Goods Receipt Document from Goods Issued Document    
    @Override
    public GoodsReceiptDocument createGoodsReceiptDocumentfromGoodsIssuedDocument(Plant plant, Calendar receiptDate) {
        goodsReceiptDocument = new GoodsReceiptDocument();
        goodsReceiptDocument.setPlant(plant);
        goodsReceiptDocument.setConfirm(false);
        goodsReceiptDocument.setReceiptDate(receiptDate);
        em.persist(goodsReceiptDocument);
        em.flush();
        return goodsReceiptDocument;
    }

//  Function: To create Goods Receipt Document Detail    
    @Override
    public void createGoodsReceiptDocumentDetail(Long grdId, Long stockId, Integer quantity) {

        System.out.println("The documentId is: " + grdId);

        goodsReceiptDocumentDetail = new GoodsReceiptDocumentDetail();
        goodsReceiptDocument = getGoodsReceiptDocument(grdId);
        stock = getStock(stockId);
        goodsReceiptDocumentDetail.setGoodsReceiptDocument(goodsReceiptDocument);
        goodsReceiptDocumentDetail.setReceivedStock(stock);
        goodsReceiptDocumentDetail.setQuantity(quantity);
        goodsReceiptDocument.getGoodsReceiptDocumentDetails().add(goodsReceiptDocumentDetail);
        em.persist(goodsReceiptDocumentDetail);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

//  Function: To create Stock Units once the Goods Receipt Document is Posted    
    @Override
    public void createStockUnitsFromGoodsReceiptDocument(Long grdId, Calendar postingDate) {
        goodsReceiptDocument = getGoodsReceiptDocument(grdId);

        if (goodsReceiptDocument.getReceiveFrom() != null) {
            goodsReceiptDocument.getReceiveFrom().setStatus(PurchaseOrderStatus.DELIVERED);
            manageNotificationsBean.createNewNotificationForPrivilegeFromPlant("Pending Payment", "Purchase Order #" +  goodsReceiptDocument.getReceiveFrom().getId().toString() +" was delivered at " + goodsReceiptDocument.getPlant().getName(), "/purchasing/purchaseorder.xhtml", "Make Payment to Supplier", managePrivilegesBean.getPrivilegeFromName("Purchase Order"), goodsReceiptDocument.getReceiveFrom().getManufacturingFacility());
        }
        goodsReceiptDocument.setConfirm(true);
        goodsReceiptDocument.setPostingDate(postingDate);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

//  Function: To edit Goods Issued Document's Shipment Status to Delivered
    @Override
    public void updateIncomingShipmentStatusToDelivered(Long id) {
        goodsIssuedDocument = getGoodsIssuedDocument(id);
        goodsIssuedDocument.setReceived(true);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

//  Function: To edit Goods Receipt Document    
    @Override
    public void editGoodsReceiptDocument(Long goodsReceiptDocumentId, Calendar receiptDate, String deliveryNote) {
        goodsReceiptDocument = getGoodsReceiptDocument(goodsReceiptDocumentId);
        goodsReceiptDocument.setReceiptDate(receiptDate);
        goodsReceiptDocument.setDeliveryNote(deliveryNote);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

// Function: To edit Goods Receipt Document Detail
    @Override
    public void editGoodsReceiptDocumentDetail(Long grddId, Long stockId, Integer qty) {
        goodsReceiptDocumentDetail = getGoodsReceiptDocumentDetail(grddId);
        stock = getStock(stockId);
        goodsReceiptDocumentDetail.setGoodsReceiptDocument(goodsReceiptDocument);
        goodsReceiptDocumentDetail.setReceivedStock(stock);
        goodsReceiptDocumentDetail.setQuantity(qty);
        em.merge(goodsReceiptDocumentDetail);
        em.flush();
    }

//  Function: To edit/add the quantity of a Goods Receipt Document Detail, when a same Stock is added to the Goods Receipt Document
    @Override
    public void editGoodsReceiptDocumentDetailQtyWhenSameStockIdIsAdded(Long grddId, Integer qty) {
        goodsReceiptDocumentDetail = getGoodsReceiptDocumentDetail(grddId);
        goodsReceiptDocumentDetail.setQuantity(qty);
        em.merge(goodsReceiptDocumentDetail);
        em.flush();
        em.refresh(goodsReceiptDocumentDetail);
    }
    
//  Function: To list of Purchase Orders of the Plant with status Confirmed, to be used when populating from POs at Goods Receipt Document 
    @Override
    public List<ProcuredStockPurchaseOrder> viewPurchaseOrder(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ProcuredStockPurchaseOrder s WHERE s.shipsTo.id=:plantId AND s.status=:status");
        q.setParameter("status", PurchaseOrderStatus.CONFIRMED);
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Function: To return list the Purchase Order Details of a Purchase Order which will be used to create Goods Receipt Document Details    
    @Override
    public List<ProcuredStockPurchaseOrderDetail> viewPurchaseOrderDetail(ProcuredStockPurchaseOrder po) {
        Query q = em.createQuery("SELECT s FROM ProcuredStockPurchaseOrderDetail s WHERE s.purchaseOrder.id=:id");
        q.setParameter("id", po.getId());
        return q.getResultList();
    }

//  Function: To diplay list of Goods Receipt Document of a Plant    
    @Override
    public List<GoodsReceiptDocument> viewGoodsReceiptDocument(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsReceiptDocument s WHERE s.confirm=FALSE AND s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Function: To diplay list of Inbound Shipment from Goods Issued Document of a Plant   
    @Override
    public List<GoodsIssuedDocument> viewInboundShipment(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.confirm=TRUE AND s.deliverTo.id=:plantId AND s.received=FALSE");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Function: To diplay list of Goods Receipt Document Posted    
    @Override
    public List<GoodsReceiptDocument> viewGoodsReceiptDocumentPosted(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsReceiptDocument s WHERE s.confirm=TRUE AND s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Function: To display list of Goods Receipt Document Details in a Goods Receipt Document    
    @Override
    public List<GoodsReceiptDocumentDetail> viewGoodsReceiptDocumentDetail(GoodsReceiptDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsReceiptDocumentDetail s WHERE s.goodsReceiptDocument.id=" + grd.getId());
        return q.getResultList();
    }

//  Function: To return list of items in the Goods Issued Document, to be used to create a Goods Receipt Document Detail    
    @Override
    public List<GoodsIssuedDocumentDetail> viewInboundShipmentByDetail(Long id) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocumentDetail s WHERE s.goodsIssuedDocument.id=:id");
        q.setParameter("id", id);
        return q.getResultList();
    }

//  @Can be done better -- Function: To return information on the Goods Receipt Document
    @Override
    public List<GoodsReceiptDocument> viewGoodsReceiptDocumentIndividual(GoodsReceiptDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsReceiptDocument s WHERE s.id=" + grd.getId());
        return q.getResultList();
    }

// Function: Delete Goods Receipt Document   
    @Override
    public void deleteGoodsReceiptDocument(Long goodsReceiptDocumentId) {

        goodsReceiptDocument = getGoodsReceiptDocument(goodsReceiptDocumentId);

        if (goodsReceiptDocument.getReceiveFrom() != null) {
            purchaseOrder = getPurchaseOrder(goodsReceiptDocument.getReceiveFrom().getId());
            purchaseOrder.setGoodsReceiptDocument(null);
            em.merge(purchaseOrder);
            em.flush();
        }

        em.remove(goodsReceiptDocument);
        em.flush();
    }

//  Function: Delete Goods Receipt Document Detail
    @Override
    public void deleteGoodsReceiptDocumentDetail(Long goodsReceiptDocumentDetailId) {
        goodsReceiptDocumentDetail = getGoodsReceiptDocumentDetail(goodsReceiptDocumentDetailId);
        em.remove(goodsReceiptDocumentDetail);
        em.flush();
    }
    
//  @Need to check --  Function: To set the Goods Receipt Document to the Purchase Order    
    @Override
    public void setGoodsReceiptDocumentToThePurchaseOrder(Long goodsReceiptDocumentId, ProcuredStockPurchaseOrder po, Calendar date) {
        goodsReceiptDocument = getGoodsReceiptDocument(goodsReceiptDocumentId);
        po.setGoodsReceiptDocument(goodsReceiptDocument);
        em.merge(po);
        em.flush();
        goodsReceiptDocument.setReceiveFrom(po);
        goodsReceiptDocument.setReceiptDate(date);
        em.merge(goodsReceiptDocument);
        em.flush();
    }
}
