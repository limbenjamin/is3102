/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author KamilulAshraf
 */
public interface ManageGoodsIssuedLocal {

    GoodsIssuedDocument createGoodsIssuedDocument(Plant plant, Calendar postingDate);

    void createGoodsIssuedDocumentDetail(Long grdId, Long stockId, Long quantity);

    void createGoodsIssuedDocumentStockUnit(Long grdId, Calendar postingDate);

    void deleteGoodsIssuedDocument(Long goodsIssuedDocumentId);

    void deleteGoodsIssuedDocumentDetail(Long goodsIssuedDocumentDetailId);

    GoodsIssuedDocument getGoodsIssuedDocument(Long id);

    GoodsIssuedDocumentDetail getGoodsIssuedDocumentDetail(Long id);

    Stock getStock(Long id);

    StockUnit getStockUnit(Long id);
    
    Plant getPlant(Long id);

    List<GoodsIssuedDocument> viewGoodsIssuedDocument(Plant plant);

    List<GoodsIssuedDocumentDetail> viewGoodsIssuedDocumentDetail(GoodsIssuedDocument grd);

    List<GoodsIssuedDocument> viewGoodsIssuedDocumentIndividual(GoodsIssuedDocument grd);

    List<GoodsIssuedDocument> viewGoodsIssuedDocumentPosted(Plant plant);

    List<Stock> viewStock();

    List<StockUnit> viewStockUnit(Plant plant);

    List<StockUnit> viewStockUnitById(Plant plant, Stock stock);

    List<StockUnit> viewStockUnitById2(Plant plant, Stock stock, GoodsIssuedDocument gid);

    List<StockUnit> viewStockUnitByIdAndGrdId(Stock stock, GoodsIssuedDocument gid);

    List<StockUnit> viewStockUnitByIdMain(Plant plant, GoodsIssuedDocument gid);

    List<StorageArea> viewStorageArea(Plant plant);

    List<StorageBin> viewStorageBin(Plant plant);

    void editGoodsIssuedDocument(Long goodsIssuedDocumentId, Calendar issuedDate, Long plantId);

    void editGoodsIssuedDocument2(Long goodsIssuedDocumentId, Calendar postingDate);
    
    List<Plant> viewPlant();
    
    

}
