/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingCapacity;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.WeeklyProductionPlan;
import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurniture.EJB.Exceptions.ProductionPlanExceedsException;
import IslandFurniture.EJB.Exceptions.ProductionPlanNoCN;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.ejb.ApplicationException;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.jasper.tagplugins.jstl.ForEach;

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

        Stream<MonthlyStockSupplyReq> sorted_MSSRL = MSSRL.stream().sorted(byMY);

        boolean firstrow = true;
        int latest_month = 0;
        int latest_year = 0;
        for (Object o : sorted_MSSRL.toArray()) {

            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            if (firstrow) {
                firstrow = false;
                latest_month = mssr.getMonth().value;
                latest_year = mssr.getYear();

            }
            CreateProductionPlanFromForecast(mssr);
        }
        if (latest_year > 0) {
            balanceProductionTill(latest_year, latest_month);
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
        long month_gap = (Year - c_year) * 12 + (month - c_month);

        long month_gap_semiannual = Math.max(month_gap, 5);
        //Loop to ensure get all MPP before are created up to the month

        MonthlyProductionPlan prev_mpp = null;
        for (int i = 0; i <= month_gap_semiannual; i++) {
            int i_month = ((c_month + i - 1) % 12) + 1;
            int i_year = Math.floorDiv((c_month + i - 1), 12) + c_year;

            Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.year=" + i_year + " and mpp.month=:i_m");
            q.setParameter("i_m", Helper.TranslateMonth(i_month));

            if (q.getResultList().size() == 0) {
                Month E_month = Helper.TranslateMonth(i_month);
                mpp = new MonthlyProductionPlan();
                mpp.setMonth(E_month);
                mpp.setYear((int) i_year);
                mpp.setFurnitureModel((FurnitureModel) furnitureModel);;
                mpp.setQTY(0); //Brand New
                if (i == 0) {
                    mpp.setLocked(true); //prevent planning for this month
                }

            } else {
                mpp = (MonthlyProductionPlan) q.getResultList().get(0);
            }

            if (prev_mpp != null && !prev_mpp.equals(mpp)) {
                mpp.setPrevMonthlyProcurementPlan(prev_mpp);
                prev_mpp.setNextMonthlyProcurementPlan(mpp);
            }

            if (mpp.getPc() == null) {
                Query v = em.createQuery("select c from ProductionCapacity c where c.manufacturingFacility.id=" + MF.getId() + " and c.stock.id=" + furnitureModel.getId());
                if (v.getResultList().size() > 0) {
                    mpp.setPc((ProductionCapacity) v.getResultList().get(0));

                }
            }
            //Tag monthlyProductionPlan to MonthlyStockSupply
            if (i == month_gap && !mpp.getMonthlyStockSupplyReqs().contains(MSSR)) {
                mpp.getMonthlyStockSupplyReqs().add(MSSR);
            }

            prev_mpp = mpp;

            em.persist(mpp);
            em.flush();

            System.err.println("ManageProductionPlanning: Created production planning for " + i_year + "/" + i_month);

        }

    } //End of iteration.

    //ProductionBalancing - To Be done
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void balanceProductionTill(int year, int m) throws Exception {

        Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.year=" + year + " and mpp.month=:i_m");
        q.setParameter("i_m", Helper.TranslateMonth(m));
        MonthlyProductionPlan plantillmonthcursor = (MonthlyProductionPlan) q.getResultList().get(0);

        int deficit = 0;
        String endmonth = plantillmonthcursor.getMonth().toString();
        while (plantillmonthcursor.getPrevMonthlyProcurementPlan() != null) {

            if (plantillmonthcursor.isLocked()) {
                break;
            }
            int max_capacity = ((ProductionCapacity) plantillmonthcursor.getProductionCapacity()).getQty();
            int requirement = (int) (plantillmonthcursor.get_total_demand());
            int fufill = Math.min(requirement + deficit, max_capacity);
            plantillmonthcursor.setQTY(fufill);
            deficit += requirement - fufill;

            System.out.println("ManageProductionPlanning: Planned capacity for Year:" + plantillmonthcursor.getYear() + " month:" + plantillmonthcursor.getMonth().value + " PRODUCE=" + fufill);

            plantillmonthcursor = plantillmonthcursor.getPrevMonthlyProcurementPlan();
        }

        if (deficit > 0) {
            throw new RuntimeException("Unable to fufill requirements! Deficit by " + deficit + " For month till " + endmonth);
        }

    }

    //Add a weeklyProduction Plan
    private WeeklyProductionPlan AddWeeklyPlan(MonthlyProductionPlan mpp) {

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
        int maxWeeknumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int rperweek = (int) Math.floor((mpp.getQTY().doubleValue() / maxWeeknumber));

        for (int i = 1; i <= maxWeeknumber; i++) {

            WeeklyProductionPlan wp = AddWeeklyPlan(mpp);
            if (i == maxWeeknumber) {
                wp.setQTY(mpp.getQTY() - rperweek * (maxWeeknumber - 1));
            } else {
                wp.setQTY(rperweek);
            }

        }

    }
}
