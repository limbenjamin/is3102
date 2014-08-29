package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.MonthlyProductionPlan;
import IslandFurniture.FW.Entities.ProductionOrder;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(WeeklyProductionPlan.class)
public class WeeklyProductionPlan_ { 

    public static volatile SingularAttribute<WeeklyProductionPlan, MonthlyProductionPlan> monthlyProductionPlan;
    public static volatile SingularAttribute<WeeklyProductionPlan, Long> id;
    public static volatile SingularAttribute<WeeklyProductionPlan, ProductionOrder> productionOrder;

}