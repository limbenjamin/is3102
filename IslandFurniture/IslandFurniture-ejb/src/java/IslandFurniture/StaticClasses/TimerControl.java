/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Enums.Month;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class TimerControl {

    @EJB
    private InitialiseServerBeanLocal initialiseServerBean;
    @EJB
    private SalesForecastBeanLocal salesForecastBean;
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private Calendar cal;
    private Calendar prevMth;

    @PostConstruct
    public void init(){
        cal = TimeMethods.getCalFromMonthYear(Month.FEB, 2014);
        prevMth = Calendar.getInstance();
        prevMth.add(Calendar.MONTH, -1);
    }
    
    @Schedule(hour = "*", minute = "*", second = "*/5")
    public void timedMssrUpdate() {
        if (initialiseServerBean.isUpdateMssrStarted() && cal.compareTo(prevMth) < 0) {
            System.out.println("Update MSSR!");
            
            List<CountryOffice> coList = (List<CountryOffice>) em.createNamedQuery("getAllCountryOffices").getResultList();
            for (CountryOffice co : coList) {
                salesForecastBean.updateMonthlyStockSupplyReq(co, Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR), Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));
            }
            this.cal.add(Calendar.MONTH, 1);
        }
    }
}
