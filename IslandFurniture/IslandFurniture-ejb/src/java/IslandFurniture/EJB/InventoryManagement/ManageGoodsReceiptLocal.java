/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.Entities.GoodsReceiptDocument;
import IslandFurniture.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.PurchaseOrder;
import IslandFurniture.Entities.PurchaseOrderDetail;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageGoodsReceiptLocal {

    GoodsReceiptDocument createGoodsReceiptDocument(Plant plant, Calendar postingDate);

    void createGoodsReceiptDocumentDetail(Long grdId, Long stockId, Integer quantity);

    void deleteGoodsReceiptDocument(Long goodsReceiptDocumentId);

    void deleteGoodsReceiptDocumentDetail(Long goodsReceiptDocumentDetailId);

    void editGoodsReceiptDocument(Long goodsReceiptDocumentId, Calendar receiptDate, String deliveryNote);

    void editGoodsReceiptDocumentDetail(Long grddId, Long stockId, Integer qty);

    Stock getStock(Long id);

    GoodsReceiptDocument getGoodsReceiptDocument(Long id);

    GoodsReceiptDocumentDetail getGoodsReceiptDocumentDetail(Long id);

    List<Stock> viewStock();

    List<GoodsReceiptDocument> viewGoodsReceiptDocument(Plant plant);

    List<GoodsReceiptDocumentDetail> viewGoodsReceiptDocumentDetail(GoodsReceiptDocument grd);

    void createGoodsReceiptDocumentStockUnit(Long grdId, Calendar postingDate);

    List<StorageArea> viewStorageArea(Plant plant);

    List<StorageBin> viewStorageBin(Plant plant);

    List<GoodsReceiptDocument> viewGoodsReceiptDocumentIndividual(GoodsReceiptDocument grd);

    List<GoodsReceiptDocument> viewGoodsReceiptDocumentPosted(Plant plant);

    List<GoodsIssuedDocument> viewInboundShipment(Plant plant);

    GoodsReceiptDocument createGoodsReceiptDocumentfromInbound(Plant plant, Calendar receiptDate);

    List<GoodsIssuedDocumentDetail> viewInboundShipmentByDetail(Long id);

    List<PurchaseOrder> viewPurchaseOrder(Plant plant);

    PurchaseOrder getPurchaseOrder(Long id);
    
    GoodsIssuedDocument getGoodsIssuedDocument(Long id);
    
    void updateIncomingShipmentStatus(Long id);
    
    List<PurchaseOrderDetail> viewPurchaseOrderDetail (PurchaseOrder po);
    
    void editGoodsReceiptDocumentPO(Long goodsReceiptDocumentId, PurchaseOrder po, Calendar date);
    
    void editGoodsReceiptDocumentDetailQty(Long grddId, Integer qty);
    
}
