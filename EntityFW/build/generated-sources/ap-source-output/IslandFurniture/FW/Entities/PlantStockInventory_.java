package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Plant;
import IslandFurniture.FW.Entities.Stock;
import java.sql.Time;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(PlantStockInventory.class)
public class PlantStockInventory_ { 

    public static volatile SingularAttribute<PlantStockInventory, Time> recordTime;
    public static volatile SingularAttribute<PlantStockInventory, Plant> plant;
    public static volatile SingularAttribute<PlantStockInventory, Long> id;
    public static volatile SingularAttribute<PlantStockInventory, Stock> stock;

}