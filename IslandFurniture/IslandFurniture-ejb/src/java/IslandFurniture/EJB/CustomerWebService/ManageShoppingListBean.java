/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
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
    
    @Override
    public ShoppingList getShoppingList(Long id) {
        return (ShoppingList) em.find(ShoppingList.class, id);
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
        Store store = (Store) em.find(Store.class, storeId);
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setStore(store);
        shoppingList.setName(name);
        shoppingList.setTotalPrice(0.00);
        shoppingList.setCustomers(newList);
        em.persist(shoppingList);
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
    public void deleteShoppingList(Long listId) {
        em.remove((ShoppingList) em.find(ShoppingList.class, listId));
    }    
    
    @Override
    public List<ShoppingListDetail> getShoppingListDetails(Long listId){
        ShoppingList shoppingList = (ShoppingList) em.find(ShoppingList.class, listId);
        em.refresh(shoppingList);
        return shoppingList.getShoppingListDetails();
    }    
    
    @Override
    public void createShoppingListDetail(Long listId, Long stockId, int quantity) throws DuplicateEntryException {
        ShoppingList shoppingList = (ShoppingList) em.find(ShoppingList.class, listId);
        FurnitureModel furniture = (FurnitureModel) em.find(FurnitureModel.class, stockId);

        if (!shoppingList.hasFurniture(furniture)) {
            ShoppingListDetail listDetail = new ShoppingListDetail();
            listDetail.setShoppingList(shoppingList);
            listDetail.setFurnitureModel(furniture);
            listDetail.setQty(quantity);
            shoppingList.getShoppingListDetails().add(listDetail);

            em.persist(listDetail);
            em.flush();
        } else {
            throw new DuplicateEntryException("Entry for " + furniture.getName() + " already exists in this shopping list!");
        }
    }    
    
    @Override
    public void updateShoppingListDetail(ShoppingListDetail listDetail) {
        em.merge(listDetail);
    }    
    
    @Override
    public void deleteShoppingListDetail(Long listId) {
        em.remove((ShoppingListDetail) em.find(ShoppingListDetail.class, listId));
    }    
}
