package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.GoodsIssuedDocumentDetail;
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
public class ManageGoodsIssued {

    @PersistenceContext
    EntityManager em;

    private GoodsIssuedDocument goodsIssuedDocument;
    private GoodsIssuedDocumentDetail goodsIssuedDocumentDetail;
    private Stock stock;

   
    public GoodsIssuedDocument getGoodsIssuedDocument(Long id) {
        goodsIssuedDocument = (GoodsIssuedDocument) em.find(GoodsIssuedDocument.class, id);
        return goodsIssuedDocument;
    }

  
    public GoodsIssuedDocumentDetail getGoodsIssuedDocumentDetail(Long id) {
        goodsIssuedDocumentDetail = (GoodsIssuedDocumentDetail) em.find(GoodsIssuedDocumentDetail.class, id);
        return goodsIssuedDocumentDetail;
    }

   
    public Stock getStock(Long id) {
        stock = (Stock) em.find(Stock.class, id);
        return stock;
    }

  
    public GoodsIssuedDocument createGoodsIssuedDocument(Plant plant, Calendar postingDate) {
        goodsIssuedDocument = new GoodsIssuedDocument();
        goodsIssuedDocument.setPlant(plant);
        goodsIssuedDocument.setPostingDate(postingDate);
        goodsIssuedDocument.setConfirm(false);
        em.persist(goodsIssuedDocument);
        em.flush();
        return goodsIssuedDocument;
    }

    public void createGoodsIssuedDocumentStockUnit(Long grdId, Calendar postingDate) {
        goodsIssuedDocument = getGoodsIssuedDocument(grdId);
        goodsIssuedDocument.setConfirm(true);
        goodsIssuedDocument.setPostingDate(postingDate);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

  
    public void createGoodsIssuedDocumentDetail(Long grdId, Long stockId, Integer quantity) {
        goodsIssuedDocumentDetail = new GoodsIssuedDocumentDetail();
        goodsIssuedDocument = getGoodsIssuedDocument(grdId);
        stock = getStock(stockId);
        goodsIssuedDocumentDetail.setGoodsIssuedDocument(goodsIssuedDocument);
        goodsIssuedDocumentDetail.setStock(stock);
        goodsIssuedDocumentDetail.setQuantity(quantity);
        goodsIssuedDocument.getGoodsIssuedDocumentDetails().add(goodsIssuedDocumentDetail);
        em.persist(goodsIssuedDocumentDetail);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

  
    public void editGoodsIssuedDocument(Long goodsIssuedDocumentId, Calendar IssuedDate, PurchaseOrder po, String deliveryNote) {
        goodsIssuedDocument = getGoodsIssuedDocument(goodsIssuedDocumentId);
        goodsIssuedDocument.setIssuedDate(IssuedDate);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

   
    public void editGoodsIssuedDocumentDetail(Long grddId, Long stockId, Integer qty) {
        goodsIssuedDocumentDetail = getGoodsIssuedDocumentDetail(grddId);
        stock = getStock(stockId);
        goodsIssuedDocumentDetail.setGoodsIssuedDocument(goodsIssuedDocument);
        goodsIssuedDocumentDetail.setStock(stock);
        goodsIssuedDocumentDetail.setQuantity(qty);
        em.merge(goodsIssuedDocumentDetail);
        em.flush();
    }

  
    public List<GoodsIssuedDocument> viewGoodsIssuedDocument() {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.confirm=FALSE");
        return q.getResultList();
    }
    
    public List<GoodsIssuedDocument> viewGoodsIssuedDocumentPosted() {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.confirm=TRUE");
        return q.getResultList();
    }

    
    public List<GoodsIssuedDocument> viewGoodsIssuedDocumentIndividual(GoodsIssuedDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.id=" + grd.getId());
        return q.getResultList();
    }

   
    public List<GoodsIssuedDocumentDetail> viewGoodsIssuedDocumentDetail(GoodsIssuedDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocumentDetail s WHERE s.goodsIssuedDocument.id=" + grd.getId());
        return q.getResultList();
    }

  
    public List<Stock> viewStock() {
        Query q = em.createQuery("SELECT s " + "FROM Stock s");
        return q.getResultList();
    }

   
    public void deleteGoodsIssuedDocument(Long goodsIssuedDocumentId) {
        goodsIssuedDocument = getGoodsIssuedDocument(goodsIssuedDocumentId);
        em.remove(goodsIssuedDocument);
        em.flush();
    }

    
    public void deleteGoodsIssuedDocumentDetail(Long goodsIssuedDocumentDetailId) {
        goodsIssuedDocumentDetail = getGoodsIssuedDocumentDetail(goodsIssuedDocumentDetailId);
        em.remove(goodsIssuedDocumentDetail);
        em.flush();
    }

   
    public List<StorageArea> viewStorageArea(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageArea s WHERE s.plant.id=" + plant.getId());
        return q.getResultList();
    }

   
    public List<StorageBin> viewStorageBin(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }
}


