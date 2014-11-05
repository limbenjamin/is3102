/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Role;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Benjamin
 */
@Remote
public interface ManageRolesBeanRemote {

    void addPrivilegeToRole(Long roleId, String privilegeName);

    void createRole(String name) throws DuplicateEntryException;

    List<Role> displayRole();

    List<Privilege> getPrivileges(Long roleId);

    Role getRole(Long roleId);

    Role getRoleFromName(String roleName);

    void removePrivilegeFromRole(Long roleId, Long privilegeId);

    void removeRole(Long id);
    
}
