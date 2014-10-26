/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.Store;
import java.util.List;
import javax.ejb.EJB;
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

    @EJB
    private ManageShoppingListBeanLocal mslb;

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
        Query q = em.createQuery("select s from Store s where s.country.code=:cd");
        q.setParameter("cd", code);
        return ((List<Store>) q.getResultList());

    }

    public CountryOffice getCOFromName(String co) {
        Query q = em.createQuery("select co from CountryOffice co where co.name=:con");
        q.setParameter("con", co);

        return ((CountryOffice) q.getResultList().get(0));

    }

    public CountryOffice getCOFromID(String ID) {
        Query q = em.createQuery("select co from CountryOffice co where co.id=:con");
        q.setParameter("con", Long.parseLong(ID));

        return ((CountryOffice) q.getResultList().get(0));

    }

    public Store getStoreFromID(String ID) {
        Query q = em.createQuery("select so from Store so where so.id=:con");
        q.setParameter("con", Long.parseLong(ID));

        return ((Store) q.getResultList().get(0));

    }

    public FurnitureModel getfmFromID(String ID) {
        Query q = em.createQuery("select fm from FurnitureModel fm where fm.id=:con");
        q.setParameter("con", Long.parseLong(ID));

        return ((FurnitureModel) q.getResultList().get(0));

    }

    public Picture getPicture(String picID) {
        Query q = em.createQuery("select p from Picture p where EXISTS (select fm from FurnitureModel fm where fm.thumbnail.id=p.id)");
        if (q.getResultList().isEmpty()) {
            return null;
        }

        return (Picture) q.getResultList().get(0);

    }

    public ShoppingList getShoppingList(String Cust_id, String StoreID) {
        Query q = em.createQuery("select sl from ShoppingList sl where sl.name='MOBILE_APP' and :cust MEMBER OF sl.customers and sl.store=:st");
        Customer c = (Customer) em.find(Customer.class, Long.parseLong(Cust_id));
        Store st = (Store) em.find(Store.class, Long.parseLong(StoreID));
        q.setParameter("cust", c);
        q.setParameter("st", st);

        if (q.getResultList().isEmpty()) {
            return (mslb.createShoppingList(c.getEmailAddress(), Long.parseLong(StoreID), "MOBILE_APP"));
        }

        return (ShoppingList) q.getResultList().get(0);
    }

}
