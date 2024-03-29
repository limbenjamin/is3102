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
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageRolesBeanLocal {
    
    void createRole(String name) throws DuplicateEntryException;
    void removeRole(Long id);
    List<Role> displayRole();
    List<Privilege> getPrivileges(Long roleId);
    void addPrivilegeToRole(Long roleId, String privilegeName);
    void removePrivilegeFromRole(Long roleId, Long privilegeId);
    Role getRole(Long roleId);
    Role getRoleFromName(String roleName);
}
