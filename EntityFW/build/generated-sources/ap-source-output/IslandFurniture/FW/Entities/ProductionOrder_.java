package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.FurnitureModel;
import IslandFurniture.FW.Entities.StockUnit;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(ProductionOrder.class)
public class ProductionOrder_ { 

    public static volatile SingularAttribute<ProductionOrder, Long> batchNo;
    public static volatile ListAttribute<ProductionOrder, StockUnit> stockUnits;
    public static volatile SingularAttribute<ProductionOrder, FurnitureModel> furnitureModel;

}