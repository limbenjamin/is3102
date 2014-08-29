package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Ingredient;
import IslandFurniture.FW.Entities.RestaurantGoodsReceiptDocumentDetail;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(RestaurantGoodReceiptDocument.class)
public class RestaurantGoodReceiptDocument_ { 

    public static volatile SingularAttribute<RestaurantGoodReceiptDocument, Ingredient> ingredient;
    public static volatile ListAttribute<RestaurantGoodReceiptDocument, List> suppliers;
    public static volatile ListAttribute<RestaurantGoodReceiptDocument, RestaurantGoodsReceiptDocumentDetail> restaurantGoodsReceiptDocumentDetails;
    public static volatile SingularAttribute<RestaurantGoodReceiptDocument, Long> id;

}