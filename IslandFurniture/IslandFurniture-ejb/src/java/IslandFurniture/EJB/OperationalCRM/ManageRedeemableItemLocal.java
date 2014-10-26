/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Voucher;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KamilulAshraf
 */
@Local
public interface ManageRedeemableItemLocal {

    // Function: Create Redeemable Item
    void createRedeemableItem(Plant plant, int cashValue, Calendar expiryDate);

    // Function: Delete Redeemable Item
    void deleteRedeemableItem(Voucher voucher);

    // Function: Edit Redeemable Item
    void editRedeemableItem(Voucher updatedVoucher);

    //  Function: View list of Redeemable Item
    List<Voucher> viewRedeemableItem(Plant plant);

    //  Function: To check if there is no Membership Tier with the Same Title
    boolean checkIfNoRedeemableItemWithSameCashValueAndExpiryDate(Plant plant, int cashValue, Calendar expiryDate);

    //  Function: To check if there is no Redemption with the RedeemableItem
    boolean checkIfNoRedemptionWithRedeemableItem(Voucher voucher);
}
