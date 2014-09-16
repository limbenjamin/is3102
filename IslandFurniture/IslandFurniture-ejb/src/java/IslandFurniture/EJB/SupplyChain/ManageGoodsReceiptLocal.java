/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageGoodsReceiptLocal {

    void createGoodsReceiptDocument(Calendar postingDate, Calendar documentDate);

    void deleteGoodsReceiptDocument(Long id);

    void editGoodsReceiptDocument(Long id, Calendar postingDate, Calendar documentDate);

    GoodsReceiptDocument getGoodsReceiptDocument(Long id);

    List<GoodsReceiptDocument> viewGoodsReceiptDocument();
    
}
