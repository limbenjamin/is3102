/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.WeeklyProductionPlan;
import IslandFurniture.StaticClasses.Helper.Helper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author James
 */
@Stateless
@LocalBean
public class ProductionPlanningBean {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

//Create MonthlyProductionPlan
    public MonthlyProductionPlan CreateProductionPlan(int month, long Year, FurnitureModel furnitureModel) {
        MonthlyProductionPlan mpp = null;
        try {
            Month E_month = Helper.TranslateMonth(month);
            mpp = new MonthlyProductionPlan();
            mpp.setMonth(E_month);
            mpp.setYear((int) Year);
            mpp.setFurnitureModel(furnitureModel);
            mpp.setWeeklyProductionPlans(new ArrayList<WeeklyProductionPlan>());
            em.persist(mpp);
        } catch (Exception ex) {
        }

        return mpp;

    }

    //Add a weeklyProduction Plan
    public WeeklyProductionPlan AddWeeklyPlan(MonthlyProductionPlan mpp, FurnitureModel furnitureModel) {

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

    public void planMPP(MonthlyProductionPlan mpp) {
        Calendar cal = Calendar.getInstance();
        cal.set(mpp.getYear(), mpp.getMonth().value,1);
        int maxWeeknumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

}
