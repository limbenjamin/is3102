/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RetailItemTransaction;
import IslandFurniture.Entities.Transaction;
import java.util.ArrayList;
import java.util.Iterator;
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
public class ManageCustomerTransactions implements ManageCustomerTransactionsLocal {

    @PersistenceContext
    EntityManager em;
    
    @Override
    public List<FurnitureTransaction> getFurnitureTransactions(Customer customer) {
        Query query = em.createQuery("SELECT m FROM Transaction m WHERE m.member=:customer");
        query.setParameter("customer", customer);
        List<Transaction> allTransactions = (List<Transaction>) query.getResultList();
        List<FurnitureTransaction> furnitureTransactions = new ArrayList<>();
        
        Iterator<Transaction> iterator = allTransactions.iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            if (transaction instanceof FurnitureTransaction) {
                FurnitureTransaction furnitureTrans = (FurnitureTransaction)transaction;
                furnitureTransactions.add(furnitureTrans);
            }
        }
        return furnitureTransactions;
    }
    
    @Override
    public List<RetailItemTransaction> getRetailTransactions(Customer customer) {
        Query query = em.createQuery("SELECT m FROM Transaction m WHERE m.member=:customer");
        query.setParameter("customer", customer);
        List<Transaction> allTransactions = (List<Transaction>) query.getResultList();
        List<RetailItemTransaction> retailTransactions = new ArrayList<>();
        
        Iterator<Transaction> iterator = allTransactions.iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            if (transaction instanceof RetailItemTransaction) {
                RetailItemTransaction retailTrans = (RetailItemTransaction)transaction;
                retailTransactions.add(retailTrans);
            }
        }
        return retailTransactions;
    }
    
    @Override
    public List<RestaurantTransaction> getRestaurantTransactions(Customer customer) {
        Query query = em.createQuery("SELECT m FROM Transaction m WHERE m.member=:customer");
        query.setParameter("customer", customer);
        List<Transaction> allTransactions = (List<Transaction>) query.getResultList();
        List<RestaurantTransaction> restaurantTransactions = new ArrayList<>();
        
        Iterator<Transaction> iterator = allTransactions.iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            if (transaction instanceof RestaurantTransaction) {
                RestaurantTransaction restaurantTrans = (RestaurantTransaction)transaction;
                restaurantTransactions.add(restaurantTrans);
            }
        }
        return restaurantTransactions;
    }    
}
