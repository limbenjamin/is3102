package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Staff;
import IslandFurniture.FW.Entities.Thread;
import java.sql.Time;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Message.class)
public class Message_ { 

    public static volatile SingularAttribute<Message, Boolean> isRead;
    public static volatile SingularAttribute<Message, Time> msgTime;
    public static volatile SingularAttribute<Message, Staff> staff;
    public static volatile SingularAttribute<Message, Long> id;
    public static volatile SingularAttribute<Message, Thread> thread;
    public static volatile SingularAttribute<Message, String> content;

}