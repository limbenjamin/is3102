/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Redemption;
import IslandFurniture.Entities.Staff;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KamilulAshraf
 */
@Local
public interface ManageRedemptionLocal {

    // Function: Create Redemption
    void createRedemption(Staff staff, Calendar time, Long customerId, Long redeemableItemId);

    //  Function: View list of Redemption
    List<Redemption> viewRedemption();
    
}
