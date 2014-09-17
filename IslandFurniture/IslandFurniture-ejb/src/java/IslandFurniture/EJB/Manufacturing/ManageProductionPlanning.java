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
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.WeeklyProductionPlan;
import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurniture.EJB.Exceptions.ProductionPlanExceedsException;
import IslandFurniture.EJB.Exceptions.ProductionPlanNoCN;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.management.RuntimeErrorException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
* This powerful Bean does production planning on a specific manufacturing facility
*/

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ManageProductionPlanning implements ManageProductionPlanningRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    private ManufacturingFacility MF = null;

    private void persist(Object object) {
        em.persist(object);
    }

    @Override
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

    @Override
    public void createOrUpdateCapacity(String fmName, String mancFacName, int daily_max_capacity) {

        FurnitureModel fm = Helper.getFirstObjectFromQuery("SELECT FM from FurnitureModel FM where FM.name='" + fmName + "'", em);
        ManufacturingFacility MF = Helper.getFirstObjectFromQuery("SELECT MF from ManufacturingFacility MF where MF.name='" + mancFacName + "'", em);
        Query q = em.createNamedQuery("ProductionCapacity.findPC");
        q.setParameter("MFNAME", MF);
        q.setParameter("FMNAME", fm);

        if (q.getResultList().size() > 0) {
            ProductionCapacity pc = (ProductionCapacity) q.getResultList().get(0);
            pc.setQty(daily_max_capacity);
            em.persist(pc);
            System.err.println("Successfully Updated Production Capacity: for " + mancFacName + "/" + fmName);
            return;
        }

        ProductionCapacity pc = new ProductionCapacity();
        pc.setFurnitureModel(fm);
        pc.setManufacturingFacility(MF);
        pc.setQty(daily_max_capacity);
        em.persist(pc);

        System.err.println("Successfully Created Production Capacity: for " + mancFacName + "/" + fmName);

    }

    public void setCN(Country cn) {
        MF = (ManufacturingFacility) em.createQuery("select mf from ManufacturingFacility mf where mf.country.id=" + cn.getId()).getResultList().get(0);

    }

    @Override
    //Plan Production Planning 6 months in advance
    public void CreateProductionPlanFromForecast() throws Exception {
        try {
            int m = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), 6, true);
            int y = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), 6, false);
            System.out.println("CreateProductionPlanFromForecast(): Planning Production Planning 6 months in advance until " + Helper.translateMonth(m).toString() + "/" + y);
            CreateProductionPlanFromForecast(m, y);
        } catch (Exception ex) {
            Logger.getLogger(ManageProductionPlanning.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Public method. Plan all requirements
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void CreateProductionPlanFromForecast(int m, int year) throws Exception {
        Query q = em.createNamedQuery("MonthlyStockSupplyReq.finduntiltime");
        q.setParameter("y", year);
        q.setParameter("m", Helper.translateMonth(m).value);
        System.out.println("CreateProductionPlanFromForecast(): Planning Production until Year:" + year + " and Month: " + Helper.translateMonth(m).toString());

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
                        return (Integer.compare((int) (e2.getYear() * 12 + e2.getMonth().value), (int) (e1.getYear() * 12 + e1.getMonth().value)));
                    }
                };

        Stream<MonthlyStockSupplyReq> sortedMSSRL = MSSRL.stream().sorted(byMY);

        boolean firstrow = true;

        for (Object o : sortedMSSRL.toArray()) {

            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            if (firstrow) {
                firstrow = false;

            }
            CreateProductionPlanFromForecast(mssr);
        }

        balanceProductionTill();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void CreateProductionPlanFromForecast(MonthlyStockSupplyReq MSSR) throws ProductionPlanExceedsException, Exception {

        int month = MSSR.getMonth().value;
        int Year = MSSR.getYear();
        FurnitureModel furnitureModel = (FurnitureModel) MSSR.getStock();

        MonthlyProductionPlan mpp = null;

        int c_year = Helper.getCurrentYear();
        int c_month = Helper.getCurrentMonth().value;
        long monthGap = (Year - c_year) * 12 + (month - c_month);

        long monthGap_semiannual = Math.max(monthGap, 6);
        //Loop to ensure get all MPP before are created up to the month

        MonthlyProductionPlan prevMpp = null;
        for (int i = 0; i <= monthGap_semiannual; i++) {

            int i_month = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, true);
            int i_year = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, false);

            Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.furnitureModel=:fm and mpp.year=" + i_year + " and mpp.month=:i_m");
            q.setParameter("i_m", Helper.translateMonth(i_month));
            q.setParameter("fm", MSSR.getStock());

            if (q.getResultList().size() == 0) {
                Month eMonth = Helper.translateMonth(i_month);
                mpp = new MonthlyProductionPlan();
                mpp.setMonth(eMonth);
                mpp.setYear((int) i_year);
                mpp.setFurnitureModel((FurnitureModel) furnitureModel);
                mpp.setManufacturingFacility(MF);
                mpp.setQTY(0); //Brand New
                System.err.println("CreateProductionPlanFromForecast(): Created production planning for " + mpp.getFurnitureModel().getName() + " period: " + i_year + "/" + Helper.translateMonth(i_month).toString());

                if (i == 0) {
                    mpp.setLocked(true); //prevent planning for this month
                }

            } else {
                mpp = (MonthlyProductionPlan) q.getResultList().get(0);
            }

            prevMpp = mpp;

            em.persist(mpp);
            em.flush();

        }

    } //End of iteration.

    @Override
    public double getAvailCapacity(int year, int m) throws Exception {
        return (year - Helper.getCurrentYear()) * 12 + (m - Helper.getCurrentMonth().value);
    }

    @Override
    public double getReqCapacity(int year, int m) throws Exception {
        //Calculate Required Capacity First
        double reqCapacity = 0;
        Query q2 = em.createNamedQuery("MonthlyStockSupplyReq.finduntiltime");
        q2.setParameter("y", year);
        q2.setParameter("m", m);

        for (Object o : q2.getResultList()) {
            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            double maxCapacity = MF.findProductionCapacity((FurnitureModel) mssr.getStock()).getCapacity(mssr.getMonth(), mssr.getYear());
            reqCapacity += mssr.getQtyRequested() / maxCapacity;
        }

        return (reqCapacity);
    }

    private void balanceProductionTill() throws Exception {
        MonthlyProductionPlan latest = (MonthlyProductionPlan) em.createQuery("select mpp from MonthlyProductionPlan mpp order by mpp.year*12 + mpp.month DESC").getResultList().get(0);
        balanceProductionTill(latest.getYear(), latest.getMonth().value);
    }

    //ProductionBalancing - To Be done
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void balanceProductionTill(int year, int m) throws Exception {

        Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.year=" + year + " and mpp.month=:i_m order by mpp.furnitureModel.name");
        q.setParameter("i_m", Helper.translateMonth(m));

        //Reset Monthly Production First
        Query l = em.createNamedQuery("MonthlyProductionPlan.FindUntilAllModel");
        l.setParameter("m", m);
        l.setParameter("y", year);
        for (MonthlyProductionPlan mpp : (List<MonthlyProductionPlan>) l.getResultList()) {
            mpp.setQTY(0);
        }

        for (MonthlyProductionPlan planTillMonthCursor : (List<MonthlyProductionPlan>) q.getResultList()) {

            double reqCapacity = getReqCapacity(year, m);
            double AvaCapacity = getAvailCapacity(year, m);

            if (reqCapacity > AvaCapacity) {
                throw new RuntimeException("Insufficient Capacity to fufill current requirement till " + m + "/" + year);
            }

            int deficit = 0;
            String endmonth = planTillMonthCursor.getMonth().toString();
            while (planTillMonthCursor.getPrevMonthlyProductionPlan(em) != null) {

                if (planTillMonthCursor.isLocked()) {
                    break;
                }
                int avaCapacity = planTillMonthCursor.getManufacturingFacility().findProductionCapacity(planTillMonthCursor.getFurnitureModel()).getCapacity(planTillMonthCursor.getMonth(), planTillMonthCursor.getYear());
                avaCapacity = (int) (avaCapacity * planTillMonthCursor.getManufacturingFacility().getCurrentFreeCapacity(em, planTillMonthCursor.getMonth(), planTillMonthCursor.getYear()));

                int requirement = (int) (planTillMonthCursor.getTotalDemand(em));
                int fufill = Math.min(requirement + deficit, avaCapacity);
                planTillMonthCursor.setQTY(fufill);
                deficit += requirement - fufill;

                System.out.println("balanceProductionTill(): Planned capacity for " + planTillMonthCursor.getFurnitureModel().getName() + " Period @ Year:" + planTillMonthCursor.getYear() + " month:" + planTillMonthCursor.getMonth().toString() + " PRODUCE=" + fufill + " Resultant Plant Free Capacity @" + planTillMonthCursor.getManufacturingFacility().getCurrentFreeCapacity(em, planTillMonthCursor.getMonth(), planTillMonthCursor.getYear()));

                planWeekMPP(planTillMonthCursor);
                em.persist(planTillMonthCursor);

                planTillMonthCursor = planTillMonthCursor.getPrevMonthlyProductionPlan(em);
            }

            if (deficit > 0) {
                throw new RuntimeException("balanceProductionTill()Unknown Error: Unable to fufill requirements! Deficit by " + deficit + " For month till " + endmonth);
            }
        }
        
        System.out.println("balanceProductionTill(): Successful Production Planning !");

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
    private void planWeekMPP(MonthlyProductionPlan mpp) throws ProductionPlanExceedsException {
        Calendar cal = Calendar.getInstance();
        cal.set(mpp.getYear(), mpp.getMonth().value, 1);
        int maxWeekNumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int rPerWeek = (int) Math.floor((mpp.getQTY().doubleValue() / maxWeekNumber));
        mpp.getWeeklyProductionPlans().clear();
        for (int i = 1; i <= maxWeekNumber; i++) {

            WeeklyProductionPlan wp = addWeeklyPlan(mpp);
            if (i == maxWeekNumber) {
                wp.setQTY(mpp.getQTY() - rPerWeek * (maxWeekNumber - 1));
            } else {
                wp.setQTY(rPerWeek);
            }
            System.out.println("Planned Year:" + mpp.getYear() + " month:" + mpp.getMonth() + " Week: " + i + " Split Product=" + wp.getQTY());
            mpp.getWeeklyProductionPlans().add(wp);
        }

    }
}
