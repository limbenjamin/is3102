package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Privilege;
import IslandFurniture.FW.Entities.Staff;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile ListAttribute<Role, Privilege> privileges;
    public static volatile SingularAttribute<Role, String> name;
    public static volatile SingularAttribute<Role, Long> id;
    public static volatile ListAttribute<Role, Staff> staffs;

}