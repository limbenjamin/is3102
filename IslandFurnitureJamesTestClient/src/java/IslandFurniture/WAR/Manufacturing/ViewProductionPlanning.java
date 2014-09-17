/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlan;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
import IslandFurniture.StaticClasses.Helper.Helper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@SessionScoped
@ManagedBean(name = "DemandPlanning")
public class ViewProductionPlanning implements Serializable {

    @EJB
    private ManageProductionPlanningRemote mpp;

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private List<SelectItem> MF_LIST;
    private String MF;

    @PostConstruct
    public void init() {
        System.out.println("ViewProductionPlanning() Init!");
        MF_LIST = new ArrayList<SelectItem>();
        Query q = em.createQuery("Select MF from ManufacturingFacility MF ORDER BY MF.country.name");
        for (ManufacturingFacility MF : (List<ManufacturingFacility>) q.getResultList()) {
            MF_LIST.add(new SelectItem(MF.getName(), MF.getCountry().getName() + "/" + MF.getName()));
        }
    }

    public List<SelectItem> getMF_LIST() {
        return MF_LIST;
    }

    public String getMF() {
        return MF;
    }

    public void setMF(String MF) {
        mpp.setCN(MF);
        System.out.println("ViewProductionPlanning() MF Changed to " + MF);
        this.MF = MF;

    }

    public String getPsHTML() {
        int m = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), 6, true);
        int y = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), 6, false);
        String html = "";
        Query q = em.createQuery("select MPP from MonthlyProductionPlan MPP where ((MPP.month+1)+MPP.year*12)<=(:m+1)+:y*12 and MPP.locked=false order by MPP.furnitureModel.name , ((MPP.month+1)+MPP.year*12 DESC)"); //                //em.createNamedQuery("MonthlyProductionPlan.FindUntilAllModel");
        q.setParameter("m", m);
        q.setParameter("y", y);
        String fm = "";
        String dump = "";
        for (MonthlyProductionPlan mppi : (List<MonthlyProductionPlan>) q.getResultList()) {
            if (mppi.getFurnitureModel().getName() != fm) {
                fm = mppi.getFurnitureModel().getName();
                html += "<h2>Planning for " + fm + "</h2>";

                //Create rows
                dump = "";
                Month c_m = null;
                try {
                    c_m = Helper.translateMonth(m);
                } catch (Exception ex) {
                }

                int c_y = y;

                while (!(c_m == Helper.getCurrentMonth() && c_y == Helper.getCurrentYear())) {

                    try {
                        c_m = Helper.translateMonth(Helper.addMonth(c_m, y, -1, true));
                    } catch (Exception ex) {
                    }
                    c_y = Helper.addMonth(c_m, c_y, -1, false);
                    dump = "<th>" + c_m.toString() + "/" + c_y + "</th>" + dump;
                }
                dump = "<table border='1'><tr><th style='width:200px'></th>" + dump + "</tr>";
                MonthlyProductionPlan temp = mppi;
                String r = "";
                while (temp != null) {
                    r = "<td>" + temp.getTotalDemand(em) + "</td>" + r;
                    temp = temp.getPrevMonthlyProductionPlan(em);
                }
                r = "<tr><td>Total Required:</td>" + r + "</tr>";
                dump += r;

                temp = mppi;
                r = "";
                while (temp != null) {
                    r = "<td>" + temp.getQTY() + "</td>" + r;
                    temp = temp.getPrevMonthlyProductionPlan(em);
                }
                r = "<tr><td>Planned Production:</td>" + r + "</tr>";
                dump += r;

                temp = mppi;
                r = "";
                while (temp != null) {
                    
                    
                    r = "<td>" + temp.getManufacturingFacility().getCurrentFreeCapacity(em, temp.getMonth(), temp.getYear()) + "</td>" + r;
                    temp = temp.getPrevMonthlyProductionPlan(em);
                }
                r = "<tr><td>Factory Available Capacity:</td>" + r + "</tr>";
                dump += r;

                dump += "</table>";

                html += dump;
            }

        }

        return html;
    }

}
