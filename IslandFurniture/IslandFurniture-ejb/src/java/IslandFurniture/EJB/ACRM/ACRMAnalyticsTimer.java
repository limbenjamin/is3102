/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.ACRM;

import IslandFurniture.Entities.CompanyPeriodAnalysisReport;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.CountryPeriodAnalysisReport;
import IslandFurniture.Entities.CustomerPeriodAnalysisReport;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.MarketBasketAnalysis;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.ProductAnalysisReport;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StorePeriodAnalysisReport;
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 *
 * @author James
 */
@Startup
@Singleton
@Lock(WRITE)
public class ACRMAnalyticsTimer implements ACRMAnalyticsTimerLocal {
    
    public static final Integer supportProb = 30;
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    public static currentdate cdate = new currentdate();
    public static currentdate cdate2 = new currentdate();
    
    private Integer stepLeft = 0;
    private Integer stepWeekLeft = 0;
    
    public currentdate getCdate() {
        return cdate;
    }
    
    public void setCdate(currentdate cdate) {
        this.cdate = cdate;
    }
    
    public static currentdate getCdate2() {
        return cdate2;
    }
    
    public static void setCdate2(currentdate cdate2) {
        ACRMAnalyticsTimer.cdate2 = cdate2;
    }
    
    public void setAdvancePeriod(int week) {
        this.stepLeft = week;
        System.out.println("ACRM(): Stepping" + stepLeft);
    }
    
    public void setAdvancePeriodForACRMT(int week) {
        this.stepWeekLeft = week;
        System.out.println("ACRM Transaction(): Stepping" + stepLeft);
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
        stepWeekLeft = 0;
        System.out.println("ACRM Analytics Started");
        
    }
    
    @Schedule(second = "*/2", hour = "*", minute = "*", info = "ACRM Timer Drill Up")
    public void TransactionAggregation() throws Exception {
        
        if (stepWeekLeft <= 0) {
            return;
        }
        
        stepWeekLeft--;
        cdate2.addWeek();
        
        Query l = em.createQuery("SELECT MAX(copar.analysis_date) from CompanyPeriodAnalysisReport copar");
        
        Calendar max = (Calendar) l.getSingleResult();
        
        if (max == null) {
            Query temp = em.createQuery("SELECT min(t.transTime) from Transaction t");
            if (temp.getResultList().isEmpty()) {
                return;
            }
            
            Timestamp t = ((Timestamp) temp.getResultList().get(0));
            DateFormat format = new SimpleDateFormat();
            format.format(t);
            System.out.println("Starting from begining at " + t);
            max = format.getCalendar();
            
        }
        
        max.setFirstDayOfWeek(Calendar.MONDAY);
        max.setWeekDate(max.get(Calendar.YEAR), max.get(Calendar.WEEK_OF_YEAR), Calendar.MONDAY);
        
        while (max.before(cdate.getCalendar())) {
            System.out.println("Transaction for dates: " + max.getTime());
            CompanyPeriodAnalysisReport copar = new CompanyPeriodAnalysisReport();
            copar.setAnalysis_date(max);
            Double total_rev = 0.0; //company revenue
            Double co_rev = 0.0; //country office
            Double s_rev = 0.0;
            Calendar oneweekafter = (Calendar) max.clone();
            oneweekafter.add(Calendar.WEEK_OF_YEAR, 1);
            CountryPeriodAnalysisReport cpar = null;
            StorePeriodAnalysisReport spar = null;

            //Customer Level Analysis fmtd.furnitureTransaction.member
            Query q = em.createQuery("select fmtd.furnitureTransaction.store.countryOffice,fmtd.furnitureTransaction.member.membershipTier,count(DISTINCT(fmtd.furnitureTransaction.member)),sum(fmtd.unitPrice*fmtd.qty) from FurnitureTransactionDetail fmtd where fmtd.furnitureTransaction.transTime>=:start and fmtd.furnitureTransaction.transTime<:end GROUP BY fmtd.furnitureTransaction.store.countryOffice,fmtd.furnitureTransaction.member.membershipTier");
            q.setParameter("start", max);
            q.setParameter("end", oneweekafter);
            
            cpar = null;
            spar = null;
            HashMap<CountryOffice, List<CustomerPeriodAnalysisReport>> map = new HashMap();
            
            for (Object[] o_array : (List<Object[]>) q.getResultList()) {
                
                if (map.get((CountryOffice) o_array[0]) == null) {
                    map.put((CountryOffice) o_array[0], new ArrayList<CustomerPeriodAnalysisReport>());
                }
                
                CustomerPeriodAnalysisReport custpar = new CustomerPeriodAnalysisReport();
                custpar.setCustomerSegment((MembershipTier) o_array[1]);
                custpar.setNumofcustomers((Long) o_array[2]);
                custpar.setTotalRevenue((Double) o_array[3]);
                map.get((CountryOffice) o_array[0]).add(custpar);
                
                System.out.println("START: " + max.getTime());
                System.out.println("END: " + oneweekafter.getTime());
                System.out.println("Country: " + o_array[0]);
                System.out.println("Membership: " + o_array[1]);
                System.out.println("Total Customers:" + o_array[2]);
                System.out.println("Total Revenue:" + o_array[3]);
                System.out.println("============================");
                
            }

            //Product Level Analysis
            q = em.createQuery("select fmtd.furnitureTransaction.store.countryOffice,fmtd.furnitureTransaction.store,fmtd.furnitureModel,sum(fmtd.qty),sum(fmtd.unitPrice*fmtd.qty) from FurnitureTransactionDetail fmtd where fmtd.furnitureTransaction.transTime>=:start and fmtd.furnitureTransaction.transTime<:end GROUP BY fmtd.furnitureTransaction.store.countryOffice,fmtd.furnitureTransaction.store,fmtd.furnitureModel");
            q.setParameter("start", max);
            q.setParameter("end", oneweekafter);
            
            cpar = null;
            spar = null;
            
            for (Object[] o_array : (List<Object[]>) q.getResultList()) {
                if (cpar == null || !cpar.getCountryOffice().equals(o_array[0])) {
                    if (cpar != null) {
                        cpar.setTotalRevenue(co_rev);
                    }
                    
                    cpar = new CountryPeriodAnalysisReport();
                    cpar.setCountryOffice((CountryOffice) o_array[0]);
                    cpar.setCompanyPeriodAnalysisReport(copar);
                    copar.getCountryPeriodAnalysisReports().add(cpar);
                    cpar.setTotalRevenue(0.0);
                    
                    //make linkage to customer reports
                    for (CustomerPeriodAnalysisReport cpap : map.get((CountryOffice) o_array[0])){
                    cpap.setCountryPeriodAnalysisReport(cpar);
                    cpar.getCustomerPeriodAnalysisReport().add(cpap);
                    }
                    
                    co_rev = 0.0;
                }
                
                if (spar == null || !spar.getStore().equals(o_array[1])) {
                    if (spar != null) {
                        spar.setTotalRevenue(s_rev);
                    }
                    s_rev = 0.0;
                    spar = new StorePeriodAnalysisReport();
                    spar.setCountryPeriodAnalysisReport(cpar);
                    spar.setStore((Store) o_array[1]);
                    cpar.getStorePeriodAnalysisReports().add(spar);
                }
                
                ProductAnalysisReport par = new ProductAnalysisReport();
                par.setStorePeriodAnalysisReport(spar);
                spar.getProductAnalysisReports().add(par);
                par.setStock((FurnitureModel) o_array[2]);
                par.setTotalQty((Long) o_array[3]);
                par.setTotalRevenue((Double) o_array[4]);
                
                System.out.println("START: " + max.getTime());
                System.out.println("END: " + oneweekafter.getTime());
                System.out.println("Country: " + o_array[0]);
                System.out.println("STORE: " + o_array[1]);
                System.out.println("Furniture Model: " + o_array[2]);
                System.out.println("Total Qty: " + o_array[3]);
                System.out.println("Total Rev: " + o_array[4]);
                System.out.println("============================");
                
                total_rev += (Double) o_array[4];
                co_rev += (Double) o_array[4];
                s_rev += (Double) o_array[4];
            }
            cpar.setTotalRevenue(co_rev); //for the last row
            spar.setTotalRevenue(s_rev);
            
            max.add(Calendar.WEEK_OF_YEAR, 1);
            copar.setTotalRevenue(total_rev);
            em.persist(copar);
        }
        
    }

//should be done once every 6 months
    @Schedule(second = "*/10", hour = "*", minute = "*", info = "ACRM Timer")
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
        
        em.createQuery("delete from MarketBasketAnalysis mba").executeUpdate();
        System.out.println("Apirori(): Deleting all Existing MBA");
        
        for (CountryOffice c : (List<CountryOffice>) co.getResultList()) {
            System.out.println("Market Basket analysis() + " + c.getName());
            
            Query j = em.createQuery("select ss from StockSupplied ss where ss.countryOffice=:co");
            j.setParameter("co", c);
            ArrayList<Stock> master_product_list = new ArrayList<>();
            
            for (StockSupplied ss : (List<StockSupplied>) j.getResultList()) {
                if (ss.getStock() instanceof FurnitureModel) {
                    master_product_list.add(ss.getStock());
                }
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
            for (FurnitureTransaction t : (List<FurnitureTransaction>) q.getResultList()) {
                
                Vector<Boolean> tran = new Vector<Boolean>();
                
                for (Stock p : master_product_list) {
                    Query anotherQuery = em.createQuery("select fm from FurnitureModel fm where exists(select ft from FurnitureTransactionDetail ft where ft.furnitureTransaction=:trans and ft.furnitureModel=:fm)");
                    anotherQuery.setParameter("trans", t);
                    anotherQuery.setParameter("fm", p);
                    
                    if (anotherQuery.getResultList().isEmpty()) {
                        tran.add(false);
                        continue;
                    }
                    
                    if (anotherQuery.getResultList().contains(p)) {
                        tran.add(true);
                    } else {
                        tran.add(false);
                    }
                    
                }
                analysis.data.add(tran);
            }

            //For this countryoffice
            analysis.Start(supportProb); //min support 30%
            int start = 1;
            
            while (start <= 5) {
                
                Vector<Vector<Object>> itemset = analysis.findListSet(start);
                
                if (itemset == null) {
                    break;
                }
                
                for (Vector<Object> o : itemset) {
                    if (start < 2) {
                        break;
                    }
                    if (o.isEmpty()) {
                        continue;
                    }
                    
                    MarketBasketAnalysis mba = new MarketBasketAnalysis();
                    mba.setCountryOffice(c);
                    mba.setBasketCount(start);
                    
                    String all = " ";
                    for (Object k : o) {
                        mba.getFurnituremodels().add((FurnitureModel) k);
                        all = all.concat(" ").concat(((FurnitureModel) k).getName());
                        
                    }
                    
                    System.out.println("Basket Size[" + start + "]:" + all.trim());
                    
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
