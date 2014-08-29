package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.Store;
import IslandFurniture.FW.Entities.StoreMember;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ { 

    public static volatile SingularAttribute<Transaction, StoreMember> member;
    public static volatile SingularAttribute<Transaction, Long> id;
    public static volatile SingularAttribute<Transaction, Store> store;

}