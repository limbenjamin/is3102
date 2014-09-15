/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.FurnitureTransaction;
import IslandFurniture.EJB.Entities.FurnitureTransactionDetail;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReqPK;
import IslandFurniture.EJB.Entities.RetailItemTransaction;
import IslandFurniture.EJB.Entities.RetailItemTransactionDetail;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.Entities.Transaction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class SalesForecastBean implements SalesForecastBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private List<Transaction> getStoreTransactions(Store store, Month month, int year) {
        Calendar start = Calendar.getInstance(TimeZone.getTimeZone(store.getCountry().getTimeZoneID()));
        Calendar end = Calendar.getInstance(TimeZone.getTimeZone(store.getCountry().getTimeZoneID()));

        start.set(year, month.value, 1, 0, 0, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.set(year, month.value, 1, 0, 0, 0);
        end.add(Calendar.MONTH, 1);
        end.set(Calendar.MILLISECOND, 0);
        end.add(Calendar.MILLISECOND, -1);

        Query q = em.createNamedQuery("getStoreTransactions");
        q.setParameter("store", store);
        q.setParameter("startDate", start, TemporalType.TIMESTAMP);
        q.setParameter("endDate", end, TemporalType.TIMESTAMP);

        return (List<Transaction>) q.getResultList();
    }

    private MonthlyStockSupplyReq addMonthlyStockSupplyReq(Stock stock, Store store, Month month, int year) {
        MonthlyStockSupplyReqPK mssrPK = new MonthlyStockSupplyReqPK(stock.getId(), store.getId(), month, year);

        MonthlyStockSupplyReq mssr = em.find(MonthlyStockSupplyReq.class, mssrPK);

        if (mssr == null) {
            mssr = new MonthlyStockSupplyReq();
            mssr.setStock(stock);
            mssr.setStore(store);
            mssr.setMonth(month);
            mssr.setYear(year);

            em.persist(mssr);

            store.getMonthlyStockSupplyReqs().add(mssr);
            stock.getMonthlyStockSupplyReqs().add(mssr);
        }

        return mssr;
    }

    @Override
    public List<MonthlyStockSupplyReq> generateSalesFigures(Store store, Month startMonth, int startYear, Month endMonth, int endYear) {
        Calendar start = Calendar.getInstance();
        start.set(startYear, startMonth.value, 1);

        Calendar end = Calendar.getInstance();
        end.set(endYear, endMonth.value, 1);

        MonthlyStockSupplyReq mssr;
        FurnitureTransaction fTrans;
        RetailItemTransaction riTrans;

        while (start.compareTo(end) <= 0) {
            for (StockSupplied eachStockSupplied : store.getSuppliedWithFrom()) {
                mssr = this.addMonthlyStockSupplyReq(eachStockSupplied.getStock(), store, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));

                // Reset all qtySold to prevent duplicate counting
                mssr.setQtySold(0);
            }

            // Grab list of transactions in given store in a month
            List<Transaction> listOfTrans = this.getStoreTransactions(store, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));

            // Display all transactions grabbed in the month
            DateFormat dateYearFormat = new SimpleDateFormat("MMM yyyy");
            System.out.println("--------");
            System.out.println(dateYearFormat.format(start.getTime()));
            System.out.println("--------");

            // Loops through each transaction and increment qtySold in relevant MSSR
            for (Transaction eachTrans : listOfTrans) {
                System.out.println("Transaction Id: " + eachTrans.getId());
                System.out.println("Store: " + eachTrans.getStore().getName());
                DateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                timeFormat.setTimeZone(TimeZone.getTimeZone(eachTrans.getStore().getCountry().getTimeZoneID()));
                System.out.println("Transaction Date: " + timeFormat.format(eachTrans.getTransTime().getTime()) + ", " + eachTrans.getStore().getCountry().getTimeZoneID() + " time");
                
                if (eachTrans instanceof FurnitureTransaction) {
                    fTrans = (FurnitureTransaction) eachTrans;
                    em.refresh(fTrans);

                    for (FurnitureTransactionDetail eachDetail : fTrans.getFurnitureTransactionDetails()) {
                        mssr = this.addMonthlyStockSupplyReq(eachDetail.getFurnitureModel(), store, Month.getMonth(fTrans.getTransTime().get(Calendar.MONTH)), fTrans.getTransTime().get(Calendar.YEAR));

                        System.out.println(eachDetail.getFurnitureModel() + "|" + eachDetail.getQty());

                        mssr.setQtySold(mssr.getQtySold() + eachDetail.getQty());

                    }

                } else if (eachTrans instanceof RetailItemTransaction) {
                    riTrans = (RetailItemTransaction) eachTrans;
                    em.refresh(riTrans);

                    for (RetailItemTransactionDetail eachDetail : riTrans.getRetailItemTransactionDetails()) {
                        mssr = this.addMonthlyStockSupplyReq(eachDetail.getRetailItem(), store, Month.getMonth(riTrans.getTransTime().get(Calendar.MONTH)), riTrans.getTransTime().get(Calendar.YEAR));

                        System.out.println(eachDetail.getRetailItem() + "|" + eachDetail.getQty());

                        mssr.setQtySold(mssr.getQtySold() + eachDetail.getQty());
                    }
                }
            }

            // Move on to next month
            start.add(Calendar.MONTH, 1);
        }

        return null;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
