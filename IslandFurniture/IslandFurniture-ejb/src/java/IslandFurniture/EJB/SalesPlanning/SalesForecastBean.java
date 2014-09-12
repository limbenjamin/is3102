/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.FurnitureTransaction;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Store;
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

    private List<FurnitureTransaction> getStoreFurnitureTransactions(Store store, Month month, int year) {
        Calendar start = Calendar.getInstance(TimeZone.getTimeZone(store.getCountry().getTimeZoneID()));
        Calendar end = Calendar.getInstance(TimeZone.getTimeZone(store.getCountry().getTimeZoneID()));
        start.set(year, month.value, 1);
        end.set(year, month.value, end.getActualMaximum(Calendar.DAY_OF_MONTH));

        Query q = em.createNamedQuery("getStoreFurnitureTransactions");
        q.setParameter("store", store);
        q.setParameter("startDate", start, TemporalType.DATE);
        q.setParameter("endDate", end, TemporalType.DATE);

        return (List<FurnitureTransaction>) q.getResultList();
    }

    @Override
    public List<MonthlyStockSupplyReq> generateSalesFigures(Store store, Month startMonth, int startYear, Month endMonth, int endYear) {
        Calendar start = Calendar.getInstance();
        start.set(startYear, startMonth.value, 1);

        Calendar end = Calendar.getInstance();
        end.set(endYear, endMonth.value, 1);

        while (start.compareTo(end) <= 0) {
            // Grab list of furniture transactions in given store in a month
            List<FurnitureTransaction> listOfTrans = this.getStoreFurnitureTransactions(store, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));

            // Display all transactions grabbed in the month
            DateFormat dateYearFormat = new SimpleDateFormat("MMM yyyy");
            System.out.println("--------");
            System.out.println(dateYearFormat.format(start.getTime()));
            System.out.println("--------");
            for (FurnitureTransaction eachTrans : listOfTrans) {
                System.out.println("Transaction Id: " + eachTrans.getId());
                System.out.println("Store: " + eachTrans.getStore().getName());

                DateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:SS");
                timeFormat.setTimeZone(TimeZone.getTimeZone(eachTrans.getStore().getCountry().getTimeZoneID()));
                System.out.println("Transaction Date: " + timeFormat.format(eachTrans.getTransTime().getTime()) + ", " + eachTrans.getStore().getCountry().getTimeZoneID() + " time");
            }

            // Move on to next month
            start.set(Calendar.MONTH, start.get(Calendar.MONTH) + 1);
        }

        return null;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
