package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.PurchaseOrder;
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
    private GoodsReceiptDocumentDetail goodsReceiptDocumentDetail;
    private Stock stock;

    @Override
    public GoodsReceiptDocument getGoodsReceiptDocument(Long id) {
        goodsReceiptDocument = (GoodsReceiptDocument) em.find(GoodsReceiptDocument.class, id);
        return goodsReceiptDocument;
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
    public void createGoodsReceiptDocumentStockUnit(Long grdId) {
        goodsReceiptDocument = getGoodsReceiptDocument(grdId);
        goodsReceiptDocument.setConfirm(true);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

    @Override
    public void createGoodsReceiptDocumentDetail(Long grdId, Long stockId, Integer quantity) {
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
    public void editGoodsReceiptDocument(Long goodsReceiptDocumentId, Calendar receiptDate, PurchaseOrder po, String deliveryNote) {
        goodsReceiptDocument = getGoodsReceiptDocument(goodsReceiptDocumentId);
        goodsReceiptDocument.setReceiptDate(receiptDate);
        goodsReceiptDocument.setReceiveFrom(po);
        goodsReceiptDocument.setDeliveryNote(deliveryNote);
        em.merge(goodsReceiptDocument);
        em.flush();
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
    public List<GoodsReceiptDocument> viewGoodsReceiptDocument() {
        Query q = em.createQuery("SELECT s " + "FROM GoodsReceiptDocument s");
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
