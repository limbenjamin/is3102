package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.MenuItem;
import IslandFurniture.FW.Entities.Store;
import IslandFurniture.FW.Enums.Month;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(MonthlyMenuItemSalesForecast.class)
public class MonthlyMenuItemSalesForecast_ { 

    public static volatile SingularAttribute<MonthlyMenuItemSalesForecast, Month> month;
    public static volatile SingularAttribute<MonthlyMenuItemSalesForecast, Integer> year;
    public static volatile SingularAttribute<MonthlyMenuItemSalesForecast, Store> store;
    public static volatile SingularAttribute<MonthlyMenuItemSalesForecast, MenuItem> menuItem;

}