/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.ITManagementModule;

import javax.ejb.Remote;

/**
 *
 * @author Benjamin
 */
@Remote
public interface ManageStaffAccountRemote {

    void createStaffAccount(String username, String password, String name, String emailAddress);
    
}
