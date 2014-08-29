package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.GoodsReceiptDocument;
import IslandFurniture.FW.Entities.Plant;
import IslandFurniture.FW.Entities.PurchaseOrderDetail;
import IslandFurniture.FW.Entities.Supplier;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(PurchaseOrder.class)
public class PurchaseOrder_ { 

    public static volatile SingularAttribute<PurchaseOrder, Plant> shipsTo;
    public static volatile ListAttribute<PurchaseOrder, Supplier> suppliers;
    public static volatile ListAttribute<PurchaseOrder, PurchaseOrderDetail> purchaseOrderDetails;
    public static volatile SingularAttribute<PurchaseOrder, Long> id;
    public static volatile SingularAttribute<PurchaseOrder, GoodsReceiptDocument> goodsReceiptDocument;

}