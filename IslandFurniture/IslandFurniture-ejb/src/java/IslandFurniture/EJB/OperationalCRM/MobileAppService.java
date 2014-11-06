/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.NFC;
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

    public Customer getcustomerFromid(String id) {
        Query q = em.createQuery("select co from Customer co where co.id=:con");
        q.setParameter("con", Long.parseLong(id));

        return ((Customer) q.getResultList().get(0));

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
        Query q = em.createQuery("select p from Picture p where EXISTS (select fm from FurnitureModel fm where fm.thumbnail.id=p.id AND fm.id=:p)");
        q.setParameter("p", Long.parseLong(picID));
        if (q.getResultList().isEmpty()) {
            return null;
        }

        return (Picture) q.getResultList().get(0);

    }

    public List<ShoppingList> getShoppingList(String Cust_id, String StoreID) {
        Query q = em.createQuery("select sl from ShoppingList sl where :cust MEMBER OF sl.customers and sl.store=:st");
        Customer c = (Customer) em.find(Customer.class, Long.parseLong(Cust_id));
        Store st = (Store) em.find(Store.class, Long.parseLong(StoreID));
        q.setParameter("cust", c);
        q.setParameter("st", st);

        return ((List<ShoppingList>) q.getResultList());

    }

    public ShoppingList getShoppingList(String Cust_id, String StoreID, String searchtext) {
        Query q = em.createQuery("select sl from ShoppingList sl where sl.name LIKE :TXT and :cust MEMBER OF sl.customers and sl.store=:st");
        Customer c = (Customer) em.find(Customer.class, Long.parseLong(Cust_id));
        Store st = (Store) em.find(Store.class, Long.parseLong(StoreID));
        q.setParameter("cust", c);
        q.setParameter("st", st);
        q.setParameter("TXT", searchtext);

        if (q.getResultList().isEmpty()) {
            System.out.println("getShoppingList(): Creating Default Shopping list for cust: " + Cust_id);
            return (mslb.createShoppingList(c.getEmailAddress(), Long.parseLong(StoreID), searchtext));
        }

        return (ShoppingList) q.getResultList().get(0);
    }

    public FurnitureModel getFurnitureModelByNFCID(String NFC_ID) throws Exception {
        System.out.println("NFC TAP:" + NFC_ID);
        Query n = em.createQuery("select z from NFC z where z.nfcId=:n");
        n.setParameter("n", NFC_ID);

        if (n.getResultList().isEmpty()) {
            throw new Exception("INVALID NFC");
        }

        Query q = em.createQuery("select fm from FurnitureModel fm where :nfc MEMBER OF fm.nfcList");
        q.setParameter("nfc", (NFC) n.getResultList().get(0));

        return (FurnitureModel) q.getResultList().get(0);

    }

    public void DeleteShopList(Long ID) {
        try {
            ShoppingList sl = em.find(ShoppingList.class, ID);
            em.remove(sl);
        } catch (Exception ex) {
        }
    }

    public void ShiftShopList(Long ListID, Long StoreID) {

        ShoppingList sl = em.find(ShoppingList.class, ListID);
        Store destination = em.find(Store.class, StoreID);
        sl.setStore(destination);
        System.out.println(sl + " SHIFTED TO" + destination);
        em.merge(sl);

    }

}
