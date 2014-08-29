package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Country;
import IslandFurniture.FW.Entities.PlantStockInventory;
import IslandFurniture.FW.Entities.Transaction;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Plant.class)
public abstract class Plant_ { 

    public static volatile SingularAttribute<Plant, Country> country;
    public static volatile ListAttribute<Plant, PlantStockInventory> plantStockInventories;
    public static volatile SingularAttribute<Plant, Long> id;
    public static volatile ListAttribute<Plant, Transaction> transactions;

}