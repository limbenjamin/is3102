/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Enums.Month;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlan;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.PurchaseOrderDetail;
import IslandFurniture.EJB.Entities.RetailItem;
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
    List<PurchaseOrder> viewPurchaseOrder();
    List<PurchaseOrderDetail> viewPurchaseOrderDetail();
    List<RetailItem> viewRetailItems();
    void lockMpp(ManufacturingFacility mf, Month month, Integer year);
    boolean checkMppLocked(ManufacturingFacility mf, Month month, Integer year);
}
