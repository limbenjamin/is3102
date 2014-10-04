/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Enums.PurchaseOrderStatus;
import IslandFurniture.EJB.Entities.Supplier;
import IslandFurniture.EJB.Entities.WeeklyMRPRecord;
import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurniture.StaticClasses.Helper.QueryMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import static javax.ejb.LockType.WRITE;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Startup
@Singleton
@Lock(WRITE)
public class ManageProductionPlanTimerBean implements ProductionPlanningSingletonRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    public static currentdate cdate = new currentdate();
    private Integer stepLeft = 0;

    public currentdate getCdate() {
        return cdate;
    }

    public void setCdate(currentdate cdate) {
        this.cdate = cdate;
    }

    @Override
    public void setAdvanceWeek(int week) {
        this.stepLeft = week;
        System.out.println("ManageProductionPlanTimerBean(): Stepping" + stepLeft);
    }

    public static class currentdate {

        private Integer month = 1;
        private Integer week = 1;
        private Integer year = 2014;

        public currentdate() {
            try {
                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.MONDAY);
                month = c.get(Calendar.MONTH);
                Double weekz = Math.ceil(c.get(Calendar.DAY_OF_MONTH) / 7.0);
                week = weekz.intValue();
                year = c.get(Calendar.YEAR);

                System.out.println("Manufacturing Facility Timer Started: Now is " + week + "/" + (month + 1) + "/" + year);
            } catch (Exception ex) {
                Logger.getLogger(ManageProductionPlanTimerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void addWeek() throws Exception {
            Integer t_month = Helper.addoneWeek(month, year, week, 1, Calendar.MONTH);
            Integer t_week = Helper.addoneWeek(month, year, week, 1, Calendar.WEEK_OF_MONTH);
            Integer t_year = Helper.addoneWeek(month, year, week, 1, Calendar.YEAR);

            this.month = t_month;
            this.week = t_week;
            this.year = t_year;
            System.out.println("Event() : Now is " + week + "/" + (month + 1) + "/" + year);
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Integer getWeek() {
            return week;
        }

        public void setWeek(Integer week) {
            this.week = week;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Calendar getCalendar() throws Exception {

            return (Helper.getStartDateOfWeek(month, year, week));

        }

    }

    public ManageProductionPlanTimerBean() {
        stepLeft = 0;
        System.out.println("ManageProductionPlanTimerBean(): Production Planning Automation Started !");

    }

    @Schedule(second = "*/2", hour = "*", minute = "*", info = "Production Planning")
    public void automaticProductionPlanning() throws Exception {

        if (stepLeft <= 0) {
            return;
        }

        stepLeft--;

        //oh one month has passed
        cdate.addWeek();

        // Supplier pivoting problem
        Query q = em.createQuery("select wmrp from WeeklyMRPRecord wmrp where wmrp.purchaseOrderDetail.purchaseOrder IS NULL and (wmrp.orderYear*1000+wmrp.orderMonth*10+wmrp.orderWeek)<=(:y*1000+:m*10+:w) order by wmrp.manufacturingFacility.name desc");
        q.setParameter("m", cdate.getMonth());
        q.setParameter("y", cdate.getYear());
        q.setParameter("w", cdate.getWeek());

        PurchaseOrder po = null;
        Supplier csupplier = null;
        Map<Supplier, List<WeeklyMRPRecord>> data = new HashMap<Supplier, List<WeeklyMRPRecord>>();

        for (WeeklyMRPRecord wmrp : (List<WeeklyMRPRecord>) q.getResultList()) {

            if (wmrp.getOrderAMT() <= 0) {
                continue;
            }

            if (data.get(QueryMethods.getSupplierByMfAndM(em, wmrp.getManufacturingFacility(), wmrp.getMaterial())) == null) {
                data.put(QueryMethods.getSupplierByMfAndM(em, wmrp.getManufacturingFacility(), wmrp.getMaterial()), new ArrayList<WeeklyMRPRecord>());
            }
            data.get(QueryMethods.getSupplierByMfAndM(em, wmrp.getManufacturingFacility(), wmrp.getMaterial())).add(wmrp);
        }

        ManufacturingFacility mf = new ManufacturingFacility();

        for (Supplier s : data.keySet()) {
            mf = new ManufacturingFacility();
            mf.setName("NULL");
            for (WeeklyMRPRecord wmrp : (List<WeeklyMRPRecord>) data.get(s)) {
                if (!wmrp.getManufacturingFacility().getName().equals(mf.getName())) {
                    po = new PurchaseOrder();
                    po.setOrderDate(cdate.getCalendar());
                    po.setStatus(PurchaseOrderStatus.PLANNED);
                    po.setSupplier(s);
                    po.setShipsTo(wmrp.getManufacturingFacility());
                    po.setManufacturingFacility(wmrp.getManufacturingFacility());
                    em.persist(po);
                    mf = wmrp.getManufacturingFacility();
                }
                po.getPurchaseOrderDetails().add(wmrp.getPurchaseOrderDetail());
                wmrp.getPurchaseOrderDetail().setPurchaseOrder(po);
                System.out.println("automaticOrderMaterials(): PO(" + po + ") Ordering for " + po.getShipsTo() + " Material=" + wmrp.getMaterial() + " ORDER="+wmrp.getOrderAMT()+ "Supplier=" + s);
            }

            System.out.println("Now Supplier=" + s);

        }

    }

    public void persist(Object object) {
        em.persist(object);
    }
}
