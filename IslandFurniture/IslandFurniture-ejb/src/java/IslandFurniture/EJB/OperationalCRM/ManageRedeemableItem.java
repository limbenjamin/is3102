/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.RedeemableItem;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.Voucher;
import java.util.Calendar;
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
public class ManageRedeemableItem implements ManageRedeemableItemLocal {

    @PersistenceContext
    EntityManager em;

    Voucher voucher;

    // Function: Create Redeemable Item
    @Override
    public void createRedeemableItem(Plant plant, int cashValue, Calendar expiryDate) {
        CountryOffice co = (CountryOffice) plant;
        Voucher voucher = new Voucher();
        voucher.setCountryOffice(co);
        voucher.setCashValue(cashValue);
        voucher.setExpiryDate(expiryDate);
        em.persist(voucher);
        em.flush();
    }

    // Function: Edit Redeemable Item
    @Override
    public void editRedeemableItem(Voucher updatedVoucher) {
        voucher = (Voucher) em.find(Voucher.class, updatedVoucher.getId());
        voucher.setCashValue(updatedVoucher.getCashValue());
        voucher.setExpiryDate(updatedVoucher.getExpiryDate());
        em.merge(voucher);
        em.flush();
    }

    // Function: Delete Redeemable Item
    @Override
    public void deleteRedeemableItem(Voucher voucher) {
        voucher = (Voucher) em.find(Voucher.class, voucher.getId());
        em.remove(voucher);
        em.flush();
    }

    //  Function: View list of Redeemable Item
    @Override
    public List<Voucher> viewRedeemableItem(Plant plant) {
        CountryOffice co = (CountryOffice) plant;
        Query q = em.createQuery("SELECT s FROM Voucher s WHERE s.countryOffice.id=:plantId");
        q.setParameter("plantId", co.getId());
        return q.getResultList();
    }
    
        //  Function: View list of Redeemable Item from Store
    @Override
    public List<Voucher> viewRedeemableItemFromStore(Plant plant, Calendar calendar) {
        Store store = (Store) plant;
        Query q = em.createQuery("SELECT s FROM Voucher s WHERE s.countryOffice.id=:plantId AND (s.expiryDate > :calendar OR s.expiryDate = :calendar)");
        q.setParameter("plantId", store.getCountryOffice().getId());
        q.setParameter("calendar", calendar);
        return q.getResultList();
    }

    //  Function: To check if there is no Membership Tier with the Same Title
    @Override
    public boolean checkIfNoRedeemableItemWithSameCashValueAndExpiryDate(Plant plant, int cashValue, Calendar expiryDate) {
        CountryOffice co = (CountryOffice) plant;
        Query q = em.createQuery("SELECT s FROM Voucher s WHERE s.countryOffice.id=:plantId AND s.cashValue=:cashValue AND s.expiryDate=:expiryDate");
        q.setParameter("plantId", co.getId());
        q.setParameter("cashValue", cashValue);
        q.setParameter("expiryDate", expiryDate);
        return q.getResultList().isEmpty();
    }
    
    //  Function: To check if there is no Redemption with the RedeemableItem
    @Override
    public boolean checkIfNoRedemptionWithRedeemableItem(Voucher voucher) {
        Query q = em.createQuery("SELECT s FROM Redemption s WHERE s.redeemableItem.id=:id");
        q.setParameter("id", voucher.getId());
        return q.getResultList().isEmpty();
    }

}
