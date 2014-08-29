package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Ingredient;
import IslandFurniture.FW.Entities.RestaurantPurchaseOrder;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(RestaurantPurchaseOrderDetail.class)
public class RestaurantPurchaseOrderDetail_ { 

    public static volatile SingularAttribute<RestaurantPurchaseOrderDetail, Ingredient> ingredient;
    public static volatile SingularAttribute<RestaurantPurchaseOrderDetail, RestaurantPurchaseOrder> purchaseOrder;
    public static volatile SingularAttribute<RestaurantPurchaseOrderDetail, Long> id;

}