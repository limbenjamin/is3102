package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Stock;
import IslandFurniture.FW.Entities.StorageLocation;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(StockUnit.class)
public class StockUnit_ { 

    public static volatile SingularAttribute<StockUnit, Long> batchNo;
    public static volatile SingularAttribute<StockUnit, Long> qty;
    public static volatile SingularAttribute<StockUnit, StorageLocation> location;
    public static volatile SingularAttribute<StockUnit, Long> id;
    public static volatile SingularAttribute<StockUnit, Stock> stock;

}