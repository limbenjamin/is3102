package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Cart;
import IslandFurniture.FW.Entities.FurnitureModel;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(CartItem.class)
public class CartItem_ { 

    public static volatile SingularAttribute<CartItem, Integer> qty;
    public static volatile SingularAttribute<CartItem, Long> id;
    public static volatile SingularAttribute<CartItem, FurnitureModel> furnitureModel;
    public static volatile SingularAttribute<CartItem, Cart> cart;

}