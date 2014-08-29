package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.RestaurantPurchaseOrderDetail;
import IslandFurniture.FW.Entities.Store;
import IslandFurniture.FW.Entities.Supplier;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(RestaurantPurchaseOrder.class)
public class RestaurantPurchaseOrder_ { 

    public static volatile SingularAttribute<RestaurantPurchaseOrder, Supplier> supplier;
    public static volatile ListAttribute<RestaurantPurchaseOrder, RestaurantPurchaseOrderDetail> purchaseOrderDetails;
    public static volatile SingularAttribute<RestaurantPurchaseOrder, Long> id;
    public static volatile SingularAttribute<RestaurantPurchaseOrder, Store> store;

}