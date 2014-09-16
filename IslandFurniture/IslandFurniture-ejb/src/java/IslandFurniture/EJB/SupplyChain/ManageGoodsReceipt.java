
package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
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
public class ManageGoodsReceipt implements ManageGoodsReceiptLocal  {

    @PersistenceContext
    EntityManager em;

    private GoodsReceiptDocument goodsReceiptDocument;

  
    @Override
    public GoodsReceiptDocument getGoodsReceiptDocument(Long id) {
        goodsReceiptDocument = (GoodsReceiptDocument) em.find(GoodsReceiptDocument.class, id);
        System.out.println("this is the goodsReceiptDocument id "+ id);
        return goodsReceiptDocument;
    }
    
    
    @Override
        public void createGoodsReceiptDocument(Calendar postingDate, Calendar documentDate) {
        goodsReceiptDocument = new GoodsReceiptDocument();
        goodsReceiptDocument.setPostingDate(postingDate);
        goodsReceiptDocument.setDocumentDate(documentDate);
        goodsReceiptDocument.setReceiveFrom(null);
        goodsReceiptDocument.setGoodsReceiptDocumentDetails(null);
        em.persist(goodsReceiptDocument);
    }
    
    
    @Override
    public void editGoodsReceiptDocument(Long id, Calendar postingDate, Calendar documentDate) {
        goodsReceiptDocument = getGoodsReceiptDocument(id);
        goodsReceiptDocument.setPostingDate(postingDate);
        goodsReceiptDocument.setDocumentDate(documentDate);
        goodsReceiptDocument.setReceiveFrom(null);
        goodsReceiptDocument.setGoodsReceiptDocumentDetails(null);
        em.merge(goodsReceiptDocument);
        em.flush();
    }

   
    @Override
    public void deleteGoodsReceiptDocument(Long id) {
        goodsReceiptDocument = getGoodsReceiptDocument(id);
        em.remove(goodsReceiptDocument);
        em.flush();
    }

   
    @Override
    public List<GoodsReceiptDocument> viewGoodsReceiptDocument() {
        Query q = em.createQuery("SELECT s " + "FROM GoodsReceiptDocument s");
        return q.getResultList();
    }
}
