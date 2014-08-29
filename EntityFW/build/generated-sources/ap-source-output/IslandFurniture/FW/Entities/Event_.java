package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Staff;
import java.sql.Time;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Event.class)
public class Event_ { 

    public static volatile SingularAttribute<Event, Staff> creator;
    public static volatile SingularAttribute<Event, String> name;
    public static volatile SingularAttribute<Event, Time> eventTime;
    public static volatile SingularAttribute<Event, String> description;
    public static volatile SingularAttribute<Event, Long> id;

}