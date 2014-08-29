package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Role;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Privilege.class)
public class Privilege_ { 

    public static volatile ListAttribute<Privilege, Role> roles;
    public static volatile SingularAttribute<Privilege, String> name;
    public static volatile SingularAttribute<Privilege, Long> id;

}