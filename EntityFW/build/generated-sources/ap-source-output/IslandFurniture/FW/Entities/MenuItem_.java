package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Dish;
import IslandFurniture.FW.Entities.Menu;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(MenuItem.class)
public class MenuItem_ { 

    public static volatile ListAttribute<MenuItem, Dish> dishes;
    public static volatile SingularAttribute<MenuItem, Long> id;
    public static volatile ListAttribute<MenuItem, Menu> menus;

}