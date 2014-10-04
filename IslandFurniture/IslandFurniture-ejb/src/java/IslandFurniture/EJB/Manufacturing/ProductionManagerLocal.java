/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.ProductionOrder;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.Exceptions.InsufficientMaterialException;
import IslandFurniture.Exceptions.InvalidInputException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface ProductionManagerLocal {

    public ProductionOrder deletePO(Long batchNo);

    public ProductionOrder editPO(Long batchNo, Calendar date, Integer qty);

    public ProductionOrder commencePO(Long batchNo, Long binId) throws InvalidInputException, InsufficientMaterialException;

    public ProductionOrder editCompletedQty(Long batchNo, Integer completedQty);

    public ProductionOrder completePO(Long batchNo, Long binId) throws InvalidInputException;

    public List<StorageBin> viewProductionBins(ManufacturingFacility mf);
}
