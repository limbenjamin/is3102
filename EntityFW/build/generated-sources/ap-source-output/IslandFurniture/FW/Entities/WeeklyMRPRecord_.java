package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Material;
import IslandFurniture.FW.Entities.PurchaseOrderDetail;
import IslandFurniture.FW.Entities.WeeklyProductionPlan;
import IslandFurniture.FW.Enums.Month;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(WeeklyMRPRecord.class)
public class WeeklyMRPRecord_ { 

    public static volatile SingularAttribute<WeeklyMRPRecord, Integer> week;
    public static volatile SingularAttribute<WeeklyMRPRecord, WeeklyProductionPlan> weeklyProductionPlan;
    public static volatile SingularAttribute<WeeklyMRPRecord, Material> material;
    public static volatile SingularAttribute<WeeklyMRPRecord, Month> month;
    public static volatile SingularAttribute<WeeklyMRPRecord, Integer> year;
    public static volatile SingularAttribute<WeeklyMRPRecord, PurchaseOrderDetail> purchaseOrderDetail;

}