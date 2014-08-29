package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.GoodsReceiptDocument;
import IslandFurniture.FW.Entities.Stock;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(GoodsReceiptDocumentDetail.class)
public class GoodsReceiptDocumentDetail_ { 

    public static volatile SingularAttribute<GoodsReceiptDocumentDetail, Long> id;
    public static volatile SingularAttribute<GoodsReceiptDocumentDetail, Stock> receivedStock;
    public static volatile SingularAttribute<GoodsReceiptDocumentDetail, GoodsReceiptDocument> goodsReceiptDocument;

}