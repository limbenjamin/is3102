package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.ProcurementContractDetail;
import IslandFurniture.FW.Entities.Stock;
import IslandFurniture.FW.Entities.StockSupplied;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(ManufacturingFacility.class)
public class ManufacturingFacility_ extends Plant_ {

    public static volatile SingularAttribute<ManufacturingFacility, ProcurementContractDetail> suppliedBy;
    public static volatile ListAttribute<ManufacturingFacility, Stock> produces;
    public static volatile ListAttribute<ManufacturingFacility, StockSupplied> supplyingWhatTo;

}