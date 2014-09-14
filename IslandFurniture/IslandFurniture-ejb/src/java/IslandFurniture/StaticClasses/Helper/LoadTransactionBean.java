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
            Random rand = new Random(1); // Seed to ensure always same sample transactions

            List<FurnitureTransactionDetail> fTransDetails = new ArrayList();
            List<RetailItemTransactionDetail> riTransDetails = new ArrayList();

            List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();
            List<FurnitureModel> furnitureModels = (List<FurnitureModel>) em.createNamedQuery("getAllFurnitureModels").getResultList();
            List<RetailItem> retailItems = (List<RetailItem>) em.createNamedQuery("getAllRetailItems").getResultList();

            for (int i = 0; i < 800; i++) {
                for (Store eachStore : stores) {
                    // Add Furniture Transaction
                    fTransDetails.clear();

                    for (FurnitureModel fm : furnitureModels) {
                        if (rand.nextBoolean()) {
                            fTransDetails.add(this.addFurnitureTransactionDetail(fm, rand.nextInt(50) + 1));
                        }
                    }

                    if (!fTransDetails.isEmpty()) {
                        cal = Calendar.getInstance(TimeZone.getTimeZone(eachStore.getCountry().getTimeZoneID()));

                        // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                        cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));

                        this.addFurnitureTransaction(eachStore, fTransDetails, cal);
                    }

                    // Add Retail Item Transaction
                    riTransDetails.clear();

                    for (RetailItem ri : retailItems) {
                        if (rand.nextBoolean()) {
                            riTransDetails.add(this.addRetailItemTransactionDetail(ri, rand.nextInt(50) + 1));
                        }
                    }

                    if (!riTransDetails.isEmpty()) {
                        cal = Calendar.getInstance(TimeZone.getTimeZone(eachStore.getCountry().getTimeZoneID()));

                        // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                        cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));

                        this.addRetailItemTransaction(eachStore, riTransDetails, cal);
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false;
        }

    }
}
