package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.MembershipTier;
import IslandFurniture.FW.Entities.PromotionDetail;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(PromotionCampaign.class)
public class PromotionCampaign_ { 

    public static volatile ListAttribute<PromotionCampaign, MembershipTier> membershipTiers;
    public static volatile SingularAttribute<PromotionCampaign, Long> id;
    public static volatile ListAttribute<PromotionCampaign, PromotionDetail> promotionDetails;

}