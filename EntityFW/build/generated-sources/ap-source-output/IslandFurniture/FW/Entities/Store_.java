package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.GoodsIssuedDocument;
import IslandFurniture.FW.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.FW.Entities.MonthlyStockSupplyReq;
import IslandFurniture.FW.Entities.Staff;
import IslandFurniture.FW.Entities.Stock;
import IslandFurniture.FW.Entities.StockSupplied;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Store.class)
public class Store_ extends Plant_ {

    public static volatile ListAttribute<Store, MonthlyStockSupplyReq> monthlyStockSupplyReqs;
    public static volatile ListAttribute<Store, StockSupplied> suppliedWithFrom;
    public static volatile SingularAttribute<Store, String> code;
    public static volatile ListAttribute<Store, Stock> sells;
    public static volatile ListAttribute<Store, GoodsIssuedDocument> goodsIssuedDocument;
    public static volatile SingularAttribute<Store, String> name;
    public static volatile ListAttribute<Store, Staff> employees;
    public static volatile ListAttribute<Store, MonthlyMenuItemSalesForecast> monthlyMenuItemSalesForecasts;

}