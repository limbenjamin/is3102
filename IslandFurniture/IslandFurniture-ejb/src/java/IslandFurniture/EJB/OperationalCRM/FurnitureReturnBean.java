/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class FurnitureReturnBean implements FurnitureReturnBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public FurnitureTransaction findTransaction(long transId) {
        return em.find(FurnitureTransaction.class, transId);
    }

    @Override
    public Double calcTransTotal(FurnitureTransaction furnTrans) {
        Double total = 0.0;

        for (FurnitureTransactionDetail detail : furnTrans.getFurnitureTransactionDetails()) {
            total += detail.getSubtotal();
        }
        return Math.floor(total);
    }

}
