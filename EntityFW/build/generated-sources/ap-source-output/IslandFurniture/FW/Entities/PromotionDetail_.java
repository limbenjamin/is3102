package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.PromotionCampaign;
import IslandFurniture.FW.Entities.Stock;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(PromotionDetail.class)
public class PromotionDetail_ { 

    public static volatile SingularAttribute<PromotionDetail, PromotionCampaign> promotionCampaign;
    public static volatile SingularAttribute<PromotionDetail, Long> id;
    public static volatile SingularAttribute<PromotionDetail, Stock> stock;

}