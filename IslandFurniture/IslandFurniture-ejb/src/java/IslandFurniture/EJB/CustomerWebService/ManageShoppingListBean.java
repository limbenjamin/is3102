/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Exceptions.DuplicateEntryException;
import static IslandFurniture.StaticClasses.EncryptMethods.SHA1Hash;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Zee
 */
@Stateless
public class ManageShoppingListBean implements ManageShoppingListBeanLocal {
    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Customer customer;
    
    @EJB
    private ManageMarketingBeanLocal mmbl;    
    
    @Override
    public ShoppingList getShoppingList(Long id) {
        return (ShoppingList) em.find(ShoppingList.class, id);
    }    
    
    @Override
    public ShoppingListDetail getShoppingListDetail(Long id) {
        return (ShoppingListDetail) em.find(ShoppingListDetail.class, id);
    }      
    
    @Override
    public Customer getCustomer(String emailAddress){
        Query query = em.createQuery("FROM Customer s where s.emailAddress=:emailAddress");
        query.setParameter("emailAddress", emailAddress);
        return (Customer) query.getSingleResult();
    }

    @Override
    public ShoppingList createShoppingList(String emailAddress, Long storeId, String name) {
        Customer customer = getCustomer(emailAddress);
        List<Customer> newList = new ArrayList();
        newList.add(customer);     
        List<ShoppingListDetail> newListDetails = new ArrayList();
        Store store = (Store) em.find(Store.class, storeId);
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setShoppingListDetails(newListDetails);
        shoppingList.setStore(store);
        shoppingList.setName(name);
        shoppingList.setTotalPrice(0.00);
        shoppingList.setCustomers(newList);
        em.persist(shoppingList);
        shoppingList.setIdHash(SHA1Hash(shoppingList.getId().toString()));
        return shoppingList;
    }    
    
    @Override
    public void updateShoppingList(Long listId, Store store, String name) {
        ShoppingList shoppingList = (ShoppingList) em.find(ShoppingList.class, listId);
        shoppingList.setStore(store);
        shoppingList.setName(name);
        em.persist(shoppingList);
    }
    
    @Override
    public void updateListTotalPrice (Long listId, Customer customer) {
        ShoppingList shoppingList = (ShoppingList) em.find(ShoppingList.class, listId);
        Double subtotal = 0.0;
        Iterator<ShoppingListDetail> iterator = shoppingList.getShoppingListDetails().iterator();
        while (iterator.hasNext()) {
            ShoppingListDetail current = iterator.next();
            Double discountedPrice = getDiscountedPrice(current.getFurnitureModel(), shoppingList.getStore(), customer);
            subtotal = subtotal + discountedPrice * current.getQty();
        }
        shoppingList.setTotalPrice(subtotal);
        em.persist(shoppingList);
    }
    
    @Override
    public void deleteShoppingList(Long listId, String emailAddress) {
        Customer customer = getCustomer(emailAddress);
        ShoppingList shoppingList = (ShoppingList) em.find(ShoppingList.class, listId);
        shoppingList.getCustomers().remove(customer);
    }    
    
    @Override
    public List<ShoppingListDetail> getShoppingListDetails(Long listId){
        ShoppingList shoppingList = (ShoppingList) em.find(ShoppingList.class, listId);
        em.refresh(shoppingList);
        return shoppingList.getShoppingListDetails();
    }    
    
    @Override
    public void createShoppingListDetail(Long listId, Long stockId, int quantity, Double discountedPrice) throws DuplicateEntryException {
        ShoppingList shoppingList = (ShoppingList) em.find(ShoppingList.class, listId);
        FurnitureModel furniture = (FurnitureModel) em.find(FurnitureModel.class, stockId);

        if (!shoppingList.hasFurniture(furniture)) {
            ShoppingListDetail listDetail = new ShoppingListDetail();
            listDetail.setShoppingList(shoppingList);
            listDetail.setFurnitureModel(furniture);
            listDetail.setQty(quantity);
            shoppingList.getShoppingListDetails().add(listDetail);
            em.persist(shoppingList);
            em.persist(listDetail);
            em.flush();
        } else {
            for (ShoppingListDetail detail : shoppingList.getShoppingListDetails()) {
                if (detail.getFurnitureModel().equals(furniture)) {
                    detail.setQty(detail.getQty()+quantity);
                    em.persist(detail);
                }
            }
            em.persist(shoppingList);
            em.flush();            
            throw new DuplicateEntryException(furniture.getName() + " already exists in this shopping list. We updated the quantity instead.");
        }
    }    
    
    @Override
    public void updateShoppingListDetail(ShoppingListDetail listDetail) {
        em.merge(listDetail);
    }    
    
    @Override
    public void deleteShoppingListDetail(Long detailId) {
        em.remove((ShoppingListDetail) em.find(ShoppingListDetail.class, detailId));
    }
    
    public Double getDiscountedPrice(Stock s, Store store, Customer customer) {
        return (Double)mmbl.getDiscountedPrice(s, store, customer).get("D_PRICE");
    }    
}
