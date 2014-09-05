/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.WeeklyProductionPlan;
import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurnitures.EJB.Exceptions.ProductionPlanExceedsException;
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
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ProductionPlanningBean {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public void CreateProductionPlanFromForecast(List<MonthlyStockSupplyReq> MSSRL) throws ProductionPlanExceedsException {

        //Start from last date to earlist date
        Comparator<MonthlyStockSupplyReq> byMY = (e1, e2) -> Integer.compare((int) (e1.getYear() & e1.getMonth().value), (int) (e2.getYear() & e2.getMonth().value));
        Stream<MonthlyStockSupplyReq> sorted_MSSRL = MSSRL.stream().sorted(byMY);

        for (Object o : sorted_MSSRL.toArray()) {
            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            CreateProductionPlanFromForecast(mssr);
        }

    }

    public MonthlyProductionPlan CreateProductionPlanFromForecast(MonthlyStockSupplyReq MSSR) throws ProductionPlanExceedsException {

        MonthlyProductionPlan MPP = CreateModifyProductionPlan(MSSR.getMonth().value, MSSR.getYear(), MSSR.getStock());
        planMPP(MPP);
        return (MPP);
    }

//Create MonthlyProductionPlan
    public MonthlyProductionPlan CreateModifyProductionPlan(int month, long Year, Stock furnitureModel) {
        MonthlyProductionPlan mpp = null;
        
        Query q = em.createQuery("select mpp  from MonthlyProductionPlan mpp where mpp.locked=false and mpp.year=0 and mpp.month=");
        
        if (q.getResultList().size()==0){
        
        try {
            Month E_month = Helper.TranslateMonth(month);
            mpp = new MonthlyProductionPlan();
            mpp.setMonth(E_month);
            mpp.setYear((int) Year);
            mpp.setFurnitureModel((FurnitureModel) furnitureModel);
            mpp.setWeeklyProductionPlans(new ArrayList<WeeklyProductionPlan>());
            em.persist(mpp);
        } catch (Exception ex) {
        }
        }else{
        
        }

        return mpp;

    }

    //Add a weeklyProduction Plan
    public WeeklyProductionPlan AddWeeklyPlan(MonthlyProductionPlan mpp) {

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
    public void planMPP(MonthlyProductionPlan mpp) throws ProductionPlanExceedsException {
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

    public void ExecuteMPP(MonthlyProductionPlan mpp) {

    }
}
