package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.FurnitureModel;
import IslandFurniture.FW.Entities.WeeklyProductionPlan;
import IslandFurniture.FW.Enums.Month;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(MonthlyProductionPlan.class)
public class MonthlyProductionPlan_ { 

    public static volatile SingularAttribute<MonthlyProductionPlan, Month> month;
    public static volatile SingularAttribute<MonthlyProductionPlan, Integer> year;
    public static volatile ListAttribute<MonthlyProductionPlan, WeeklyProductionPlan> weeklyProductionPlans;
    public static volatile SingularAttribute<MonthlyProductionPlan, FurnitureModel> furnitureModel;

}