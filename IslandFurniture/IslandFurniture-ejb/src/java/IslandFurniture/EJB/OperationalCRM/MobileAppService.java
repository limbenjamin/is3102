/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Store;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    public List<CountryOffice> getAllCO() {
        return ((List<CountryOffice>) em.createQuery("select c from CountryOffice c where EXISTS(select s from Store s where s.countryOffice=c)").getResultList());

    }

    public List<Store> getAllStores(String code) {
        Query q=em.createQuery("select s from Store s where s.country.code=:cd");
        q.setParameter("cd", code);
        return ((List<Store>) q.getResultList());

    }

}
