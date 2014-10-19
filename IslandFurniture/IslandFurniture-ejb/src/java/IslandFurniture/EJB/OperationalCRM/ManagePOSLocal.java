/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManagePOSLocal {

    int getVoucher(String id);

    int getReceipt(String receiptId);
    
}
