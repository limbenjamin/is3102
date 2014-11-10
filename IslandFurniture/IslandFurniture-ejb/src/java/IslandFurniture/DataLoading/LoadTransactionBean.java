/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.EJB.ACRM.ACRMAnalyticsTimerLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.RetailItemTransaction;
import IslandFurniture.Entities.RetailItemTransactionDetail;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.Store;
import IslandFurniture.StaticClasses.QueryMethods;
import IslandFurniture.StaticClasses.TimeMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadTransactionBean implements LoadTransactionBeanRemote {

    @EJB
    private ACRMAnalyticsTimerLocal aCRMAnalyticsTimer;
    
    @EJB
    private ManageMembershipLocal mmsl;

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private FurnitureTransaction addFurnitureTransaction(Store store, List<FurnitureTransactionDetail> fTransDetails, Calendar transTime, Customer customer) {
        Double total = 0.0;
        FurnitureTransaction fTrans = new FurnitureTransaction();
        fTrans.setStore(store);
        fTrans.setTransTime(transTime);
        for (FurnitureTransactionDetail eachDetail : fTransDetails) {
            eachDetail.setUnitPrice(store.getCountryOffice().findStockSupplied(eachDetail.getFurnitureModel()).getPrice());
            eachDetail.setFurnitureTransaction(fTrans);
            total += eachDetail.getSubtotal();
        }
        fTrans.setFurnitureTransactionDetails(fTransDetails);
        fTrans.setGrandTotal(Math.round(total * 100) / 100.0);
        fTrans.setVoucherTotal(0.0);
        fTrans.setReturnedCreditsUsed(0.0);
        fTrans.setMember(customer);

        em.persist(fTrans);

        return fTrans;
    }

    private FurnitureTransactionDetail addFurnitureTransactionDetail(FurnitureModel furniture, int qty) {
        FurnitureTransactionDetail fTransDetail = new FurnitureTransactionDetail();
        fTransDetail.setUnitPoints(furniture.getPointsWorth());
        fTransDetail.setFurnitureModel(furniture);
        fTransDetail.setQty(qty);
        fTransDetail.setNumClaimed(0);
        fTransDetail.setNumReturned(0);
        return fTransDetail;
    }

    private RetailItemTransaction addRetailItemTransaction(Store store, List<RetailItemTransactionDetail> riTransDetails, Calendar transTime, Customer customer) {
        Double total = 0.0;
        RetailItemTransaction riTrans = new RetailItemTransaction();
        riTrans.setStore(store);
        riTrans.setTransTime(transTime);
        for (RetailItemTransactionDetail eachDetail : riTransDetails) {
            eachDetail.setUnitPrice(store.getCountryOffice().findStockSupplied(eachDetail.getRetailItem()).getPrice());
            eachDetail.setRetailItemTransaction(riTrans);
            total += eachDetail.getSubtotal();
        }
        riTrans.setRetailItemTransactionDetails(riTransDetails);
        riTrans.setGrandTotal(Math.round(total * 100) / 100.0);
        riTrans.setVoucherTotal(0.0);
        riTrans.setMember(customer);

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

    private RestaurantTransaction addRestaurantTransaction(Store store, List<RestaurantTransactionDetail> restTransDetails, Calendar transTime, Customer customer) {
        Double total = 0.0;
        RestaurantTransaction restTrans = new RestaurantTransaction();
        restTrans.setStore(store);
        restTrans.setTransTime(transTime);
        for (RestaurantTransactionDetail eachDetail : restTransDetails) {
            eachDetail.setRestaurantTransaction(restTrans);
            total += eachDetail.getSubtotal();
        }
        restTrans.setRestaurantTransactionDetails(restTransDetails);
        restTrans.setGrandTotal(Math.round(total * 100) / 100.0);
        restTrans.setVoucherTotal(0.0);
        restTrans.setMember(customer);

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
    public boolean loadSampleData() {

        try {
            // Add Transactions for stores
            Calendar cal = Calendar.getInstance();
            Calendar curr;

            Random rand = new Random(2); // Seed to ensure always same sample transactions

            List<FurnitureTransactionDetail> fTransDetails = new ArrayList();
            List<RetailItemTransactionDetail> riTransDetails = new ArrayList();
            List<RestaurantTransactionDetail> restTransDetails = new ArrayList();

            List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();
            List<Customer> customers = (List<Customer>) em.createNamedQuery("getAllCustomers").getResultList();
            Integer points;

            // Pre search pairing and triple entities
            FurnitureModel studyTable = QueryMethods.findFurnitureByName(em, "Study Table - Dinosaur Edition");
            FurnitureModel swivelChair = QueryMethods.findFurnitureByName(em, "Swivel Chair");
            FurnitureModel lamp = QueryMethods.findFurnitureByName(em, "Bedside Lamp H31");
            FurnitureModel bedFrame = QueryMethods.findFurnitureByName(em, "Gothic Bed Frame (Queen Size)");
            FurnitureModel nightStand = QueryMethods.findFurnitureByName(em, "Ninja Night Stand");

            int[] track = {0, 0, 0};

            for (Store eachStore : stores) {
                // Get current time in store's timezone
                curr = TimeMethods.getPlantCurrTime(eachStore);

                for (int i = 0; i < 800; i++) {
                    // Add Furniture Transactions & Retail Item Transactions & Restaurant Transactions
                    fTransDetails.clear();
                    riTransDetails.clear();
                    restTransDetails.clear();

                    // Type of staging
                    int type = rand.nextInt(3);
                    track[type]++;

                    if (!eachStore.getCountryOffice().getSuppliedWithFrom().isEmpty()) {
                        for (StockSupplied ss : eachStore.getCountryOffice().getSuppliedWithFrom()) {
                            // Bias product pairing logic
                            if (ss.getStock().equals(studyTable) && type == 0) {
                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) ss.getStock(), rand.nextInt(10) + 1));
                            } else if (ss.getStock().equals(swivelChair) && type == 0) {
                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) ss.getStock(), rand.nextInt(10) + 1));
                            } else if (ss.getStock().equals(lamp) && type == 1) {
                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) ss.getStock(), rand.nextInt(10) + 1));
                            } else if (ss.getStock().equals(bedFrame) && type == 1) {
                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) ss.getStock(), rand.nextInt(10) + 1));
                            } else if (ss.getStock().equals(nightStand) && type == 1) {
                                fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) ss.getStock(), rand.nextInt(10) + 1));
                            } else if (rand.nextDouble() < 0.35) {
                                // Equal chance item logic

                                if (ss.getStock() instanceof FurnitureModel) {
                                    fTransDetails.add(this.addFurnitureTransactionDetail((FurnitureModel) ss.getStock(), rand.nextInt(10) + 1));
                                } else if (ss.getStock() instanceof RetailItem) {
                                    riTransDetails.add(this.addRetailItemTransactionDetail((RetailItem) ss.getStock(), rand.nextInt(10) + 1));
                                }
                            }
                        }

                        if (!fTransDetails.isEmpty()) {
                            // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                            do {
                                cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));
                            } while (cal.after(curr));

                            // Seasonal Staging
                            if (rand.nextDouble() < 0.3) {
                                // Christmas Peak
                                cal.set(Calendar.MONTH, 11);
                            } else if (eachStore.getCountry().equals(QueryMethods.findCountryByName(em, "Singapore")) && cal.get(Calendar.MONTH) == 4) {
                                // Great Singapore Sales lower sales in May, higher in June
                                if (rand.nextBoolean()) {
                                    cal.set(Calendar.MONTH, 5);
                                }
                            }

                            cal = TimeMethods.convertToUtcTime(eachStore, cal);
                            FurnitureTransaction addedFT = this.addFurnitureTransaction(eachStore, fTransDetails, cal, (rand.nextBoolean()) ? customers.get(rand.nextInt(customers.size())) : null);
                            if (addedFT.getMember() != null) {
                                points = 0;
                                for (FurnitureTransactionDetail detail : addedFT.getFurnitureTransactionDetails()) {
                                    points += detail.getTotalPoints().intValue();
                                }
                                addedFT.getMember().setCumulativePoints(addedFT.getMember().getCumulativePoints() + points);
                                addedFT.getMember().setCurrentPoints(addedFT.getMember().getCurrentPoints() + points);
                            }
                        }

                        if (!riTransDetails.isEmpty()) {
                            // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                            do {
                                cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));
                            } while (cal.after(curr));

                            // Seasonal Staging
                            if (rand.nextDouble() < 0.3) {
                                // Christmas Peak
                                cal.set(Calendar.MONTH, 11);
                            } else if (eachStore.getCountry().equals(QueryMethods.findCountryByName(em, "Singapore")) && cal.get(Calendar.MONTH) == 4) {
                                // Great Singapore Sales lower sales in May, higher in June
                                if (rand.nextBoolean()) {
                                    cal.set(Calendar.MONTH, 5);
                                }
                            }

                            cal = TimeMethods.convertToUtcTime(eachStore, cal);
                            RetailItemTransaction addedRT = this.addRetailItemTransaction(eachStore, riTransDetails, cal, (rand.nextBoolean()) ? customers.get(rand.nextInt(customers.size())) : null);
                            if (addedRT.getMember() != null) {
                                points = 0;
                                for (RetailItemTransactionDetail detail : addedRT.getRetailItemTransactionDetails()) {
                                    points += detail.getTotalPoints().intValue();
                                }
                                addedRT.getMember().setCumulativePoints(addedRT.getMember().getCumulativePoints() + points);
                                addedRT.getMember().setCurrentPoints(addedRT.getMember().getCurrentPoints() + points);
                            }
                        }
                    }
                    
                    for (MenuItem mi : eachStore.getCountryOffice().getMenuItems()) {
                        if (rand.nextBoolean()) {
                            restTransDetails.add(this.addRestaurantTransactionDetail(mi, rand.nextInt(10) + 1));
                        }
                    }

                    if (!restTransDetails.isEmpty()) {
                        // Note: for java.util.Calendar, value of month ranges from 0 to 11 inclusive
                        do {
                            cal.set(rand.nextInt(2) + 2013, rand.nextInt(12), rand.nextInt(28) + 1, rand.nextInt(13) + 10, rand.nextInt(60), rand.nextInt(60));
                        } while (cal.after(curr));

                        // Seasonal Staging
                        if (rand.nextDouble() < 0.3) {
                            // Christmas Peak
                            cal.set(Calendar.MONTH, 11);
                        } else if (eachStore.getCountry().equals(QueryMethods.findCountryByName(em, "Singapore")) && cal.get(Calendar.MONTH) == 4) {
                            // Great Singapore Sales lower sales in May, higher in June
                            if (rand.nextBoolean()) {
                                cal.set(Calendar.MONTH, 5);
                            }
                        }

                        cal = TimeMethods.convertToUtcTime(eachStore, cal);
                        RestaurantTransaction addedRestTrans = this.addRestaurantTransaction(eachStore, restTransDetails, cal, (rand.nextBoolean()) ? customers.get(rand.nextInt(customers.size())) : null);
                        if (addedRestTrans.getMember() != null) {
                            points = 0;
                            for (RestaurantTransactionDetail detail : addedRestTrans.getRestaurantTransactionDetails()) {
                                points += detail.getTotalPoints().intValue();
                            }
                            addedRestTrans.getMember().setCumulativePoints(addedRestTrans.getMember().getCumulativePoints() + points);
                            addedRestTrans.getMember().setCurrentPoints(addedRestTrans.getMember().getCurrentPoints() + points);
                        }
                    }
                }

                em.flush();
            }
            
            //Promote members since they have points already
            
            for (Customer c: customers)
            {
                mmsl.promoteCustomer(c);
            }
            
            

            // Advance Analytics Timer
            System.out.println(track[0] + " | " + track[1] + " | " + track[2]);
            aCRMAnalyticsTimer.setAdvancePeriod(1);
            aCRMAnalyticsTimer.setAdvancePeriodForACRMT(1);
            

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false;
        }

    }
}
