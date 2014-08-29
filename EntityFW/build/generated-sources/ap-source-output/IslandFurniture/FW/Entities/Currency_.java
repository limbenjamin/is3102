package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Country;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Currency.class)
public class Currency_ { 

    public static volatile SingularAttribute<Currency, Double> exchangeRate;
    public static volatile SingularAttribute<Currency, String> name;
    public static volatile SingularAttribute<Currency, Long> id;
    public static volatile ListAttribute<Currency, Country> countries;
    public static volatile SingularAttribute<Currency, Date> effectiveDate;

}