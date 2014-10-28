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
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Entities.Stock;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageGoodsReceiptLocal {

    //  Function: To create a Goods Receipt Document
    GoodsReceiptDocument createGoodsReceiptDocument(Plant plant, Calendar cal);

    //  Function: To create Goods Receipt Document Detail
    void createGoodsReceiptDocumentDetail(Long grdId, Long stockId, Integer quantity);

    //  Function: To create Goods Receipt Document from Goods Issued Document
    GoodsReceiptDocument createGoodsReceiptDocumentfromGoodsIssuedDocument(Plant plant, Calendar receiptDate);

    //  Function: To create Stock Units once the Goods Receipt Document is Posted
    void createStockUnitsFromGoodsReceiptDocument(Long grdId, Calendar postingDate);

    // Function: Delete Goods Receipt Document
    void deleteGoodsReceiptDocument(Long goodsReceiptDocumentId);

    //  Function: Delete Goods Receipt Document Detail
    void deleteGoodsReceiptDocumentDetail(Long goodsReceiptDocumentDetailId);

    //  Function: To edit Goods Receipt Document
    void editGoodsReceiptDocument(Long goodsReceiptDocumentId, Calendar receiptDate, String deliveryNote);

    // Function: To edit Goods Receipt Document Detail
    void editGoodsReceiptDocumentDetail(Long grddId, Long stockId, Integer qty);

    //  Function: To edit/add the quantity of a Goods Receipt Document Detail, when a same Stock is added to the Goods Receipt Document
    void editGoodsReceiptDocumentDetailQtyWhenSameStockIdIsAdded(Long grddId, Integer qty);

    //  Function: To get the Goods Issued Document entity from Id
    GoodsIssuedDocument getGoodsIssuedDocument(Long id);

    //  Function: To get the Goods Recipt Document entity from Id
    GoodsReceiptDocument getGoodsReceiptDocument(Long id);

    //  Function: To get the Goods Recipt Document Detail entity from Id
    GoodsReceiptDocumentDetail getGoodsReceiptDocumentDetail(Long id);

    //  Function: To get the Purchase Order entity from Id
    ProcuredStockPurchaseOrder getPurchaseOrder(Long id);

    //  Function: To get the Stock entity from Id
    Stock getStock(Long id);

    //  @Need to check -- Function: To set the Goods Receipt Document to the Purchase Order
    void setGoodsReceiptDocumentToThePurchaseOrder(Long goodsReceiptDocumentId, ProcuredStockPurchaseOrder po, Calendar date);

    //  Function: To edit Goods Issued Document's Shipment Status to Delivered
    void updateIncomingShipmentStatusToDelivered(Long id);

    //  Function: To diplay list of Goods Receipt Document of a Plant
    List<GoodsReceiptDocument> viewGoodsReceiptDocument(Plant plant);

    //  Function: To display list of Goods Receipt Document Details in a Goods Receipt Document
    List<GoodsReceiptDocumentDetail> viewGoodsReceiptDocumentDetail(GoodsReceiptDocument grd);

    //  @Can be done better -- Function: To return information on the Goods Receipt Document
    List<GoodsReceiptDocument> viewGoodsReceiptDocumentIndividual(GoodsReceiptDocument grd);

    //  Function: To diplay list of Goods Receipt Document Posted
    List<GoodsReceiptDocument> viewGoodsReceiptDocumentPosted(Plant plant);

    //  Function: To diplay list of Inbound Shipment from Goods Issued Document of a Plant
    List<GoodsIssuedDocument> viewInboundShipment(Plant plant);

    //  Function: To return list of items in the Goods Issued Document, to be used to create a Goods Receipt Document Detail
    List<GoodsIssuedDocumentDetail> viewInboundShipmentByDetail(Long id);

    //  Function: To list of Purchase Orders of the Plant with status Confirmed, to be used when populating from POs at Goods Receipt Document
    List<ProcuredStockPurchaseOrder> viewPurchaseOrder(Plant plant);

    //  Function: To return list the Purchase Order Details of a Purchase Order which will be used to create Goods Receipt Document Details
    List<ProcuredStockPurchaseOrderDetail> viewPurchaseOrderDetail(ProcuredStockPurchaseOrder po);
    
}
