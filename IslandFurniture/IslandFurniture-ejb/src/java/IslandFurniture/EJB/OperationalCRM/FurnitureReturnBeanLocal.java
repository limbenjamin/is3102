/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Exceptions.InvalidInputException;
import javax.ejb.Local;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Local
public interface FurnitureReturnBeanLocal {

    FurnitureTransaction findTransaction(long transId);

    boolean checkValid(FurnitureTransaction furnTrans);

    void saveReturns(FurnitureTransaction furnTrans, Staff staff) throws InvalidInputException;

    void saveClaims(FurnitureTransaction furnTrans, Staff staff) throws InvalidInputException;

    boolean checkCanIssue(FurnitureTransaction furnTrans);
    
}
