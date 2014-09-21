/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

/**
 *
 * @author KamilulAshraf
 */
public interface ManageGoodsIssuedLocal {

    GoodsIssuedDocument createGoodsIssuedDocument(Plant plant, Calendar postingDate);

    void createGoodsIssuedDocumentDetail(Long grdId, Long stockId, Integer quantity);

    void createGoodsIssuedDocumentStockUnit(Long grdId, Calendar postingDate);

    void deleteGoodsIssuedDocument(Long goodsIssuedDocumentId);

    void deleteGoodsIssuedDocumentDetail(Long goodsIssuedDocumentDetailId);

    void editGoodsIssuedDocument(Long goodsIssuedDocumentId, Calendar IssuedDate);

    void editGoodsIssuedDocumentDetail(Long grddId, Long stockId, Integer qty);

    GoodsIssuedDocument getGoodsIssuedDocument(Long id);

    GoodsIssuedDocumentDetail getGoodsIssuedDocumentDetail(Long id);

    Stock getStock(Long id);

    List<GoodsIssuedDocument> viewGoodsIssuedDocument();

    List<GoodsIssuedDocumentDetail> viewGoodsIssuedDocumentDetail(GoodsIssuedDocument grd);

    List<GoodsIssuedDocument> viewGoodsIssuedDocumentIndividual(GoodsIssuedDocument grd);

    List<GoodsIssuedDocument> viewGoodsIssuedDocumentPosted();

    List<Stock> viewStock();

    List<StorageArea> viewStorageArea(Plant plant);

    List<StorageBin> viewStorageBin(Plant plant);
    
}
