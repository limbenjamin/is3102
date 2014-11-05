/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Staff;
import javax.ejb.Remote;

/**
 *
 * @author Benjamin
 */
@Remote
public interface ManageUserAccountBeanRemote {

    Staff getStaff(String username);

    Staff getStaffFromCardId(String cardId);

    Staff getStaffFromId(Long staffId);

    void modifyNote(String username, String notes);

    void modifyPersonalParticulars(String username, String phoneNo, String emailAddress);
    
}
