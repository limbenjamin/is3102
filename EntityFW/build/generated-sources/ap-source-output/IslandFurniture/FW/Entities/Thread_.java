package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Message;
import IslandFurniture.FW.Entities.Staff;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Thread.class)
public class Thread_ { 

    public static volatile SingularAttribute<Thread, Staff> sender;
    public static volatile SingularAttribute<Thread, Staff> recipient;
    public static volatile ListAttribute<Thread, Message> messages;
    public static volatile SingularAttribute<Thread, Long> id;

}