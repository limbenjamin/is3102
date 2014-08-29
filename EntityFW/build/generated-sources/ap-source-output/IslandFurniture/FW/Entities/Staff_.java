package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Announcement;
import IslandFurniture.FW.Entities.Event;
import IslandFurniture.FW.Entities.Notification;
import IslandFurniture.FW.Entities.Plant;
import IslandFurniture.FW.Entities.Preference;
import IslandFurniture.FW.Entities.Role;
import IslandFurniture.FW.Entities.Thread;
import IslandFurniture.FW.Entities.Todo;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Staff.class)
public class Staff_ { 

    public static volatile SingularAttribute<Staff, String> salt;
    public static volatile ListAttribute<Staff, Todo> todoList;
    public static volatile ListAttribute<Staff, Role> roles;
    public static volatile SingularAttribute<Staff, Preference> preference;
    public static volatile ListAttribute<Staff, Thread> outbox;
    public static volatile SingularAttribute<Staff, String> password;
    public static volatile SingularAttribute<Staff, String> emailAddress;
    public static volatile SingularAttribute<Staff, Plant> plant;
    public static volatile SingularAttribute<Staff, String> name;
    public static volatile SingularAttribute<Staff, String> forgottenPasswordCode;
    public static volatile SingularAttribute<Staff, Long> id;
    public static volatile ListAttribute<Staff, Thread> inbox;
    public static volatile ListAttribute<Staff, Announcement> announcements;
    public static volatile ListAttribute<Staff, Notification> notifications;
    public static volatile ListAttribute<Staff, Event> events;
    public static volatile SingularAttribute<Staff, String> username;
    public static volatile SingularAttribute<Staff, Date> lastLogon;

}