/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import IslandFurniture.Entities.RedeemableItem;
import IslandFurniture.Entities.Redemption;
import IslandFurniture.Entities.Voucher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
public class ManagePOS implements ManagePOSLocal {

    @PersistenceContext
    EntityManager em;

    @Override
    public int getVoucher(String id){
        Query query = em.createQuery("SELECT r FROM Redemption r WHERE r.id=:id AND r.claimed=FALSE");
        query.setParameter("id", Long.valueOf(id));
        Voucher v ;
        try{
            Redemption redemption = (Redemption) query.getSingleResult();
            v = (Voucher) redemption.getRedeemableItem();
        }catch(Exception e){
            return -1;
        }
        return v.getCashValue();
    }
    
    @Override
    public int getReceipt(String id){
        Query query = em.createQuery("SELECT f FROM FurnitureTransaction f WHERE f.id=:id");
        query.setParameter("id", Long.valueOf(id));
        int amount = 0;
        try{
            FurnitureTransaction ft = (FurnitureTransaction) query.getSingleResult();
            List<FurnitureTransactionDetail> ftdList = ft.getFurnitureTransactionDetails();
            Iterator<FurnitureTransactionDetail> iterator = ftdList.iterator();
            while(iterator.hasNext()){
                FurnitureTransactionDetail ftd = iterator.next();
                amount += (ftd.getNumReturned() - ftd.getNumClaimed())*ftd.getUnitPrice(); 
            }
        }catch(Exception e){
            System.err.print(e);
            return -1;
        }
        return amount;
    }
    
}
