package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Staff;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Todo.class)
public class Todo_ { 

    public static volatile SingularAttribute<Todo, String> description;
    public static volatile SingularAttribute<Todo, Staff> staff;
    public static volatile SingularAttribute<Todo, Long> id;
    public static volatile SingularAttribute<Todo, Boolean> status;

}