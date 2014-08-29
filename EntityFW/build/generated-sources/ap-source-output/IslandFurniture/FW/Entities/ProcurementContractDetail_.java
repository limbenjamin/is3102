package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.ManufacturingFacility;
import IslandFurniture.FW.Entities.ProcuredStock;
import IslandFurniture.FW.Entities.ProcurementContract;
import IslandFurniture.FW.Entities.Supplier;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(ProcurementContractDetail.class)
public class ProcurementContractDetail_ { 

    public static volatile SingularAttribute<ProcurementContractDetail, Supplier> supplier;
    public static volatile SingularAttribute<ProcurementContractDetail, ProcuredStock> procuredStock;
    public static volatile SingularAttribute<ProcurementContractDetail, Long> id;
    public static volatile SingularAttribute<ProcurementContractDetail, ProcurementContract> procurementContract;
    public static volatile SingularAttribute<ProcurementContractDetail, ManufacturingFacility> supplierFor;

}