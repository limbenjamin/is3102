package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.PurchaseOrder;
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

    @Override
    public GoodsReceiptDocument getGoodsReceiptDocument(Long id) {
        goodsReceiptDocument = (GoodsReceiptDocument) em.find(GoodsReceiptDocument.class, id);
        return goodsReceiptDocument;
    }

    @Override
    public void createGoodsReceiptDocument(Plant plant, Calendar postingDate) {  
        goodsReceiptDocument = new GoodsReceiptDocument();
//        goodsReceiptDocument.setPlant(plant);
        goodsReceiptDocument.setPostingDate(postingDate);
        goodsReceiptDocument.setDocumentDate(null);
        goodsReceiptDocument.setReceiveFrom(null);
        goodsReceiptDocument.setDeliveryNote(null);
        em.persist(goodsReceiptDocument);
    }

    @Override
    public void editGoodsReceiptDocument(Long goodsReceiptDocumentId, Calendar postingDate, Calendar documentDate, PurchaseOrder po, String deliveryNote) {
        goodsReceiptDocument = getGoodsReceiptDocument(goodsReceiptDocumentId);
        goodsReceiptDocument.setPostingDate(postingDate);
        goodsReceiptDocument.setDocumentDate(documentDate);
        goodsReceiptDocument.setReceiveFrom(po);
        goodsReceiptDocument.setDeliveryNote(deliveryNote);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

    @Override
    public List<GoodsReceiptDocument> viewGoodsReceiptDocument() {
        Query q = em.createQuery("SELECT s " + "FROM GoodsReceiptDocument s");
        return q.getResultList();
    }

    @Override
    public void deleteGoodsReceiptDocument(Long goodsReceiptDocumentId) {
        goodsReceiptDocument = getGoodsReceiptDocument(goodsReceiptDocumentId);
        em.remove(goodsReceiptDocument);
        em.flush();
    }
}
