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
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.RetailItemTransaction;
import IslandFurniture.Entities.RetailItemTransactionDetail;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Transaction;
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
        query.setParameter("id", Long.parseLong(id));
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
    public void useVoucher(String voucherId){
        Query query = em.createQuery("SELECT r FROM Redemption r WHERE r.id=:id AND r.claimed=FALSE");
        query.setParameter("id", Long.parseLong(voucherId));
        try{
            Redemption redemption = (Redemption) query.getSingleResult();
            redemption.setClaimed(Boolean.TRUE);
        }catch(Exception e){
        }
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
    @Override
    public void linkReceipt(String id,Transaction t){
        Query query = em.createQuery("SELECT t FROM Transaction t WHERE t.id=:id");
        query.setParameter("id", Long.valueOf(id));
        int amount = 0;
        try{
            Transaction tr = (Transaction) query.getSingleResult();
            //TODO : link receipt
        }catch(Exception e){
            System.err.print(e);
        }
    }
    @Override
    public Stock getStock(Long id){
        Query query = em.createQuery("SELECT s FROM Stock s WHERE s.id=:id");
        query.setParameter("id", id);
        try{
            Stock stock = (Stock) query.getSingleResult();
            return stock;
        }catch(Exception e){
            System.err.print(e);
            return null;
        }
    }
    @Override
    public void persistFTD(FurnitureTransactionDetail ftd){
        em.persist(ftd);
    }
    @Override
    public void persistFT(FurnitureTransaction ft){
        em.persist(ft);
    }
    @Override
    public void persistRTD(RetailItemTransactionDetail rtd){
        em.persist(rtd);
    }
    @Override
    public void persistRT(RetailItemTransaction rt){
        em.persist(rt);
    }
    @Override
    public void persistRST(RestaurantTransaction rt){
        em.persist(rt);
    }
    @Override
    public void persistRSTD(RestaurantTransactionDetail rtd){
        em.persist(rtd);
    }
}
