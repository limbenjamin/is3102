/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.ProdOrderStatus;
import IslandFurniture.EJB.Entities.ProductionOrder;
import java.util.Calendar;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774
 */
@Stateful
public class ProductionManager implements ProductionManagerLocal {

    @PersistenceContext
    EntityManager em;
    
    @Override
    public ProductionOrder deletePO(Long batchNo) {
        ProductionOrder po;
        
        try {
            System.out.println("ProductionManager.deletePO()");
            po = em.find(ProductionOrder.class, batchNo);
            System.out.println("Perform deletion");
            em.remove(po);
            return po;
        } catch(Exception ex) {
            System.err.println("Error");
            return null;
        }
    }
    @Override
    public ProductionOrder editPO(Long batchNo, Calendar date, Integer qty) {
        ProductionOrder po;
        try {
            System.out.println("ProductionManager.editPO()"); 
            System.out.println("Date is " + date.getTime().toString() + ". Quantity is " + qty);
            po = em.find(ProductionOrder.class, batchNo);
            if(date != null)
                po.setProdOrderDate(date);
            if(qty != null)
                po.setQty(qty);
            em.persist(po);
            return po;
        } catch(Exception ex) {
            System.err.println("Error");
            return null;            
        }
    }
    @Override
    public ProductionOrder editCompletedQty(Long batchNo, Integer completedQty) {
        ProductionOrder po;
        try {
            System.out.println("ProductionManager.editCompletedQty()");
            System.out.println("Editing batchNo: " + batchNo + ". Changing completedQty to " + completedQty); 
            po = em.find(ProductionOrder.class, batchNo);
            if(completedQty != null) {
                if(completedQty > po.getQty()) {
                    System.out.println("Invalid. Completed Quantity cannot be greater than planned quantity");
                    return null;
                } else {
                    po.setCompletedQty(completedQty);
                    em.persist(po);
                }
            }
            return po;
        } catch(Exception ex) {
            System.err.println("Error");
            return null;            
        }
    }
    @Override
    public ProductionOrder commencePO(Long batchNo) {
        ProductionOrder po;
        try {
            System.out.println("ProductionManager.commencePO()");
            po = em.find(ProductionOrder.class, batchNo);
            po.setStatus(ProdOrderStatus.STARTED);
            em.persist(po);
            return po;
        } catch(Exception ex) {
            System.err.println("Error");
            return null;             
        }
    }
    @Override
    public ProductionOrder completePO(Long batchNo) {
        ProductionOrder po;
        try {
            System.out.println("ProductionManager.completePO()");
            po = em.find(ProductionOrder.class, batchNo);
            po.setStatus(ProdOrderStatus.COMPLETED);
            em.persist(po);
            return po;
        } catch(Exception ex) {
            System.err.println("Error");
            return null;             
        }
    }
}
