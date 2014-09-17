/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageGoodsReceiptLocal {

    void createGoodsReceiptDocument(Plant plant, Calendar postingDate);

    void deleteGoodsReceiptDocument(Long goodsReceiptDocumentId);

    void editGoodsReceiptDocument(Long goodsReceiptDocumentId, Calendar documentDate, PurchaseOrder po, String deliveryNote);

    GoodsReceiptDocument getGoodsReceiptDocument(Long id);

    List<GoodsReceiptDocument> viewGoodsReceiptDocument();
    
}
