/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.WeeklyProductionPlan;
import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurniture.EJB.Exceptions.ProductionPlanExceedsException;
import IslandFurniture.EJB.Exceptions.ProductionPlanNoCN;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@LocalBean
public class ManageProductionPlanning {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    private ManufacturingFacility MF = null;

    private void persist(Object object) {
        em.persist(object);
    }

    public void setCN(String cn_name) {
        Country cn = null;
        try {
            cn = (Country) em.createQuery("select cn from Country cn where cn.name='" + cn_name + "'").getResultList().get(0);
            setCN(cn);
        } catch (Exception ex) {
            try {
                throw new Exception("No such Country");
            } catch (Exception ex1) {
            }
        }

    }

    //Public method. Country must set first.
    public void setCN(Country cn) {
        MF = (ManufacturingFacility) em.createQuery("select mf from ManufacturingFacility mf where mf.country.id=" + cn.getId()).getResultList().get(0);

    }

    //Public method. Plan all requirements
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void CreateProductionPlanFromForecast() throws Exception {
        Query q = em.createQuery("select mmsr from MonthlyStockSupplyReq mmsr");

        List<MonthlyStockSupplyReq> MSSRL = q.getResultList();

        CreateProductionPlanFromForecast(MSSRL);
    }

    // Public method , pass a list of forecast to see if it is feasible.
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void CreateProductionPlanFromForecast(List<MonthlyStockSupplyReq> MSSRL) throws ProductionPlanExceedsException, ProductionPlanNoCN, Exception {
        if (MF == null) {
            throw new ProductionPlanNoCN();
        }
        //Start from last date to earlist date
        Comparator<MonthlyStockSupplyReq> byMY
                = new Comparator<MonthlyStockSupplyReq>() {
                    @Override
                    public int compare(MonthlyStockSupplyReq e1, MonthlyStockSupplyReq e2) {
                        return (Integer.compare((int) (e2.getYear() & e2.getMonth().value), (int) (e1.getYear() & e1.getMonth().value)));
                    }
                };

        Stream<MonthlyStockSupplyReq> sortedMSSRL = MSSRL.stream().sorted(byMY);

        boolean firstrow = true;
        int latestMonth = 0;
        int latestYear = 0;
        for (Object o : sortedMSSRL.toArray()) {

            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            if (firstrow) {
                firstrow = false;
                latestMonth = mssr.getMonth().value;
                latestYear = mssr.getYear();

            }
            CreateProductionPlanFromForecast(mssr);
        }
        if (latestYear > 0) {
            balanceProductionTill(latestYear, latestMonth);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void CreateProductionPlanFromForecast(MonthlyStockSupplyReq MSSR) throws ProductionPlanExceedsException, Exception {

        int month = MSSR.getMonth().value;
        int Year = MSSR.getYear();
        FurnitureModel furnitureModel = (FurnitureModel) MSSR.getStock();

        MonthlyProductionPlan mpp = null;

        int c_year = Calendar.getInstance().get(Calendar.YEAR);
        int c_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        long monthGap = (Year - c_year) * 12 + (month - c_month);

        long monthGap_semiannual = Math.max(monthGap, 5);
        //Loop to ensure get all MPP before are created up to the month

        MonthlyProductionPlan prevMpp = null;
        for (int i = 0; i <= monthGap_semiannual; i++) {
            int i_month = ((c_month + i - 1) % 12) + 1;
            int i_year = Math.floorDiv((c_month + i - 1), 12) + c_year;

            Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.year=" + i_year + " and mpp.month=:i_m");
            q.setParameter("i_m", Helper.translateMonth(i_month));

            if (q.getResultList().size() == 0) {
                Month eMonth = Helper.translateMonth(i_month);
                mpp = new MonthlyProductionPlan();
                mpp.setMonth(eMonth);
                mpp.setYear((int) i_year);
                mpp.setFurnitureModel((FurnitureModel) furnitureModel);;
                mpp.setQTY(0); //Brand New
                if (i == 0) {
                    mpp.setLocked(true); //prevent planning for this month
                }

            } else {
                mpp = (MonthlyProductionPlan) q.getResultList().get(0);
            }

            if (prevMpp != null && !prevMpp.equals(mpp)) {
                mpp.setPrevMonthlyProductionPlan(prevMpp);
                prevMpp.setNextMonthlyProductionPlan(mpp);
            }

            //Tag monthlyProductionPlan to MonthlyStockSupply
            if (i == monthGap && !mpp.getMonthlyStockSupplyReqs().contains(MSSR)) {
                mpp.getMonthlyStockSupplyReqs().add(MSSR);
            }

            prevMpp = mpp;

            em.persist(mpp);
            em.flush();

            System.err.println("ManageProductionPlanning: Created production planning for " + i_year + "/" + i_month);

        }

    } //End of iteration.

    //ProductionBalancing - To Be done
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void balanceProductionTill(int year, int m) throws Exception {

        Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.year=" + year + " and mpp.month=:i_m");
        q.setParameter("i_m", Helper.translateMonth(m));
        MonthlyProductionPlan planTillMonthCursor = (MonthlyProductionPlan) q.getResultList().get(0);

        int deficit = 0;
        String endmonth = planTillMonthCursor.getMonth().toString();
        while (planTillMonthCursor.getPrevMonthlyProductionPlan() != null) {

            if (planTillMonthCursor.isLocked()) {
                break;
            }
            int maxCapacity = planTillMonthCursor.getManufacturingFacility().findProductionCapacity(planTillMonthCursor.getFurnitureModel()).getQty() * planTillMonthCursor.getNumWorkDays();
            int requirement = (int) (planTillMonthCursor.getTotalDemand());
            int fufill = Math.min(requirement + deficit, maxCapacity);
            planTillMonthCursor.setQTY(fufill);
            deficit += requirement - fufill;

            System.out.println("ManageProductionPlanning: Planned capacity for Year:" + planTillMonthCursor.getYear() + " month:" + planTillMonthCursor.getMonth().value + " PRODUCE=" + fufill);

            planTillMonthCursor = planTillMonthCursor.getPrevMonthlyProductionPlan();
        }

        if (deficit > 0) {
            throw new RuntimeException("Unable to fufill requirements! Deficit by " + deficit + " For month till " + endmonth);
        }

    }

    //Add a weeklyProduction Plan
    private WeeklyProductionPlan addWeeklyPlan(MonthlyProductionPlan mpp) {

        WeeklyProductionPlan wpp = null;
        try {

            wpp = new WeeklyProductionPlan();
            wpp.setMonthlyProductionPlan(mpp);
            wpp.setWeekNo(mpp.getWeeklyProductionPlans().size() + 1);

            em.persist(wpp);
        } catch (Exception ex) {
        }

        return wpp;

    }

    //This process plans MPP and split it evenly across weeks
    private void planMPP(MonthlyProductionPlan mpp) throws ProductionPlanExceedsException {
        Calendar cal = Calendar.getInstance();
        cal.set(mpp.getYear(), mpp.getMonth().value, 1);
        int maxWeekNumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int rPerWeek = (int) Math.floor((mpp.getQTY().doubleValue() / maxWeekNumber));

        for (int i = 1; i <= maxWeekNumber; i++) {

            WeeklyProductionPlan wp = addWeeklyPlan(mpp);
            if (i == maxWeekNumber) {
                wp.setQTY(mpp.getQTY() - rPerWeek * (maxWeekNumber - 1));
            } else {
                wp.setQTY(rPerWeek);
            }

        }

    }
}
