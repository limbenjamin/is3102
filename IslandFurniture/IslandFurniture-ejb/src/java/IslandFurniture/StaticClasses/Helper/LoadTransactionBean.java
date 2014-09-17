/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.FurnitureTransaction;
import IslandFurniture.EJB.Entities.FurnitureTransactionDetail;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.RetailItemTransaction;
import IslandFurniture.EJB.Entities.RetailItemTransactionDetail;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Store;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
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
            eachDetail.setFurnitureTransaction(fTrans);
        }
        fTrans.setFurnitureTransactionDetails(fTransDetails);

        em.persist(fTrans);

        return fTrans;
    }

    private FurnitureTransactionDetail addFurnitureTransactionDetail(FurnitureModel furniture, int qty) {
        FurnitureTransactionDetail fTransDetail = new FurnitureTransactionDetail();
        fTransDetail.setFurnitureModel(furniture);
        fTransDetail.setQty(qty);

        return fTransDetail;
    }

    private RetailItemTransaction addRetailItemTransaction(Store store, List<RetailItemTransactionDetail> riTransDetails, Calendar transTime) {
        RetailItemTransaction riTrans = new RetailItemTransaction();
        riTrans.setStore(store);
        riTrans.setTransTime(transTime);
        for (RetailItemTransactionDetail eachDetail : riTransDetails) {
            eachDetail.setRetailItemTransaction(riTrans);
        }
        riTrans.setRetailItemTransactionDetails(riTransDetails);

        em.persist(riTrans);

        return riTrans;
    }

    private RetailItemTransactionDetail addRetailItemTransactionDetail(RetailItem retailItem, int qty) {
        RetailItemTransactionDetail riTransDetail = new RetailItemTransactionDetail();
        riTransDetail.setRetailItem(retailItem);
        riTransDetail.setQty(qty);

        return riTransDetail;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {

        try {
            // Add Transactions for stores
            Calendar cal;
            Calendar curr;
            TimeZone tz;
            Random rand = new Random(1); // Seed to ensure always same sample transactions

            List<FurnitureTransactionDetail> fTransDetails = new ArrayList();
            List<RetailItemTransactionDetail> riTransDetails = new ArrayList();

            List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();

            for (Store eachStore : stores) {
                tz = TimeZone.getTimeZone(eachStore.getTimeZoneID());

                for (int i = 0; i < 800; i++) {
                    // Add Furniture Transaction & Retail Item Transaction
                    fTransDetails.clear();
                    riTransDetails.clear();

                    // Get current time in store's timezone
                    curr = Calendar.getInstance();
                    curr.add(Calendar.MILLISECOND, tz.getRawOffset() * -1);

                    for (StockSupplied ss : eachStore.getSuppliedWithFrom()) {
                        if (rand.nextBoolean()) {
                            if (ss.getStock() instanceof FurnitureModel) {
                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) ss.getStock(), rand.nextInt(50) + 1));
                            } else if (ss.getStock() instanceof RetailItem) {
                                riTransDetails.add(this.addRetailItemTransactionDetail((RetailItem) ss.getStock(), rand.nextInt(50) + 1));
                            }
                        }
                    }

                    if (!fTransDetails.isEmpty()) {
                        cal = Calendar.getInstance();

                        // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                        do {
                            cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));
                            cal.add(Calendar.MILLISECOND, tz.getRawOffset() * -1);
                        } while (cal.after(curr));

                        this.addFurnitureTransaction(eachStore, fTransDetails, cal);
                    }

                    if (!riTransDetails.isEmpty()) {
                        cal = Calendar.getInstance();

                        // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                        do {
                            cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));
                            cal.add(Calendar.MILLISECOND, tz.getRawOffset() * -1);
                        } while (cal.after(curr));

                        this.addRetailItemTransaction(eachStore, riTransDetails, cal);
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
