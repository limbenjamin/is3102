package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.FW.Entities.PurchaseOrder;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(GoodsReceiptDocument.class)
public class GoodsReceiptDocument_ { 

    public static volatile ListAttribute<GoodsReceiptDocument, GoodsReceiptDocumentDetail> goodsReceiptDocumentDetails;
    public static volatile SingularAttribute<GoodsReceiptDocument, PurchaseOrder> receiveFrom;
    public static volatile SingularAttribute<GoodsReceiptDocument, Long> id;

}