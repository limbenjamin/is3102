/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.ACRM;

import IslandFurniture.EJB.Manufacturing.ProductionPlanningSingletonLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.MarketBasketAnalysis;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.StaticClasses.Helper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import static javax.ejb.LockType.WRITE;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Startup
@Singleton
@Lock(WRITE)
public class ACRMAnalyticsTimer implements ProductionPlanningSingletonLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    public static currentdate cdate = new currentdate();
    private Integer stepLeft = 0;

    public currentdate getCdate() {
        return cdate;
    }

    public void setCdate(currentdate cdate) {
        this.cdate = cdate;
    }

    public void setAdvanceWeek(int week) {
        this.stepLeft = week;
        System.out.println("ManageProductionPlanTimerBean(): Stepping" + stepLeft);
    }

    public static class currentdate {

        private Integer month = 1;
        private Integer week = 1;
        private Integer year = 2014;
        private Integer weekAdded = 0;
        private Calendar calendar;

        public currentdate() {

            try {
                System.out.println("Production Planning Timer Started!");
            } catch (Exception ex) {
                Logger.getLogger(ACRMAnalyticsTimer.class.getName()).log(Level.SEVERE, null, ex);
            }
            calendar = Calendar.getInstance();
        }

        public void addWeek() throws Exception {

            weekAdded++;
            Calendar c = getCalendar();
            month = c.get(Calendar.MONTH);
            week = Helper.getWeekNoFromDate(c);
            year = c.get(Calendar.YEAR);
            System.out.println("Event() : Now is " + week + "/" + (month + 1) + "/" + year);

        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Integer getWeek() {
            return week;
        }

        public void setWeek(Integer week) {
            this.week = week;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public void setCalendar(Calendar c) {
            this.calendar = c;
        }

        public Calendar getCalendar() throws Exception {
            //code optimization
            Calendar c = calendar;
            c.setFirstDayOfWeek(Calendar.MONDAY);

            month = c.get(Calendar.MONTH);
            week = Helper.getWeekNoFromDate(c);
            year = c.get(Calendar.YEAR);

            Integer t_month = Helper.addoneWeek(month, year, week, weekAdded, Calendar.MONTH);
            Integer t_week = Helper.addoneWeek(month, year, week, weekAdded, Calendar.WEEK_OF_MONTH);
            Integer t_year = Helper.addoneWeek(month, year, week, weekAdded, Calendar.YEAR);

            this.month = t_month;
            this.week = t_week;
            this.year = t_year;

            return (Helper.getStartDateOfWeek(month, year, week));

        }

    }

    public ACRMAnalyticsTimer() {
        stepLeft = 0;
        System.out.println("ACRM Analytics Started");

    }

    //should be done once every 6 months
    @Schedule(second = "*/2", hour = "*", minute = "*", year = "1", info = "Production Planning")
    public void Apiori() throws Exception {

        if (stepLeft <= 0) {
            return;
        }

        stepLeft--;

        //oh 6 months has passed
        Calendar newcal = cdate.getCalendar();
        newcal.add(Calendar.MONTH, 6);
        cdate.setCalendar(newcal);

        //ACRM Starts here
        //Tabulate dataset
        Query co = em.createQuery("SELECT co from CountryOffice co");

        em.createQuery("delete from MarketBasketAnalysis mba");
        System.out.println("Apirori(): Deleting all Existing MBA");

        for (CountryOffice c : (List<CountryOffice>) co.getResultList()) {
            System.out.println("Market Basket analysis() + " + c.getName());

            Query j = em.createQuery("select ss from StockSupplied ss where ss.countryOffice=:co");
            j.setParameter("co", c);
            ArrayList<Stock> master_product_list = new ArrayList<>();

            for (StockSupplied ss : (List<StockSupplied>) j.getResultList()) {
                master_product_list.add(ss.getStock());
            }

            if (master_product_list.size() == 0) {
                continue;
            }

            Apriori analysis = new Apriori();
            analysis.setColumnsmap(master_product_list);

            Query q = em.createQuery("SELECT t from FurnitureTransaction t where t.transTime <= :current and t.store.countryOffice=:co ");
            q.setParameter("current", cdate.getCalendar());
            q.setParameter("co", c);

            if (q.getResultList().isEmpty()) {
                continue;
            }

            //building the data
            Vector<Boolean> tran = new Vector<Boolean>();
            for (FurnitureTransaction t : (List<FurnitureTransaction>) q.getResultList()) {
                Query anotherQuery = em.createQuery("select fm from FurnitureModel fm where exists(select ft from FurnitureTransactionDetail ft where ft.furnitureTransaction=:trans and ft.furnitureModel=:fm)");
                if (anotherQuery.getResultList().isEmpty()) {
                    continue;
                }
                for (Stock p : master_product_list) {

                    if (anotherQuery.getResultList().contains(p)) {
                        tran.add(true);
                    } else {
                        tran.add(false);
                    }

                }
            }

            analysis.data.add(tran);

            //For this countryoffice
            analysis.Start(30); //min support 30%
            int start = 2;

            while (start <= 5) {
                Vector<Vector<Object>> itemset = analysis.createlargeitemset(2);

                if (itemset == null) {
                    break;
                }

                for (Vector<Object> o : itemset) {

                    MarketBasketAnalysis mba = new MarketBasketAnalysis();
                    mba.setCountryOffice(c);
                    mba.setBasketCount(start);

                    String all = " ";
                    for (Object k : o) {
                        mba.getFurnituremodels().add((FurnitureModel) k);
                        all.concat(" ").concat(((FurnitureModel) k).getName());

                    }

                    System.out.println("Backet Size[" + start + "]:" + all.trim());

                    em.persist(mba);

                }
                start++;
            }

        }

        System.out.println("==Finished==");
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
