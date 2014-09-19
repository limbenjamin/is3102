/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.Stock;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageGoodsReceiptLocal {

    GoodsReceiptDocument createGoodsReceiptDocument(Plant plant, Calendar postingDate);

    void createGoodsReceiptDocumentDetail(Long grdId, Long stockId);

    void deleteGoodsReceiptDocument(Long goodsReceiptDocumentId);

    void deleteGoodsReceiptDocumentDetail(Long goodsReceiptDocumentDetailId);

    void editGoodsReceiptDocument(Long goodsReceiptDocumentId, Calendar receiptDate, PurchaseOrder po, String deliveryNote);

    void editGoodsReceiptDocumentDetail(Long grddId, Long stockId, Integer qty);

    Stock getStock(Long id);

    GoodsReceiptDocument getGoodsReceiptDocument(Long id);

    GoodsReceiptDocumentDetail getGoodsReceiptDocumentDetail(Long id);

    List<Stock> viewStock();

    List<GoodsReceiptDocument> viewGoodsReceiptDocument();

//    List<GoodsReceiptDocumentDetail> viewGoodsReceiptDocumentDetail();
    List<GoodsReceiptDocumentDetail> viewGoodsReceiptDocumentDetail();

}
