package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.ManufacturingFacility;
import IslandFurniture.FW.Entities.Stock;
import IslandFurniture.FW.Entities.Store;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(StockSupplied.class)
public class StockSupplied_ { 

    public static volatile SingularAttribute<StockSupplied, Store> store;
    public static volatile SingularAttribute<StockSupplied, Stock> stock;
    public static volatile SingularAttribute<StockSupplied, ManufacturingFacility> manufacturingFacility;

}