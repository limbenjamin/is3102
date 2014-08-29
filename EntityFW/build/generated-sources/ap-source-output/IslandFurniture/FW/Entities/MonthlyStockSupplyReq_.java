package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.FW.Entities.Stock;
import IslandFurniture.FW.Entities.Store;
import IslandFurniture.FW.Enums.Month;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(MonthlyStockSupplyReq.class)
public class MonthlyStockSupplyReq_ { 

    public static volatile SingularAttribute<MonthlyStockSupplyReq, Integer> beginInventory;
    public static volatile SingularAttribute<MonthlyStockSupplyReq, Month> month;
    public static volatile ListAttribute<MonthlyStockSupplyReq, GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails;
    public static volatile SingularAttribute<MonthlyStockSupplyReq, Integer> year;
    public static volatile SingularAttribute<MonthlyStockSupplyReq, Integer> qtyForecasted;
    public static volatile SingularAttribute<MonthlyStockSupplyReq, Integer> qtySold;
    public static volatile SingularAttribute<MonthlyStockSupplyReq, Integer> qtyRequested;
    public static volatile SingularAttribute<MonthlyStockSupplyReq, Store> store;
    public static volatile SingularAttribute<MonthlyStockSupplyReq, Stock> stock;
    public static volatile SingularAttribute<MonthlyStockSupplyReq, Boolean> commited;

}