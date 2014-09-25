/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.ProductionOrder;
import java.util.Calendar;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface ProductionManagerLocal {

    public ProductionOrder deletePO(Long batchNo);

    public ProductionOrder editPO(Long batchNo, Calendar date, Integer qty);

    public ProductionOrder commencePO(Long batchNo);

    public ProductionOrder editCompletedQty(Long batchNo, Integer completedQty);

    public ProductionOrder completePO(Long batchNo);
    
}
