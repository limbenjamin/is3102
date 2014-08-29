package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Country;
import IslandFurniture.FW.Entities.Ingredient;
import IslandFurniture.FW.Entities.ProcuredStock;
import IslandFurniture.FW.Entities.ProcurementContractDetail;
import IslandFurniture.FW.Entities.RestaurantPurchaseOrder;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Supplier.class)
public class Supplier_ { 

    public static volatile SingularAttribute<Supplier, ProcurementContractDetail> procurementContractDetail;
    public static volatile SingularAttribute<Supplier, Country> country;
    public static volatile ListAttribute<Supplier, RestaurantPurchaseOrder> restaurantPurchaseOrders;
    public static volatile ListAttribute<Supplier, Ingredient> ingredients;
    public static volatile SingularAttribute<Supplier, Long> id;
    public static volatile ListAttribute<Supplier, List> purchaseOrders;
    public static volatile ListAttribute<Supplier, ProcuredStock> procuredStocks;

}