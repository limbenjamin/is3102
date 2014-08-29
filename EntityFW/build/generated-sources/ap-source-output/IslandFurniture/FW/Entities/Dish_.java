package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Ingredient;
import IslandFurniture.FW.Entities.MenuItem;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Dish.class)
public class Dish_ { 

    public static volatile ListAttribute<Dish, Ingredient> ingredients;
    public static volatile SingularAttribute<Dish, Long> id;
    public static volatile ListAttribute<Dish, MenuItem> menuItem;

}