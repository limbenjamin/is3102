/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.RedeemableItem;
import IslandFurniture.Entities.Voucher;
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
        Query query = em.createQuery("SELECT r FROM Redemption r WHERE r.redeemableItem.id=:id");
        query.setParameter("id", Long.valueOf(id));
        Voucher v ;
        try{
            RedeemableItem redeemableItem = (RedeemableItem) query.getSingleResult();
            v = (Voucher) redeemableItem;
        }catch(Exception e){
            return -1;
        }
        return v.getCashValue();
    }
    
}
