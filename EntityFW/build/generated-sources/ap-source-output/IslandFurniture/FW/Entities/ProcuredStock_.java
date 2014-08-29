package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.ProcurementContractDetail;
import IslandFurniture.FW.Entities.Supplier;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(ProcuredStock.class)
public abstract class ProcuredStock_ extends Stock_ {

    public static volatile ListAttribute<ProcuredStock, Supplier> suppliers;
    public static volatile ListAttribute<ProcuredStock, ProcurementContractDetail> procurementContractDetails;

}