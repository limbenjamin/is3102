/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import java.util.List;
import java.util.Vector;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageStaffAccountsBeanLocal {

    void createStaffAccount(String username, String password, String name, String emailAddress, String phoneNo, String country, String storeName);

    List<Vector> displayAllStaffAccounts();
    
}
