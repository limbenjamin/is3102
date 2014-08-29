package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.ManufacturingFacility;
import IslandFurniture.FW.Entities.MonthlyStockSupplyReq;
import IslandFurniture.FW.Entities.PlantStockInventory;
import IslandFurniture.FW.Entities.PriceInCountry;
import IslandFurniture.FW.Entities.StockUnit;
import IslandFurniture.FW.Entities.Store;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Stock.class)
public abstract class Stock_ { 

    public static volatile ListAttribute<Stock, MonthlyStockSupplyReq> monthlyStockSupplyReqs;
    public static volatile ListAttribute<Stock, PlantStockInventory> planStockInventories;
    public static volatile ListAttribute<Stock, PriceInCountry> priceInCountry;
    public static volatile ListAttribute<Stock, Store> soldBy;
    public static volatile SingularAttribute<Stock, Long> id;
    public static volatile ListAttribute<Stock, ManufacturingFacility> producedBy;
    public static volatile ListAttribute<Stock, StockUnit> stockUnit;

}