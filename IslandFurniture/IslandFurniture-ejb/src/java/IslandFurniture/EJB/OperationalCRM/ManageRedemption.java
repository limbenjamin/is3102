/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.RedeemableItem;
import IslandFurniture.Entities.Redemption;
import IslandFurniture.Entities.Staff;
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
public class ManageRedemption implements ManageRedemptionLocal {

    @PersistenceContext
    EntityManager em;

    Redemption redemption;

    // Function: Create Redemption
    @Override
    public void createRedemption(Staff staff, Calendar time, Long customerId, Long redeemableItemId) {
        Customer customer = (Customer) em.find(Customer.class, customerId);
        RedeemableItem redeemableItem = (RedeemableItem) em.find(RedeemableItem.class, redeemableItemId);
        Redemption redemption = new Redemption();
        redemption.setCustomer(customer);
        redemption.setRedeemableItem(redeemableItem);
        redemption.setClaimed(false);
        redemption.setCreatedBy(staff);
        redemption.setCreationTime(time);
        em.persist(redemption);
        em.flush();
    }

    //  Function: View list of Redemption
    @Override
    public List<Redemption> viewRedemption() {
        Query q = em.createQuery("SELECT s FROM Redemption s");
        return q.getResultList();
    }
}
