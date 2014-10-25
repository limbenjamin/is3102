/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author James
 */
@Stateless
public class MobileAppService implements MobileAppServiceLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    public String getCustomerName(Long cust_id) {
        Customer c = (Customer) em.find(Customer.class, cust_id);
        if (c.getName() == null) {
            return "No Name";
        }
        return c.getName();
    }

    public String getCustomerMemberTier(Long cust_id) {
        Customer c = (Customer) em.find(Customer.class, cust_id);
        if (c.getMembershipTier() == null) {
            return "No Membership";
        }
        return c.getMembershipTier().getTitle();
    }

    public Integer getCustomerCurrentPoint(Long cust_id) {
        Customer c = (Customer) em.find(Customer.class, cust_id);
        if (c.getCurrentPoints() == null) {
            return 0;
        }
        return c.getCurrentPoints();
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
