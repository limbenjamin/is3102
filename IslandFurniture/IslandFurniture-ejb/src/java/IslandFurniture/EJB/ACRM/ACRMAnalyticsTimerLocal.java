/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author James
 */
package IslandFurniture.EJB.ACRM;

import IslandFurniture.EJB.Manufacturing.*;
import javax.ejb.Local;

/**
- *
- * @author James
- */
@Local
public interface ACRMAnalyticsTimerLocal {
    public void setAdvancePeriod(int week);
}