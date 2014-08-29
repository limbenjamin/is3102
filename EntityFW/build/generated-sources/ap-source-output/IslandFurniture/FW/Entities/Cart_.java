package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.CartItem;
import IslandFurniture.FW.Entities.Store;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Cart.class)
public class Cart_ { 

    public static volatile SingularAttribute<Cart, Long> id;
    public static volatile SingularAttribute<Cart, Store> store;
    public static volatile ListAttribute<Cart, CartItem> cartItems;

}