/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.MonthlyProcurementPlan;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageProcurementPlanLocal {
    void createMonthlyProcumentPlan();
    List<MonthlyProcurementPlan> viewMonthlyProcurementPlan();
}
