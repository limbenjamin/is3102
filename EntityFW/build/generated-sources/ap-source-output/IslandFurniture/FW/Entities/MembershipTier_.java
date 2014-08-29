package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.PromotionCampaign;
import IslandFurniture.FW.Entities.StoreMember;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(MembershipTier.class)
public class MembershipTier_ { 

    public static volatile ListAttribute<MembershipTier, StoreMember> members;
    public static volatile ListAttribute<MembershipTier, PromotionCampaign> promotionCampaigns;
    public static volatile SingularAttribute<MembershipTier, Long> id;

}