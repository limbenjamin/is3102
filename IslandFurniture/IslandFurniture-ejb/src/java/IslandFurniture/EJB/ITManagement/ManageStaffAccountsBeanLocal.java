/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Staff;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageStaffAccountsBeanLocal {

    Long createStaffAccount(String username, String password, String name, String emailAddress, String phoneNo, String countryName, String plantName);
    List<Staff> displayAllStaffAccounts();
    void deleteStaffAccount(Long id);
    void removeRoleFromStaff(Long staffId, Long roleId);
    void addRoleToStaff(Long staffId, String roleName);
    void addRoleToStaffByUsername(String staffName, String roleName);
    boolean checkIfStaffHasPrivilege(String staffName, Privilege privilege);
    List<Privilege> getPrivilegeListforStaff(String staffName);
    List<Staff> displayStaffAccountsFromPlant(String username);
    void createStaffAccountinBulk(String username, String password, String name, String emailAddress, String phoneNo, String countryName, String plantName);
}
