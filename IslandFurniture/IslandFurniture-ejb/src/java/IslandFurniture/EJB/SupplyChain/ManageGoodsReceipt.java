package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.PurchaseOrderDetail;
import IslandFurniture.EJB.Entities.PurchaseOrderStatus;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StorageArea;
import IslandFurniture.EJB.Entities.StorageBin;
import java.util.Calendar;
import java.util.List;
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
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderDetail purchaseOrderDetail;

    @Override
    public GoodsReceiptDocument getGoodsReceiptDocument(Long id) {
        goodsReceiptDocument = (GoodsReceiptDocument) em.find(GoodsReceiptDocument.class, id);
        return goodsReceiptDocument;
    }

    @Override
    public GoodsIssuedDocument getGoodsIssuedDocument(Long id) {
        goodsIssuedDocument = (GoodsIssuedDocument) em.find(GoodsIssuedDocument.class, id);
        return goodsIssuedDocument;
    }

    @Override
    public GoodsReceiptDocumentDetail getGoodsReceiptDocumentDetail(Long id) {
        goodsReceiptDocumentDetail = (GoodsReceiptDocumentDetail) em.find(GoodsReceiptDocumentDetail.class, id);
        return goodsReceiptDocumentDetail;
    }

    @Override
    public Stock getStock(Long id) {
        stock = (Stock) em.find(Stock.class, id);
        return stock;
    }

    @Override
    public PurchaseOrder getPurchaseOrder(Long id) {
        purchaseOrder = (PurchaseOrder) em.find(PurchaseOrder.class, id);
        return purchaseOrder;
    }

    @Override
    public GoodsReceiptDocument createGoodsReceiptDocument(Plant plant, Calendar postingDate) {
        goodsReceiptDocument = new GoodsReceiptDocument();
        goodsReceiptDocument.setPlant(plant);
        goodsReceiptDocument.setPostingDate(postingDate);
        goodsReceiptDocument.setConfirm(false);
        em.persist(goodsReceiptDocument);
        em.flush();
        return goodsReceiptDocument;
    }

    @Override
    public void updateIncomingShipmentStatus(Long id) {
        goodsIssuedDocument = getGoodsIssuedDocument(id);
        goodsIssuedDocument.setReceived(true);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

    @Override
    public GoodsReceiptDocument createGoodsReceiptDocumentfromInbound(Plant plant, Calendar receiptDate) {
        goodsReceiptDocument = new GoodsReceiptDocument();
        goodsReceiptDocument.setPlant(plant);
        goodsReceiptDocument.setConfirm(false);
        goodsReceiptDocument.setReceiptDate(receiptDate);
        em.persist(goodsReceiptDocument);
        em.flush();
        return goodsReceiptDocument;
    }

    @Override
    public void createGoodsReceiptDocumentStockUnit(Long grdId, Calendar postingDate) {
        goodsReceiptDocument = getGoodsReceiptDocument(grdId);
        goodsReceiptDocument.setConfirm(true);
        goodsReceiptDocument.setPostingDate(postingDate);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

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

    @Override
    public void editGoodsReceiptDocument(Long goodsReceiptDocumentId, Calendar receiptDate, String deliveryNote) {
        goodsReceiptDocument = getGoodsReceiptDocument(goodsReceiptDocumentId);
        goodsReceiptDocument.setReceiptDate(receiptDate);
        goodsReceiptDocument.setDeliveryNote(deliveryNote);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

    @Override
    public void editGoodsReceiptDocumentPO(Long goodsReceiptDocumentId, PurchaseOrder po) {
        goodsReceiptDocument = getGoodsReceiptDocument(goodsReceiptDocumentId);
        po.setGoodsReceiptDocument(goodsReceiptDocument);
        em.merge(po);
        em.flush();
        goodsReceiptDocument.setReceiveFrom(po);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

    @Override
    public List<PurchaseOrderDetail> viewPurchaseOrderDetail(PurchaseOrder po) {
        Query q = em.createQuery("SELECT s FROM PurchaseOrderDetail s WHERE s.purchaseOrder.id=:id");
        q.setParameter("id", po.getId());
        return q.getResultList();
    }

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

    @Override
    public void editGoodsReceiptDocumentDetailQty(Long grddId, Integer qty) {
        goodsReceiptDocumentDetail = getGoodsReceiptDocumentDetail(grddId);
        goodsReceiptDocumentDetail.setQuantity(qty);
        em.merge(goodsReceiptDocumentDetail);
        em.flush();
        em.refresh(goodsReceiptDocumentDetail);
    }

    @Override
    public List<PurchaseOrder> viewPurchaseOrder(Plant plant) {
        Query q = em.createQuery("SELECT s FROM PurchaseOrder s WHERE s.shipsTo.id=:plantId AND s.status=:status");
        q.setParameter("status", PurchaseOrderStatus.CONFIRMED);
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<GoodsReceiptDocument> viewGoodsReceiptDocument(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsReceiptDocument s WHERE s.confirm=FALSE AND s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<GoodsIssuedDocument> viewInboundShipment(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.confirm=TRUE AND s.deliverTo.id=:plantId AND s.received=FALSE");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<GoodsIssuedDocumentDetail> viewInboundShipmentByDetail(Long id) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocumentDetail s WHERE s.goodsIssuedDocument.id=:id");
        q.setParameter("id", id);
        return q.getResultList();
    }

    @Override
    public List<GoodsReceiptDocument> viewGoodsReceiptDocumentPosted(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsReceiptDocument s WHERE s.confirm=TRUE AND s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<GoodsReceiptDocument> viewGoodsReceiptDocumentIndividual(GoodsReceiptDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsReceiptDocument s WHERE s.id=" + grd.getId());
        return q.getResultList();
    }

    @Override
    public List<GoodsReceiptDocumentDetail> viewGoodsReceiptDocumentDetail(GoodsReceiptDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsReceiptDocumentDetail s WHERE s.goodsReceiptDocument.id=" + grd.getId());
        return q.getResultList();
    }

    @Override
    public List<Stock> viewStock() {
        Query q = em.createQuery("SELECT s " + "FROM Stock s");
        return q.getResultList();
    }

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

    @Override
    public void deleteGoodsReceiptDocumentDetail(Long goodsReceiptDocumentDetailId) {
        goodsReceiptDocumentDetail = getGoodsReceiptDocumentDetail(goodsReceiptDocumentDetailId);
        em.remove(goodsReceiptDocumentDetail);
        em.flush();
    }

    @Override
    public List<StorageArea> viewStorageArea(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageArea s WHERE s.plant.id=" + plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StorageBin> viewStorageBin(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }

}
