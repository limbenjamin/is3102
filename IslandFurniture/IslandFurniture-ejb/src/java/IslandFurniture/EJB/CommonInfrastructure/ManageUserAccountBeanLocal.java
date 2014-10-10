/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Staff;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageUserAccountBeanLocal {

    Staff getStaff(String username);

    Staff getStaffFromId(Long staffId);
    
    Staff getStaffFromCardId(String CardId);

    void modifyNote(String username, String notes);

    void modifyPersonalParticulars(String username, String phoneNo, String emailAddress);
}
