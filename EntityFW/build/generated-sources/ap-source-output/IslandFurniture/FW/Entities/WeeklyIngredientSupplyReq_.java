package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.DailyIngredientSupplyReq;
import IslandFurniture.FW.Entities.Ingredient;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(WeeklyIngredientSupplyReq.class)
public class WeeklyIngredientSupplyReq_ { 

    public static volatile SingularAttribute<WeeklyIngredientSupplyReq, Long> period;
    public static volatile SingularAttribute<WeeklyIngredientSupplyReq, Ingredient> ingredient;
    public static volatile SingularAttribute<WeeklyIngredientSupplyReq, Long> qty;
    public static volatile SingularAttribute<WeeklyIngredientSupplyReq, Long> id;
    public static volatile ListAttribute<WeeklyIngredientSupplyReq, DailyIngredientSupplyReq> dailyReq;

}