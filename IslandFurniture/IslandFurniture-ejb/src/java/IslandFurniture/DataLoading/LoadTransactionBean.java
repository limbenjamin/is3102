/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.RetailItemTransaction;
import IslandFurniture.Entities.RetailItemTransactionDetail;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.Store;
import IslandFurniture.StaticClasses.TimeMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadTransactionBean implements LoadTransactionBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private FurnitureTransaction addFurnitureTransaction(Store store, List<FurnitureTransactionDetail> fTransDetails, Calendar transTime) {
        FurnitureTransaction fTrans = new FurnitureTransaction();
        fTrans.setStore(store);
        fTrans.setTransTime(transTime);
        for (FurnitureTransactionDetail eachDetail : fTransDetails) {
            eachDetail.setUnitPrice(store.getCountryOffice().findStockSupplied(eachDetail.getFurnitureModel()).getPrice());
            eachDetail.setFurnitureTransaction(fTrans);
        }
        fTrans.setFurnitureTransactionDetails(fTransDetails);

        em.persist(fTrans);

        return fTrans;
    }

    private FurnitureTransactionDetail addFurnitureTransactionDetail(FurnitureModel furniture, int qty) {
        FurnitureTransactionDetail fTransDetail = new FurnitureTransactionDetail();
        fTransDetail.setUnitPoints(furniture.getPointsWorth());
        fTransDetail.setFurnitureModel(furniture);
        fTransDetail.setQty(qty);

        return fTransDetail;
    }

    private RetailItemTransaction addRetailItemTransaction(Store store, List<RetailItemTransactionDetail> riTransDetails, Calendar transTime) {
        RetailItemTransaction riTrans = new RetailItemTransaction();
        riTrans.setStore(store);
        riTrans.setTransTime(transTime);
        for (RetailItemTransactionDetail eachDetail : riTransDetails) {
            eachDetail.setUnitPrice(store.getCountryOffice().findStockSupplied(eachDetail.getRetailItem()).getPrice());
            eachDetail.setRetailItemTransaction(riTrans);
        }
        riTrans.setRetailItemTransactionDetails(riTransDetails);

        em.persist(riTrans);

        return riTrans;
    }

    private RetailItemTransactionDetail addRetailItemTransactionDetail(RetailItem retailItem, int qty) {
        RetailItemTransactionDetail riTransDetail = new RetailItemTransactionDetail();
        riTransDetail.setUnitPoints(retailItem.getPointsWorth());
        riTransDetail.setRetailItem(retailItem);
        riTransDetail.setQty(qty);

        return riTransDetail;
    }

    private RestaurantTransaction addRestaurantTransaction(Store store, List<RestaurantTransactionDetail> restTransDetails, Calendar transTime) {
        RestaurantTransaction restTrans = new RestaurantTransaction();
        restTrans.setStore(store);
        restTrans.setTransTime(transTime);
        for (RestaurantTransactionDetail eachDetail : restTransDetails) {
            eachDetail.setRestaurantTransaction(restTrans);
        }
        restTrans.setRestaurantTransactionDetails(restTransDetails);

        em.persist(restTrans);

        return restTrans;
    }

    private RestaurantTransactionDetail addRestaurantTransactionDetail(MenuItem menuItem, int qty) {
        RestaurantTransactionDetail miTransDetail = new RestaurantTransactionDetail();
        miTransDetail.setUnitPrice(menuItem.getPrice());
        miTransDetail.setUnitPoints(menuItem.getPointsWorth());
        miTransDetail.setMenuItem(menuItem);
        miTransDetail.setQty(qty);

        return miTransDetail;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {

        try {
            // Add Transactions for stores
            Calendar cal = Calendar.getInstance();
            Calendar curr;

            Random rand = new Random(1); // Seed to ensure always same sample transactions

            List<FurnitureTransactionDetail> fTransDetails = new ArrayList();
            List<RetailItemTransactionDetail> riTransDetails = new ArrayList();
            List<RestaurantTransactionDetail> restTransDetails = new ArrayList();

            List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();

            for (Store eachStore : stores) {
                // Get current time in store's timezone
                curr = TimeMethods.getPlantCurrTime(eachStore);

                for (int i = 0; i < 800; i++) {
                    // Add Furniture Transaction & Retail Item Transaction
                    fTransDetails.clear();
                    riTransDetails.clear();
                    restTransDetails.clear();

                    for (StockSupplied ss : eachStore.getCountryOffice().getSuppliedWithFrom()) {
                        if (rand.nextBoolean()) {
                            if (ss.getStock() instanceof FurnitureModel) {
                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) ss.getStock(), 10));
//                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) stock, rand.nextInt(50) + 1));
                            } else if (ss.getStock() instanceof RetailItem) {
                                riTransDetails.add(this.addRetailItemTransactionDetail((RetailItem) ss.getStock(), 10));
//                                riTransDetails.add(this.addRetailItemTransactionDetail((RetailItem) stock, rand.nextInt(50) + 1));
                            }
                        }
                    }

                    for (MenuItem mi : eachStore.getCountryOffice().getMenuItems()) {
                        if (rand.nextBoolean()) {
                            restTransDetails.add(this.addRestaurantTransactionDetail(mi, rand.nextInt(10) + 1));
                        }
                    }

                    if (!fTransDetails.isEmpty()) {
                        // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                        do {
                            cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));
                        } while (cal.after(curr));

                        cal = TimeMethods.convertToUtcTime(eachStore, cal);
                        this.addFurnitureTransaction(eachStore, fTransDetails, cal);
                    }

                    if (!riTransDetails.isEmpty()) {
                        // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                        do {
                            cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));
                        } while (cal.after(curr));

                        cal = TimeMethods.convertToUtcTime(eachStore, cal);
                        this.addRetailItemTransaction(eachStore, riTransDetails, cal);
                    }

                    if (!restTransDetails.isEmpty()) {
                        // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                        do {
                            cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));
                        } while (cal.after(curr));

                        cal = TimeMethods.convertToUtcTime(eachStore, cal);
                        this.addRestaurantTransaction(eachStore, restTransDetails, cal);
                    }
                }

                em.flush();
            }

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false;
        }

    }
}
