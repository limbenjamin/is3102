package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
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
public class ManageGoodsIssued implements ManageGoodsIssuedLocal {

    @PersistenceContext
    EntityManager em;

    private GoodsIssuedDocument goodsIssuedDocument;
    private GoodsIssuedDocumentDetail goodsIssuedDocumentDetail;
    private Stock stock;
    private StockUnit stockUnit;
    private Plant plant;

//  Function: To get GoodsIssuedDocument entity based on GoodIssuedDocumentId   
    @Override
    public GoodsIssuedDocument getGoodsIssuedDocument(Long id) {
        goodsIssuedDocument = (GoodsIssuedDocument) em.find(GoodsIssuedDocument.class, id);
        return goodsIssuedDocument;
    }

//  Function: To get GoodsIssuedDocumentDetail entity based on GoodIssuedDocumentDetailId       
    @Override
    public GoodsIssuedDocumentDetail getGoodsIssuedDocumentDetail(Long id) {
        goodsIssuedDocumentDetail = (GoodsIssuedDocumentDetail) em.find(GoodsIssuedDocumentDetail.class, id);
        return goodsIssuedDocumentDetail;
    }

//  Function: To create Goods Issued Document    
    @Override
    public GoodsIssuedDocument createGoodsIssuedDocument(Plant plant) {
        goodsIssuedDocument = new GoodsIssuedDocument();
        goodsIssuedDocument.setPlant(plant);
        goodsIssuedDocument.setPostingDate(null);
        goodsIssuedDocument.setConfirm(false);
        goodsIssuedDocument.setReceived(false);
        em.persist(goodsIssuedDocument);
        em.flush();
        return goodsIssuedDocument;
    }

// Function: To create Goods Issued Document Detail    
    @Override
    public void createGoodsIssuedDocumentDetail(Long grdId, Long stockId, Long quantity) {
        goodsIssuedDocumentDetail = new GoodsIssuedDocumentDetail();
        goodsIssuedDocument = getGoodsIssuedDocument(grdId);
        stock = (Stock) em.find(Stock.class, stockId);
        goodsIssuedDocumentDetail.setGoodsIssuedDocument(goodsIssuedDocument);
        goodsIssuedDocumentDetail.setStock(stock);
        goodsIssuedDocumentDetail.setQuantity(quantity);
        goodsIssuedDocument.getGoodsIssuedDocumentDetails().add(goodsIssuedDocumentDetail);
        em.persist(goodsIssuedDocumentDetail);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

//  Function: To edit Goods Issued Document
    @Override
    public void editGoodsIssuedDocument(Long goodsIssuedDocumentId, Calendar issuedDate, Plant plant) {
        goodsIssuedDocument = getGoodsIssuedDocument(goodsIssuedDocumentId);
        goodsIssuedDocument.setIssuedDate(issuedDate);
        goodsIssuedDocument.setDeliverTo(plant);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

//  Function: To post Goods Issued Document    
    @Override
    public void postGoodsIssuedDocument(Long grdId, Calendar postingDate) {
        goodsIssuedDocument = getGoodsIssuedDocument(grdId);
        goodsIssuedDocument.setConfirm(true);
        goodsIssuedDocument.setPostingDate(postingDate);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

//  Function: To display Goods Issued Document    
    @Override
    public List<GoodsIssuedDocument> viewGoodsIssuedDocument(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.confirm=FALSE AND s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Functiuon: To display Goods Issued Document Posted    
    @Override
    public List<GoodsIssuedDocument> viewGoodsIssuedDocumentPosted(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.confirm=TRUE AND s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//  Function: To display Goods Issued Document Detail    
    @Override
    public List<GoodsIssuedDocumentDetail> viewGoodsIssuedDocumentDetail(GoodsIssuedDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocumentDetail s WHERE s.goodsIssuedDocument.id=" + grd.getId());
        return q.getResultList();
    }

//  Function: To display list of Stock Units pending at the Goods Issued Document
    @Override
    public List<StockUnit> viewStockUnitPendingMovementAtGoodsIssuedDocument(GoodsIssuedDocument gid) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.available=FALSE AND s.goodsIssuedDocument.id=:gidId");
        q.setParameter("gidId", gid.getId());
        return q.getResultList();
    }

//  Function: To delete Goods Issued Document    
    @Override
    public void deleteGoodsIssuedDocument(Long goodsIssuedDocumentId) {
        goodsIssuedDocument = getGoodsIssuedDocument(goodsIssuedDocumentId);
        em.remove(goodsIssuedDocument);
        em.flush();
    }

//  Function: To delete Goods Issued Document Detail    
    @Override
    public void deleteGoodsIssuedDocumentDetail(Long goodsIssuedDocumentDetailId) {
        goodsIssuedDocumentDetail = getGoodsIssuedDocumentDetail(goodsIssuedDocumentDetailId);
        em.remove(goodsIssuedDocumentDetail);
        em.flush();
    }

//  Function: To display details on the Goods Issued Document
    @Override
    public List<GoodsIssuedDocument> viewGoodsIssuedDocumentIndividual(GoodsIssuedDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.id=" + grd.getId());
        return q.getResultList();
    }

//  Function: To display Stock Units of the particular Stock and tagged to a particular Goods Issued Document    
    @Override
    public List<StockUnit> viewStockUnitByStockandGID(Stock stock, GoodsIssuedDocument gid) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.stock.id=:stockId AND s.goodsIssuedDocument.id=:gidId");
        q.setParameter("stockId", stock.getId());
        q.setParameter("gidId", gid.getId());
        return q.getResultList();
    }

//  Function: To display pending movement Stock Units of the particular Stock and tagged to a particular Goods Issued Document      
    @Override
    public List<StockUnit> viewStockUnitPendingMovementAtGIDForAParticularStock(Stock stock, GoodsIssuedDocument gid) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.stock.id=:stockId AND s.available=FALSE AND s.goodsIssuedDocument.id=:gidId");
        q.setParameter("stockId", stock.getId());
        q.setParameter("gidId", gid.getId());
        return q.getResultList();
    }
}
