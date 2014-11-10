/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Redemption;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Zee
 */
@Stateless
public class ManageCustomerRedemptions implements ManageCustomerRedemptionsLocal {

    @PersistenceContext
    EntityManager em;
    
    @Override
    public List<Redemption> getRedemptions(Customer customer) {
        Query query = em.createQuery("SELECT r FROM Redemption r WHERE r.customer=:customer");
        query.setParameter("customer", customer);
        return (List<Redemption>) query.getResultList();
    }    
}
