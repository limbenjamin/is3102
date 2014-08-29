package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Staff;
import java.sql.Time;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(LogEntry.class)
public class LogEntry_ { 

    public static volatile SingularAttribute<LogEntry, String> userAction;
    public static volatile SingularAttribute<LogEntry, String> entityName;
    public static volatile SingularAttribute<LogEntry, String> changeMessage;
    public static volatile SingularAttribute<LogEntry, Long> entityId;
    public static volatile SingularAttribute<LogEntry, Staff> staff;
    public static volatile SingularAttribute<LogEntry, Long> id;
    public static volatile SingularAttribute<LogEntry, Time> logTime;

}