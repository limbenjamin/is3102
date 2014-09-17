/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.EJB.Entities.Staff;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageStaffAccountsBeanLocal {

    void createStaffAccount(String username, String password, String name, String emailAddress, String phoneNo, String country, String storeName);
    List<Staff> displayAllStaffAccounts();
    void deleteStaffAccount(Long id);
    void removeRoleFromStaff(Long staffId, Long roleId);
    void addRoleToStaff(Long staffId, String roleName);
    void addRoleToStaffByUsername(String staffName, String roleName);
}