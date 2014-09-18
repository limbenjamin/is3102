/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.EJB.Entities.Privilege;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManagePrivilegesBeanLocal {
    void createPrivilege(String name);
    void removePrivilege(Long id);
    List<Privilege> displayPrivilege();
    Privilege getPrivilegeFromName(String privilegeName);
}
