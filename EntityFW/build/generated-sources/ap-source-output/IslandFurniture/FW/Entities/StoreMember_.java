package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Cart;
import IslandFurniture.FW.Entities.Country;
import IslandFurniture.FW.Entities.Feedback;
import IslandFurniture.FW.Entities.MembershipTier;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(StoreMember.class)
public class StoreMember_ { 

    public static volatile SingularAttribute<StoreMember, Country> country;
    public static volatile ListAttribute<StoreMember, Cart> carts;
    public static volatile SingularAttribute<StoreMember, MembershipTier> membershipTier;
    public static volatile ListAttribute<StoreMember, Feedback> feedbacks;
    public static volatile SingularAttribute<StoreMember, Long> id;

}