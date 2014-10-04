/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.Entities.LogEntry;
import IslandFurniture.Entities.Plant;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageSystemAuditLogBeanLocal {

    void log(String entityName, long entityId, String userAction, String changeMessage, String username);
    List<LogEntry> getLog();
    List<LogEntry> GetLogForPlant(Plant plant);
}
