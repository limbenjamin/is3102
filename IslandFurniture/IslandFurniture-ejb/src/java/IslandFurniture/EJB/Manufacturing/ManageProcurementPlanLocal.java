/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.MonthlyProcurementPlan;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Entities.RetailItem;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageProcurementPlanLocal {
    void createMonthlyProcumentPlan(ManufacturingFacility mf);
    List<MonthlyProcurementPlan> viewMonthlyProcurementPlan();
    void createPurchaseOrder(ManufacturingFacility mf, Month month, Integer year);
    List<ProcuredStockPurchaseOrder> viewPurchaseOrder();
    List<ProcuredStockPurchaseOrderDetail> viewPurchaseOrderDetail();
    List<RetailItem> viewRetailItems();
    void lockMpp(ManufacturingFacility mf, Month month, Integer year);
    boolean checkMppLocked(ManufacturingFacility mf, Month month, Integer year);
    boolean checkMppExist(ManufacturingFacility mf, Month month, Integer year);
}
