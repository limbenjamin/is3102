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
import IslandFurnitures.EJB.Exceptions.ProductionPlanExceedsException;
import IslandFurnitures.EJB.Exceptions.ProductionPlanNoCN;
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
@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ManageProductionPlanning {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    private ManufacturingFacility MF = null;

    private void persist(Object object) {
        em.persist(object);
    }

    //Public method. Country must set first.
    public void setCN(Country cn) {
        MF = (ManufacturingFacility) em.createQuery("select mf from ManufacturingFacility mf where mf.country.id=" + cn.getId()).getResultList().get(1);

    }

    //Public method , pass a list of forecast to see if it is feasible.
    public void CreateProductionPlanFromForecast(List<MonthlyStockSupplyReq> MSSRL) throws ProductionPlanExceedsException, ProductionPlanNoCN, Exception {
        if (MF == null) {
            throw new ProductionPlanNoCN();
        }
        //Start from last date to earlist date
        Comparator<MonthlyStockSupplyReq> byMY = (e1, e2) -> Integer.compare((int) (e1.getYear() & e1.getMonth().value), (int) (e2.getYear() & e2.getMonth().value));
        Stream<MonthlyStockSupplyReq> sorted_MSSRL = MSSRL.stream().sorted(byMY);

        for (Object o : sorted_MSSRL.toArray()) {
            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            CreateProductionPlanFromForecast(mssr);
        }

    }

    private void CreateProductionPlanFromForecast(MonthlyStockSupplyReq MSSR) throws ProductionPlanExceedsException, Exception {

        int month = MSSR.getMonth().value;
        int Year = MSSR.getYear();
        FurnitureModel furnitureModel = (FurnitureModel) MSSR.getStock();

        MonthlyProductionPlan mpp = null;

        int c_year = Calendar.getInstance().get(Calendar.YEAR);
        int c_month = Calendar.getInstance().get(Calendar.MONTH);
        long month_gap = (Year - c_year) * 12 + (month - c_month);
        //Loop to ensure get all MPP before are created up to the month
        
        MonthlyProductionPlan prev_mpp=null;
        for (int i = 0; i <= month_gap; i++) {
            int i_month = ((c_month + i - 1) % 12) + 1;
            int i_year = Math.floorDiv((c_month + i - 1), 12) + c_year;

            Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.year=" + i_year + " and mpp.month.value=" + i_month);
            
            
            
            if (q.getResultList().size() == 0) {
                Month E_month = Helper.TranslateMonth(i_month);
                mpp = new MonthlyProductionPlan();
                mpp.setMonth(E_month);
                mpp.setYear((int) i_year);
                mpp.setFurnitureModel((FurnitureModel) furnitureModel);;
                mpp.setQTY(0); //Brand New
                if(prev_mpp != null){
//                    mpp.setPrevMonthlyProcurementPlan(prev_mpp);
//                    prev_mpp.setNextMonthlyProcurementPlan(mpp);
                }
                    em.persist(mpp);
            } else {
                mpp = (MonthlyProductionPlan) q.getResultList().get(0);
            }
            if (mpp.getPc() == null) {
                Query v = em.createQuery("select c from ProductionCapacity c where c.manufacturingFacility.id=" + MF.getId() + " and c.stock.id=" + furnitureModel.getId());
                if (v.getResultList().size() > 0) {
                    mpp.setPc((ProductionCapacity) v.getResultList().get(0));
                    em.persist(mpp);
                }
            }            
            //Tag monthlyProductionPlan to MonthlyStockSupply
            if (!mpp.getMonthlyStockSupplyReqs().contains(MSSR)) {
                mpp.getMonthlyStockSupplyReqs().add(MSSR);
                em.persist(mpp);
            }
            
            prev_mpp=mpp;

        }

    } //End of iteration.

     //ProductionBalancing - To Be done
    private void balanceProduction() {
        return;

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
