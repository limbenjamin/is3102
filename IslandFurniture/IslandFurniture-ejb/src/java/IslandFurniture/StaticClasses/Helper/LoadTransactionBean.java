/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.FurnitureTransaction;
import IslandFurniture.EJB.Entities.FurnitureTransactionDetail;
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

        System.out.println(fTransDetails.get(0).getFurnitureTransaction());

        return fTrans;
    }

    private FurnitureTransactionDetail addFurnitureTransactionDetail(FurnitureModel furniture, int qty) {
        FurnitureTransactionDetail fTransDetail = new FurnitureTransactionDetail();
        fTransDetail.setFurnitureModel(furniture);
        fTransDetail.setQty(qty);

        return fTransDetail;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {

        FurnitureTransaction fTrans;
        List<FurnitureTransactionDetail> fTransDetails = new ArrayList();

        try {
            // Add Transactions for stores
            Calendar cal;
            Random rand = new Random();
            List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();
            List<FurnitureModel> furnitureModels = (List<FurnitureModel>) em.createNamedQuery("getAllFurnitureModels").getResultList();

            for (int i = 0; i < 800; i++) {
                for (Store eachStore : stores) {
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
                        
                        fTrans = this.addFurnitureTransaction(eachStore, fTransDetails, cal);
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
