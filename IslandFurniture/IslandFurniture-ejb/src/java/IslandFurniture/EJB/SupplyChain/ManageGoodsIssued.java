package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockUnit;
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
public class ManageGoodsIssued implements ManageGoodsIssuedLocal {

    @PersistenceContext
    EntityManager em;

    private GoodsIssuedDocument goodsIssuedDocument;
    private GoodsIssuedDocumentDetail goodsIssuedDocumentDetail;
    private Stock stock;
    private StockUnit stockUnit;
    private Plant plant;

    @Override
    public GoodsIssuedDocument getGoodsIssuedDocument(Long id) {
        goodsIssuedDocument = (GoodsIssuedDocument) em.find(GoodsIssuedDocument.class, id);
        return goodsIssuedDocument;
    }

    @Override
    public GoodsIssuedDocumentDetail getGoodsIssuedDocumentDetail(Long id) {
        goodsIssuedDocumentDetail = (GoodsIssuedDocumentDetail) em.find(GoodsIssuedDocumentDetail.class, id);
        return goodsIssuedDocumentDetail;
    }

    @Override
    public Stock getStock(Long id) {
        stock = (Stock) em.find(Stock.class, id);
        return stock;
    }

    @Override
    public Plant getPlant(Long id) {
        plant = (Plant) em.find(Plant.class, id);
        return plant;
    }

    @Override
    public StockUnit getStockUnit(Long id) {
        stockUnit = (StockUnit) em.find(StockUnit.class, id);
        return stockUnit;
    }

    @Override
    public GoodsIssuedDocument createGoodsIssuedDocument(Plant plant, Calendar postingDate) {
        goodsIssuedDocument = new GoodsIssuedDocument();
        goodsIssuedDocument.setPlant(plant);
        goodsIssuedDocument.setPostingDate(postingDate);
        goodsIssuedDocument.setConfirm(false);
        goodsIssuedDocument.setReceived(false);
        em.persist(goodsIssuedDocument);
        em.flush();
        return goodsIssuedDocument;
    }

    @Override
    public void createGoodsIssuedDocumentStockUnit(Long grdId, Calendar postingDate) {
        goodsIssuedDocument = getGoodsIssuedDocument(grdId);
        goodsIssuedDocument.setConfirm(true);
        goodsIssuedDocument.setPostingDate(postingDate);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

    @Override
    public void createGoodsIssuedDocumentDetail(Long grdId, Long stockId, Long quantity) {
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

    @Override
    public void editGoodsIssuedDocument(Long goodsIssuedDocumentId, Calendar issuedDate, Plant plant) {
        goodsIssuedDocument = getGoodsIssuedDocument(goodsIssuedDocumentId);
        goodsIssuedDocument.setIssuedDate(issuedDate);
        goodsIssuedDocument.setDeliverTo(plant);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

    @Override
    public void editGoodsIssuedDocument2(Long goodsIssuedDocumentId, Calendar postingDate) {
        goodsIssuedDocument = getGoodsIssuedDocument(goodsIssuedDocumentId);
        goodsIssuedDocument.setPostingDate(postingDate);
        goodsIssuedDocument.setConfirm(true);
        em.merge(goodsIssuedDocument);
        em.flush();
    }

    @Override
    public List<GoodsIssuedDocument> viewGoodsIssuedDocument(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.confirm=FALSE AND s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<GoodsIssuedDocument> viewGoodsIssuedDocumentPosted(Plant plant) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.confirm=TRUE AND s.plant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<GoodsIssuedDocument> viewGoodsIssuedDocumentIndividual(GoodsIssuedDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocument s WHERE s.id=" + grd.getId());
        return q.getResultList();
    }

    @Override
    public List<GoodsIssuedDocumentDetail> viewGoodsIssuedDocumentDetail(GoodsIssuedDocument grd) {
        Query q = em.createQuery("SELECT s FROM GoodsIssuedDocumentDetail s WHERE s.goodsIssuedDocument.id=" + grd.getId());
        return q.getResultList();
    }

    @Override
    public List<Stock> viewStock() {
        Query q = em.createQuery("SELECT s " + "FROM Stock s");
        return q.getResultList();
    }

    @Override
    public List<Plant> viewPlant() {
        Query q = em.createQuery("SELECT s FROM Plant s");
        return q.getResultList();
    }

    @Override
    public void deleteGoodsIssuedDocument(Long goodsIssuedDocumentId) {
        goodsIssuedDocument = getGoodsIssuedDocument(goodsIssuedDocumentId);
        em.remove(goodsIssuedDocument);
        em.flush();
    }

    @Override
    public void deleteGoodsIssuedDocumentDetail(Long goodsIssuedDocumentDetailId) {
        goodsIssuedDocumentDetail = getGoodsIssuedDocumentDetail(goodsIssuedDocumentDetailId);
        em.remove(goodsIssuedDocumentDetail);
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

    @Override
    public List<StockUnit> viewStockUnitById(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE (s.location.storageArea.plant.id=:plantId AND s.stock.id=:stockId AND s.available=TRUE)");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        return q.getResultList();
    }
    
    @Override
    public List<StockUnit> viewStockUnitById2(Plant plant, Stock stock, GoodsIssuedDocument gid) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId AND s.stock.id=:stockId AND s.available=FALSE AND s.goodsIssuedDocument.id=:gidId");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        q.setParameter("gidId", gid.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitByIdMain(Plant plant, GoodsIssuedDocument gid) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId AND s.available=FALSE AND s.goodsIssuedDocument.id=:gidId");
        q.setParameter("plantId", plant.getId());
        q.setParameter("gidId", gid.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitByIdAndGrdId(Stock stock, GoodsIssuedDocument gid) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.stock.id=:stockId AND s.goodsIssuedDocument.id=:gidId");
        q.setParameter("stockId", stock.getId());
        q.setParameter("gidId", gid.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnit(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId GROUP BY s.stock.name");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

}
