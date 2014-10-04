/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author James
 */
@Local
public interface ProductionPlanningSingletonRemote {
    public void setAdvanceWeek(int week);
}
