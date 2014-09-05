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
public class ProductionPlanningBean {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    private ManufacturingFacility MF=null;

    private void persist(Object object) {
        em.persist(object);
    }

    //Public method. Country must set first.
    public void setCN(Country cn)
    {
       MF=(ManufacturingFacility)em.createQuery("select mf from ManufacturingFacility mf where mf.country.id="+cn.getId()).getResultList().get(1);
       
    }
    
    
    //Public method 
    public void CreateProductionPlanFromForecast(List<MonthlyStockSupplyReq> MSSRL) throws ProductionPlanExceedsException, ProductionPlanNoCN {
           if (MF==null) throw new ProductionPlanNoCN();
        //Start from last date to earlist date
        Comparator<MonthlyStockSupplyReq> byMY = (e1, e2) -> Integer.compare((int) (e1.getYear() & e1.getMonth().value), (int) (e2.getYear() & e2.getMonth().value));
        Stream<MonthlyStockSupplyReq> sorted_MSSRL = MSSRL.stream().sorted(byMY);

        for (Object o : sorted_MSSRL.toArray()) {
            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            CreateProductionPlanFromForecast(mssr);
        }

    }

    private MonthlyProductionPlan CreateProductionPlanFromForecast(MonthlyStockSupplyReq MSSR) throws ProductionPlanExceedsException {
        
        

        MonthlyProductionPlan MPP = CreateOverwriteProductionPlan(MSSR.getMonth().value, MSSR.getYear(), MSSR.getStock());
        planMPP(MPP);
        return (MPP);
    }

//Create MonthlyProductionPlan
    private MonthlyProductionPlan CreateOverwriteProductionPlan(int month, long Year, Stock furnitureModel) {
        MonthlyProductionPlan mpp = null;
        
        Query q = em.createQuery("select mpp  from MonthlyProductionPlan mpp where mpp.locked=false and mpp.year="+ Year + " and mpp.month.value=" + month);
        ManufacturingCapacity capacity=(ManufacturingCapacity)em.createQuery("select cpp from ManufacturingCapacity cpp where cpp.stock.id="+furnitureModel.getId()+" and cpp.manufacturingFacility.id="+MF.getId());
        //Lets create mpp for other months from current month to that month if they dont exist
        
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
            mpp = (MonthlyProductionPlan)q.getResultList().get(0);
            
        }

        return mpp;

    }

    //ProductionBalancing
    private void balanceProduction(){
        
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
