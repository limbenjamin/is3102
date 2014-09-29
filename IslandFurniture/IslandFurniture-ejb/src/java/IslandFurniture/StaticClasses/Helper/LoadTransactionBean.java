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
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.Store;
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
            Calendar cal = Calendar.getInstance();
            Calendar curr;

            Random rand = new Random(1); // Seed to ensure always same sample transactions

            List<FurnitureTransactionDetail> fTransDetails = new ArrayList();
            List<RetailItemTransactionDetail> riTransDetails = new ArrayList();

            List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();

            for (Store eachStore : stores) {
                    // Get current time in store's timezone
                    curr = TimeMethods.getPlantCurrTime(eachStore);

                for (int i = 0; i < 800; i++) {
                    // Add Furniture Transaction & Retail Item Transaction
                    fTransDetails.clear();
                    riTransDetails.clear();

                    for (Stock stock : eachStore.getSells()) {
                        if (rand.nextBoolean()) {
                            if (stock instanceof FurnitureModel) {
                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) stock, 10));
//                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) stock, rand.nextInt(50) + 1));
                            } else if (stock instanceof RetailItem) {
                                riTransDetails.add(this.addRetailItemTransactionDetail((RetailItem) stock, 10));
//                                riTransDetails.add(this.addRetailItemTransactionDetail((RetailItem) stock, rand.nextInt(50) + 1));
                            }
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
