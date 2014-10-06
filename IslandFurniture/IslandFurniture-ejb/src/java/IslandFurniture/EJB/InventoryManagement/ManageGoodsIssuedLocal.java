/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageGoodsIssuedLocal {

    //  Function: To create Goods Issued Document
    GoodsIssuedDocument createGoodsIssuedDocument(Plant plant);

    // Function: To create Goods Issued Document Detail
    void createGoodsIssuedDocumentDetail(Long grdId, Long stockId, Long quantity);

    //  Function: To delete Goods Issued Document
    void deleteGoodsIssuedDocument(Long goodsIssuedDocumentId);

    //  Function: To delete Goods Issued Document Detail
    void deleteGoodsIssuedDocumentDetail(Long goodsIssuedDocumentDetailId);

    //  Function: To edit Goods Issued Document
    void editGoodsIssuedDocument(Long goodsIssuedDocumentId, Calendar issuedDate, Plant plant);

    //  Function: To get GoodsIssuedDocument entity based on GoodIssuedDocumentId
    GoodsIssuedDocument getGoodsIssuedDocument(Long id);

    //  Function: To get GoodsIssuedDocumentDetail entity based on GoodIssuedDocumentDetailId
    GoodsIssuedDocumentDetail getGoodsIssuedDocumentDetail(Long id);

    //  Function: To post Goods Issued Document
    void postGoodsIssuedDocument(Long grdId, Calendar postingDate);

    //  Function: To display Goods Issued Document
    List<GoodsIssuedDocument> viewGoodsIssuedDocument(Plant plant);

    //  Function: To display Goods Issued Document Detail
    List<GoodsIssuedDocumentDetail> viewGoodsIssuedDocumentDetail(GoodsIssuedDocument grd);

    //  Function: To display details on the Goods Issued Document
    List<GoodsIssuedDocument> viewGoodsIssuedDocumentIndividual(GoodsIssuedDocument grd);

    //  Functiuon: To display Goods Issued Document Posted
    List<GoodsIssuedDocument> viewGoodsIssuedDocumentPosted(Plant plant);

    //  Function: To display Stock Units of the particular Stock and tagged to a particular Goods Issued Document
    List<StockUnit> viewStockUnitByStockandGID(Stock stock, GoodsIssuedDocument gid);

    //  Function: To display pending movement Stock Units of the particular Stock and tagged to a particular Goods Issued Document
    List<StockUnit> viewStockUnitPendingMovementAtGIDForAParticularStock(Stock stock, GoodsIssuedDocument gid);

    //  Function: To display list of Stock Units pending at the Goods Issued Document
    List<StockUnit> viewStockUnitPendingMovementAtGoodsIssuedDocument(GoodsIssuedDocument gid);
    
}
