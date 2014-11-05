/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Url;
import IslandFurniture.Exceptions.NullException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Benjamin
 */
@Remote
public interface ManagePrivilegesBeanRemote {

    void createPrivilege(String name, String description, List<Url> urlList) throws NullException;

    Url createUrl(String link, String icon, String menuItemName, boolean visible, Integer weight);

    List<Privilege> displayPrivilege();

    Privilege getPrivilegeFromName(String privilegeName);

    void removePrivilege(Long id);
    
}
