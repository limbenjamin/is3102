package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Staff;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Announcement.class)
public class Announcement_ { 

    public static volatile SingularAttribute<Announcement, Staff> creator;
    public static volatile SingularAttribute<Announcement, Date> activeDate;
    public static volatile SingularAttribute<Announcement, Date> expireDate;
    public static volatile SingularAttribute<Announcement, Long> id;
    public static volatile SingularAttribute<Announcement, String> title;
    public static volatile SingularAttribute<Announcement, String> content;

}