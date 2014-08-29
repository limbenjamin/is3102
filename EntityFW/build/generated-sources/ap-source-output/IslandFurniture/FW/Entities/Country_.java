package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Currency;
import IslandFurniture.FW.Entities.Plant;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Country.class)
public class Country_ { 

    public static volatile SingularAttribute<Country, String> code;
    public static volatile ListAttribute<Country, Plant> plant;
    public static volatile SingularAttribute<Country, String> name;
    public static volatile SingularAttribute<Country, Long> id;
    public static volatile ListAttribute<Country, Currency> currencies;

}