/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import IslandFurniture.EJB.Kitchen.FoodForecastBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Enums.Month;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.Entities.Store;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Startup
@Singleton
public class TimerControl implements TimerControlLocal {

    @EJB
    FoodForecastBeanLocal foodForecastBean;

    @EJB
    private SalesForecastBeanLocal salesForecastBean;

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private boolean updateMssrStarted;
    private Calendar cal;
    private Calendar prevMth;

    private int ipodYr;
    private int ipodMth;
    private int ipodWk;
    private int ipodSteps = 0;
    private int numWeeks;

    @PostConstruct
    public void init() {
        this.updateMssrStarted = false;
        cal = TimeMethods.getCalFromMonthYear(Month.AUG, 2014);
        prevMth = Calendar.getInstance();
        prevMth.add(Calendar.MONTH, -1);

        ipodYr = 2014;
        ipodMth = 11;
        ipodWk = 4;
    }

    @Schedule(hour = "*", minute = "*", second = "*/5")
    public void timedMssrUpdate() {
        if (updateMssrStarted && cal.compareTo(prevMth) < 0) {
            System.out.println("Update MSSR!");

            List<CountryOffice> coList = (List<CountryOffice>) em.createNamedQuery("getAllCountryOffices").getResultList();
            for (CountryOffice co : coList) {
                salesForecastBean.updateMonthlyStockSupplyReq(co, Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR), Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));
            }
            this.cal.add(Calendar.MONTH, 1);
        }

        // Ingredient Purchase Order generation hitching on MSSR Update
        if (ipodSteps > 0) {
            ipodSteps--;
            numWeeks = Helper.getNumOfWeeks(ipodMth, ipodYr);

            ipodWk++;
            if (ipodWk > numWeeks) {
                ipodWk = 1;
                ipodMth++;
                if (ipodMth > 11) {
                    ipodMth = 0;
                    ipodYr++;
                }
            }

            List<Store> storeList = (List<Store>) em.createNamedQuery("getAllStores").getResultList();
            for (Store store : storeList) {
                foodForecastBean.makeIngredPurchaseOrders(store, Month.getMonth(ipodMth), ipodYr, ipodWk);
            }
        }
    }

    @Override
    public void startMssrTimer() {
        this.updateMssrStarted = true;
    }
    
    @Override
    public void setIpodSteps(int ipodSteps) {
        this.ipodSteps = ipodSteps;
    }

}
