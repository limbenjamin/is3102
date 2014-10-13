/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Material;
import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.MonthlyProductionPlan;
import IslandFurniture.Entities.MonthlyProductionPlanPK;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProductionCapacity;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.WeeklyMRPRecord;
import IslandFurniture.Entities.WeeklyProductionPlan;
import IslandFurniture.StaticClasses.Helper;
import IslandFurniture.StaticClasses.QueryMethods;
import IslandFurnitures.DataStructures.JDataTable;
import IslandFurnitures.DataStructures.JDataTable.Cell;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Stateful
public class ManageProductionPlanningWebFunctions implements ManageProductionPlanningLocalInterface {

    @EJB
    private ManageProductionPlanningLocal mpp;

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private Integer month;
    private Integer year;

    public ManageProductionPlanningWebFunctions() {
        try {
            if (this.month == null) {
                this.month = ManageProductionPlanTimerBean.cdate.getCalendar().get(Calendar.MONTH);
            }
            if (this.year == null) {
                this.year = ManageProductionPlanTimerBean.cdate.getCalendar().get(Calendar.YEAR);
            }
        } catch (Exception ex) {
        }
    }

    public HashMap<String, String>
            getAuthorizedMF(String AUTH) {

        Query q = em.createNamedQuery("getAllMFs");
        HashMap<String, String> temp = new HashMap<String, String>();
        Staff staff = null;
        if (AUTH != "") {
            Query j = em.createQuery("select s from Staff s where s.username=:u");
            j.setParameter("u", AUTH);
            staff = (Staff) j.getResultList().get(0);

        }

        for (ManufacturingFacility MF : (List<ManufacturingFacility>) q.getResultList()) {
            if (AUTH != "") {

                if (!staff.getPlant().equals(MF)) {
                    continue;
                }

            }

            temp.put(MF.getName(), MF.getCountry().getName() + "/" + MF.getName());
        }

        return (temp);

    }

    @Override
    public Object getCapacityList(String MF) {
        ManufacturingFacility mff = (ManufacturingFacility) em.createQuery("Select Mf from ManufacturingFacility Mf where Mf.name='" + MF + "'").getSingleResult();
        try {
            mpp.setMF(mff.getName());
            mpp.createCapacityIFDoNotExist();
        } catch (Exception ex) {
        }

        Query q = em.createNamedQuery("ProductionCapacity.findPCbyMF");

        q.setParameter("MFNAME", mff);
        JDataTable<String> dt = new JDataTable<String>();
        dt.Title = "Capacity Management";

        dt.columns.add("Furniture Model");
        dt.columns.add("Maximum Daily Production");

        int c_year = 0;
        try {
            c_year = Helper.getCurrentYear();
        } catch (Exception ex) {

        }
        int c_month = Helper.getCurrentMonth().value;

        for (int i = 0; i <= 6; i++) {

            try {
                int i_month = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, true);
                int i_year = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, false);

                dt.columns.add(Helper.translateMonth(i_month) + "/" + i_year + "<br/>[" + Helper.getNumWorkDays(Helper.translateMonth(i_month), i_year) + "]");
            } catch (Exception ex) {
            }
        }

        for (ProductionCapacity pc : (List<ProductionCapacity>) q.getResultList()) {

            JDataTable.Row r = dt.newBindedRow(pc);

            r.newCell(pc.getFurnitureModel().getName());
            r.newBindedCell(pc.getQty().toString(), "Qty").setIsEditable(true);

            for (int i = 0; i <= 6; i++) {

                try {
                    int i_month = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, true);
                    int i_year = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, false);

                    long availableCapacity = Math.round(mff.findProductionCapacity(pc.getFurnitureModel()).getCapacity(Helper.translateMonth(i_month), i_year) * QueryMethods.getCurrentFreeCapacity(em, mff, Helper.translateMonth(i_month), i_year));
                    r.newCell(
                            ((availableCapacity < 0) ? "No Spare" : availableCapacity)
                            + "/" + String.valueOf(mff.findProductionCapacity(pc.getFurnitureModel()).getCapacity(Helper.translateMonth(i_month), i_year))
                    );

                } catch (Exception ex) {
                }
            }

        }

        return dt;

    }

    @Override
    public void addOneMonth() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        c.add(Calendar.MONTH, 1);

        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

    }

    @Override
    public void minusOneMonth() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        c.add(Calendar.MONTH, -1);

        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

    }

    @Override
    public Object getDemandPlanningTable(String MF) throws Exception {
        ManufacturingFacility mff = (ManufacturingFacility) em.createQuery("Select Mf from ManufacturingFacility Mf where Mf.name='" + MF + "'").getSingleResult();

        return (getDemandPlanningTable(mff));
    }

    @Override
    public boolean changeMPP(String mppID, int newValue
    ) {
        String[] temp = mppID.split("\\|");
        MonthlyProductionPlanPK MPPPK = null;

        try {
            MPPPK = new MonthlyProductionPlanPK(Long.valueOf(temp[0]), Long.valueOf(temp[1]), Month.valueOf(temp[2]), Integer.valueOf(temp[3]));
        } catch (Exception ex) {
            return false;

        }
        MonthlyProductionPlan mpp = (MonthlyProductionPlan) em.find(MonthlyProductionPlan.class, MPPPK);
        mpp.setQTY(newValue);

        em.persist(mpp);

        System.out.println(
                "changeMP(): " + mppID + " TO " + newValue);

        return true;
    }

    @Override
    public boolean updateListOfEntities(ArrayList<Object> listOfEntities) throws Exception {
        int delta = 0;
        for (Object o : listOfEntities) {

            if (o instanceof MonthlyProductionPlan) {

                MonthlyProductionPlan _mpp = (MonthlyProductionPlan) o;

                int capacity = _mpp.getManufacturingFacility().findProductionCapacity(_mpp.getFurnitureModel()).getCapacity(_mpp.getMonth(), _mpp.getYear());
                if (_mpp.getQTY() > capacity) {
                    throw new Exception(_mpp.getMonth() + "/" + _mpp.getYear() + " PLANNED CAPCITY @" + _mpp.getQTY() + " exceeds Capacity of" + capacity);
                }

                if (_mpp.getQTY() < 0) {
                    throw new Exception("A number greater than zero is expected !");
                }

                em.merge(o);

                //sigh so tedious to don one thing
                mpp.planWeekMPP(_mpp.getManufacturingFacility().getName(), _mpp.getFurnitureModel().getName(), _mpp.getMonth().value, _mpp.getYear());

            }

            if (o instanceof ProductionCapacity) {
                ProductionCapacity pc = (ProductionCapacity) o;

                if (pc.getQty() < 0) {
                    throw new Exception("Production Capacity for " + pc.getFurnitureModel().getName() + " cannot be negative !");
                }

                em.merge(o);

            }

            if (o instanceof WeeklyProductionPlan) {
                WeeklyProductionPlan wpp = (WeeklyProductionPlan) o;

                if (wpp.getQTY() < 0) {
                    throw new Exception("Production Capacity for " + wpp.getWeekNo() + wpp.getMonthlyProductionPlan().getMonth() + "/" + wpp.getMonthlyProductionPlan().getYear() + " cannot be negative !");
                }

                double totalproduction = 0;
                int capacity = wpp.getMonthlyProductionPlan().getManufacturingFacility().findProductionCapacity(wpp.getMonthlyProductionPlan().getFurnitureModel()).getCapacity(wpp.getMonthlyProductionPlan().getMonth(), wpp.getMonthlyProductionPlan().getYear());

                int maxWeek = Helper.getNumOfWeeks(wpp.getMonthlyProductionPlan().getMonth().value, wpp.getMonthlyProductionPlan().getYear());
                int overflow = 0;
                int temp = ((WeeklyProductionPlan) em.find(WeeklyProductionPlan.class, wpp.getId())).getQTY();
                delta += wpp.getQTY() - temp;
//                if (wpp.getWeekNo() == maxWeek) {
//                    Double w1 = (Helper.getBoundaryWeekDays(wpp.getMonthlyProductionPlan().getMonth(), wpp.getMonthlyProductionPlan().getYear()) / 7.0);
//
//                    Double prorate = w1 * delta;
//                    overflow = delta - prorate.intValue();
//                    delta = prorate.intValue();
//                    MonthlyProductionPlan mpp_next = QueryMethods.getNextMonthlyProductionPlan(em, wpp.getMonthlyProductionPlan());
//                    if (mpp_next != null) {
//                        mpp_next.setQTY(mpp_next.getQTY() + overflow);
//                    }
//                }

                totalproduction = wpp.getMonthlyProductionPlan().getQTY() + delta;

                if (totalproduction > capacity) {
                    throw new Exception("Production Capacity for " + wpp.getWeekNo() + wpp.getMonthlyProductionPlan().getMonth() + "/" + wpp.getMonthlyProductionPlan().getYear() + " exceeded total month capacity of " + capacity);

                }

                em.merge(o);

                System.out.println("updateListOfEntities(): Also updated parents of WPP" + wpp.getId() + " New value=" + wpp.getMonthlyProductionPlan().getQTY() + "+" + delta);
                wpp.getMonthlyProductionPlan().setQTY(Double.valueOf(totalproduction).intValue());
                em.merge(wpp.getMonthlyProductionPlan());

            }

        }
        System.out.println("updateListOfEntities(): Success");
        return true;
    }

    //Not exposed
    private JDataTable<String> getDemandPlanningTable(ManufacturingFacility MF) throws Exception {
        Query q = em.createNamedQuery("MonthlyProductionPlan.FindAllOfMF");
        q.setParameter("mf", MF);
            q.setParameter("m", this.month);
            q.setParameter("y", this.year);
        JDataTable<String> dt = new JDataTable<String>();

        dt.columns.add("Furniture Model");
        dt.columns.add("Data");
        if (q.getResultList().size() == 0) {
            System.err.println("getDemandPlanningTable(): Warning! Not a single production plan ! Running automatic mode");
            mpp.setMF(MF.getName());
            mpp.CreateProductionPlanFromForecast();
            return (getDemandPlanningTable(MF)); //Recursive callback again
        }

        String Cur_FM = "";
        JDataTable.Row demand_row = null;
        JDataTable.Row planned_row = null;
        JDataTable.Row max_CAP = null;
        JDataTable.Row cc_row = null;
        JDataTable.Row pc_row = null;
        JDataTable.Row inventory_row = null;
        JDataTable.Row Working_days = dt.newRow();

        JDataTable.Row fcc_Row = dt.NewRowDefered("percentage.2dp");
        fcc_Row.rowheader = "Remaining Capacity";
        fcc_Row.rowgroup = "Summary";
        fcc_Row.setColorClass("summary");
        fcc_Row.newCell("");
        fcc_Row.newCell("Remaining Capacity");
        Working_days.newCell("");
        Working_days.setColorClass("summary");
        Working_days.newCell("Working Days");

        String colorclass = "normal_even";

        HashMap<String, Long> iinventory = new HashMap<>();

        for (MonthlyProductionPlan pp : (List<MonthlyProductionPlan>) q.getResultList()) {

            if (!Cur_FM.equals(pp.getFurnitureModel().getName())) {
                if (colorclass.equals("normal_odd")) {
                    colorclass = "normal_even";
                } else {
                    colorclass = "normal_odd";
                }

                Cur_FM = pp.getFurnitureModel().getName();

                JDataTable.Row new_Row = dt.newRow();
                new_Row.setColorClass(colorclass);
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell("Sales Forecast"); //second
                demand_row = new_Row;

                new_Row = dt.newRow();
                new_Row.setColorClass(colorclass);
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell("Production Plan"); //second
                planned_row = new_Row;

                new_Row = dt.newRow();
                new_Row.setColorClass(colorclass);
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell("Inventory"); //second
                inventory_row = new_Row;

                new_Row = dt.newRow();
                new_Row.setColorClass(colorclass);
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell("Maximum Capacity"); //second
                max_CAP = new_Row;

                new_Row = dt.newRow("percentage.2dp");
                new_Row.setColorClass(colorclass);
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell("Demand Required Capacity"); //second
                pc_row = new_Row;

                new_Row = dt.newRow("percentage.2dp");
                new_Row.setColorClass(colorclass);
                new_Row.rowgroup = Cur_FM;
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell("Used Capacity"); //second
                cc_row = new_Row;

            }

            if (!dt.columns.contains(pp.getMonth().toString() + "/" + pp.getYear())) {
                dt.columns.add(pp.getMonth().toString() + "/" + pp.getYear());
                Double MonthCapacity = QueryMethods.getCurrentFreeCapacity(em, pp.getManufacturingFacility(), pp.getMonth(), pp.getYear());
                if (MonthCapacity > 0.75) {
                    fcc_Row.newCell(String.valueOf(MonthCapacity)).setColorClass("LIGHT_WORKLOAD");
                } else if (MonthCapacity > 0.5) {
                    fcc_Row.newCell(String.valueOf(MonthCapacity)).setColorClass("MEDIUM_WORKLOAD");
                } else {
                    fcc_Row.newCell(String.valueOf(MonthCapacity)).setColorClass("HEAVY_WORKLOAD");
                }

                Working_days.newCell(String.valueOf(pp.getNumWorkDays()));
            }

            //Start off with Demand Requirement.
            long demand = QueryMethods.getTotalDemand(em, pp, MF);
            double used_capacity = (QueryMethods.getTotalDemand(em, pp, MF) + 0.0) / pp.getManufacturingFacility().findProductionCapacity(pp.getFurnitureModel()).getCapacity(pp.getMonth(), pp.getYear());
            double planned_capacity = (pp.getQTY() + 0.0) / pp.getManufacturingFacility().findProductionCapacity(pp.getFurnitureModel()).getCapacity(pp.getMonth(), pp.getYear());
            demand_row.newCell(String.valueOf(demand));
            Cell p = pc_row.newCell(String.valueOf((used_capacity))); //demand requested
            Cell w = cc_row.newCell((String.valueOf(planned_capacity))); //Planned Capacity
            Cell pr = planned_row.newBindedCell(pp.getQTY().toString(), "QTY").setBinded_entity(pp);
            long inventory = 0;
            if (!pp.isLocked() && !QueryMethods.isOrderedMaterial(em, MF, pp.getMonth(), pp.getYear())) {
                inventory = (iinventory.get(Cur_FM) == null ? 0 : iinventory.get(Cur_FM)) + pp.getQTY() - QueryMethods.getTotalDemand(em, pp, MF);
            }
            if (inventory >= 0) {
                inventory_row.newCell(String.valueOf(inventory));
            } else {
                inventory_row.newCell(String.valueOf(inventory)).setColorClass("ERROR");

            }
            iinventory.put(Cur_FM, inventory);

            if (pp.isLocked() || QueryMethods.isOrderedMaterial(em, MF, pp.getMonth(), pp.getYear())) {
                pr.setIsEditable(false); //Editable Cell

            } else {
                pr.setIsEditable(true); //Editable Cell

            }

            if (planned_capacity < 0.5) {
                w.setColorClass("LIGHT_WORKLOAD");
            } else if (planned_capacity < 0.75) {
                w.setColorClass("MEDIUM_WORKLOAD");
            } else {
                w.setColorClass("HEAVY_WORKLOAD");
            }

            max_CAP.newCell(String.valueOf(pp.getManufacturingFacility().findProductionCapacity(pp.getFurnitureModel()).getCapacity(pp.getMonth(), pp.getYear())));

        }

        dt.Internalrows.add(fcc_Row);
        System.out.println("getDemandPlanningTable(): SUCCESS ! @" + MF.getName());
        return dt;
    }

    public void persist(Object object) {
        em.persist(object);
    }

    //Material Level drill
    @Override
    public Object getWeeklyMRPTable(String period, String MFNAME) throws Exception {

        ManufacturingFacility mff = (ManufacturingFacility) em.createQuery("Select Mf from ManufacturingFacility Mf where Mf.name='" + MFNAME + "'").getSingleResult();
        Month requestedMonth = Month.valueOf(period.split("/")[0]);
        int requestedYear = Integer.valueOf(period.split("/")[1]);

//        if (em.createQuery("SELECT MPP FROM MonthlyProductionPlan MPP where MPP.month=:m and MPP.year=:y and MPP.locked=TRUE").setParameter("m", requestedMonth).setParameter("y", requestedYear).getResultList().size() > 0) {
//            throw new Exception("month is locked !");
//        }
        Query L = em.createNamedQuery("weeklyMRPRecord.findwMRPatMFmonth");
        L.setParameter("mf", mff);
        L.setParameter("m", requestedMonth);
        L.setParameter("y", requestedYear);

        JDataTable<String> jdt = new JDataTable<String>();
        JDataTable.Row SPACER = null;
        JDataTable.Row MPS = null;

        JDataTable.Row ScheduledReceipt = null;
        JDataTable.Row PlannedReceipt = null;
        JDataTable.Row OnHand = null;
        JDataTable.Row PlannedOrders = null;
        //JDataTable.Row OrderNow = null;
        //OrderNow = jdt.NewRowDefered().setColorClass("summary");
        //OrderNow.newCell("");

        String cur_material = "";

        jdt.columns.add("Data Type");

        for (int i = 1; i <= Helper.getNumOfWeeks(requestedMonth.value, requestedYear); i++) {

            jdt.columns.add(String.valueOf(i));

//            if (QueryMethods.isMaterialWeekPermanentLocked(em, mff, requestedMonth, requestedYear, i)) {
//                OrderNow.newCell("Material Already Scheduled. No reversal allowed !");
//            } else if (QueryMethods.isMaterialWeekLocked(em, mff, requestedMonth, requestedYear, i)) {
//                OrderNow.newCell("Unschedule Week").setCommand("UNORDER_MATERIAL").setIdentifier(requestedYear + "_" + requestedMonth.value + "_" + i);
//            } else {
//                OrderNow.newCell("Schedule Week").setCommand("ORDER_MATERIAL").setIdentifier(requestedYear + "_" + requestedMonth.value + "_" + i);
//
//            }
        }
        String last_cell = "normal_odd";
        for (WeeklyMRPRecord wMRP : (List<WeeklyMRPRecord>) L.getResultList()) {

            if (!cur_material.equals(wMRP.getMaterial().getName())) {
                cur_material = wMRP.getMaterial().getName();

                if (last_cell == "normal_odd") {
                    last_cell = "normal_even";
                } else {
                    last_cell = "normal_odd";
                }

                SPACER = jdt.newRow().setColorClass(last_cell);
                SPACER.newCell(cur_material);
                SPACER.newCell(QueryMethods.getSupplierByMfAndM(em, mff, wMRP.getMaterial()).getName());
                SPACER.setColorClass("summary");
                MPS = jdt.newRow().setColorClass(last_cell);
                MPS.newCell("Gross Requirements");
                ScheduledReceipt = jdt.newRow().setColorClass(last_cell);

                ScheduledReceipt.newCell("Scheduled Receipt(X" + wMRP.getLotSize().toString() + ")");
                PlannedReceipt = jdt.newRow().setColorClass(last_cell);
                PlannedReceipt.newCell("Planned Receipt(X" + wMRP.getLotSize().toString() + ")");
                OnHand = jdt.newRow().setColorClass(last_cell);
                OnHand.newCell("On Hand(Pieces)");
                PlannedOrders = jdt.newRow().setColorClass(last_cell);
                PlannedOrders.newCell("Planned Orders(Lead Time=" + wMRP.getLeadTime() + " days)");

            }
            if (wMRP.getQtyReq() != null) {
                MPS.newCell(wMRP.getQtyReq().toString());
            } else {
                MPS.newCell("0");

            }

            if (wMRP.getOrderAMT() == 0) {
                ScheduledReceipt.newCell("0");
                PlannedReceipt.newCell("0");
            } else if (wMRP.getPurchaseOrderDetail() != null && wMRP.getPurchaseOrderDetail().getPurchaseOrder() != null) { //simple fix
                ScheduledReceipt.newCell(wMRP.getOrderAMT().toString());
                PlannedReceipt.newCell("0");
            } else {
                ScheduledReceipt.newCell("0");
                PlannedReceipt.newCell(wMRP.getOrderAMT().toString());
            }

            OnHand.newCell(String.valueOf(wMRP.getOnHand()));

            PlannedOrders.newCell(String.valueOf(wMRP.getPlannedOrder()));

        }

//        jdt.Internalrows.add(OrderNow);
        return (jdt);
    }

    //Weekly Production Plan
    @Override
    public Object getWeeklyPlans(String period, String MFNAME) throws Exception {
        ManufacturingFacility mff = (ManufacturingFacility) em.createQuery("Select Mf from ManufacturingFacility Mf where Mf.name='" + MFNAME + "'").getSingleResult();
        try {
            mpp.setMF(mff.getName());
        } catch (Exception ex) {

        }
        Month requestedMonth = Month.valueOf(period.split("/")[0]);
        int requestedYear = Integer.valueOf(period.split("/")[1]);

        //Check if is locked
//        if (em.createQuery("SELECT MPP FROM MonthlyProductionPlan MPP where MPP.month=:m and MPP.year=:y and MPP.locked=TRUE").setParameter("m", requestedMonth).setParameter("y", requestedYear).getResultList().size() > 0) {
//            throw new Exception("month is locked !");
//        }
        JDataTable<String> jdt = new JDataTable<String>();
        jdt.columns.add("DataType");
        Query q = em.createNamedQuery("WeeklyProductionPlan.getForMF");
        q.setParameter("m", requestedMonth.value);
        q.setParameter("y", requestedYear);
        q.setParameter("MF", mff);
        if (q.getResultList().size() == 0) {
            throw new Exception("getWeeklyPlans(): No Weekly Production Plan !");
        }

        JDataTable.Row StartDayOfWeek = jdt.newRow();
        JDataTable.Row EndDayOfWeek = jdt.newRow();
        StartDayOfWeek.setColorClass("summary");
        StartDayOfWeek.newCell("Start Day");
        EndDayOfWeek.newCell("End Day");
        EndDayOfWeek.setColorClass("summary");

        JDataTable.Row PlannedWeekProduction = null;
        HashMap<Integer, Boolean> alluncommited = new HashMap<>();
        HashMap<Integer, Boolean> lockcolumn = new HashMap<>();

        String CFM = "";

        //Columns Construction
        int MaxWeek = Helper.getNumOfWeeks(requestedMonth.value, requestedYear);
        for (int i = 1; i <= MaxWeek; i++) {
            jdt.columns.add("Week " + i);
            Calendar start = Helper.getStartDateOfWeek(requestedMonth.value, requestedYear, i);
            StartDayOfWeek.setColorClass("summary");
            EndDayOfWeek.setColorClass("summary");

            StartDayOfWeek.newCell(start.get(Calendar.DAY_OF_MONTH) + "/" + (start.get(Calendar.MONTH) + 1));
            start.add(Calendar.DAY_OF_MONTH, 6);
            EndDayOfWeek.newCell(start.get(Calendar.DAY_OF_MONTH) + "/" + (start.get(Calendar.MONTH) + 1));

        }

        HashMap<Material, JDataTable.Row> materialRows = new HashMap<Material, JDataTable.Row>();
        //Iterate WPP
        HashMap<Plant, JDataTable.Row> mf_rows = null;
        HashMap<Plant, Long> mssrLists = null;

        String last_class = "normal_odd";
        for (WeeklyProductionPlan wpp : (List<WeeklyProductionPlan>) q.getResultList()) {
            if (!CFM.equals(wpp.getMonthlyProductionPlan().getFurnitureModel().getName())) {
                CFM = wpp.getMonthlyProductionPlan().getFurnitureModel().getName();
                if (last_class == "normal_odd") {
                    last_class = "normal_even";
                } else {
                    last_class = "normal_odd";
                }

                //Show breakdown to Country Offices
                mssrLists = QueryMethods.traceWPPToPlant(em, wpp);
                mf_rows = new HashMap<>();
                JDataTable.Row space = jdt.newRow(); //spacer
                space.setColorClass("summary");
                space.newCell("");
                for (Plant p : mssrLists.keySet()) {
                    JDataTable.Row r = jdt.newRow();
                    r.newCell(wpp.getMonthlyProductionPlan().getFurnitureModel().getName() + " <br/> To: " + p.getName());
                    r.setColorClass(last_class);
                    mf_rows.put(p, r);
                }

                PlannedWeekProduction = jdt.newRow().setColorClass(last_class);
                PlannedWeekProduction.newCell(CFM + " <br/> Total Week Requirement:" + QueryMethods.getTotalDemand(em, wpp.getMonthlyProductionPlan(), mff));

//                ActionRow = jdt.newRow().setColorClass(last_class);;
//                ActionRow.newCell("Commit to Production Order");
            }
//            Cell d = null;

            boolean hasCommittedMaterial = QueryMethods.isOrderedMaterial(em, mff, wpp.getMonthlyProductionPlan().getMonth(), wpp.getMonthlyProductionPlan().getYear(), wpp.getWeekNo());

            Cell c = null;
            //Individual Cells
            if (wpp.isLocked()) {
                jdt.keyvaluepair.put("button", "");

//                d = ActionRow.newCell("Locked");
                lockcolumn.put(wpp.getWeekNo(), true);
                c = PlannedWeekProduction.newBindedCell(String.valueOf(wpp.getQTY()), "QTY").setBinded_entity(wpp);
                if (wpp.getProductionOrder() == null) {
                    c = PlannedWeekProduction.newCell("0");
                } else {
                    c = PlannedWeekProduction.newBindedCell(String.valueOf(wpp.getQTY()), "QTY").setBinded_entity(wpp);
                }
            } else if (hasCommittedMaterial) {
                jdt.keyvaluepair.put("button", "uncommit");
//                d = ActionRow.newCell("Remove MRP First");
                if (wpp.getProductionOrder() == null) {
                    c = PlannedWeekProduction.newCell("0");
                } else {
                    c = PlannedWeekProduction.newBindedCell(String.valueOf(wpp.getQTY()), "QTY").setBinded_entity(wpp);
                }
            } else if (wpp.getProductionOrder() == null) {
                if (jdt.keyvaluepair.get("button") == null) {
                    jdt.keyvaluepair.put("button", "commit");
                }

//                alluncommited.put(ActionRow.rowdata.size(), true);
//                d = ActionRow.newCell("Commit");
//                d.setCommand("COMMIT_WPP");
//                d.setIdentifier(wpp.getId().toString());
                c = PlannedWeekProduction.newBindedCell(String.valueOf(wpp.getQTY()), "QTY").setBinded_entity(wpp);
                c.setIsEditable(true);
            } else {
                jdt.keyvaluepair.put("button", "commit");

//                d = ActionRow.newCell("Uncommit");
//                d.setCommand("UNCOMMIT_WPP");
//                d.setIdentifier(wpp.getId().toString());
                c = PlannedWeekProduction.newBindedCell(String.valueOf(wpp.getQTY()), "QTY").setBinded_entity(wpp);
            }

            //Populate distribution Table
            HashMap<Plant, Long> dist = QueryMethods.traceWPPToPlant(em, wpp);
            for (Plant p : dist.keySet()) {
                mf_rows.get(p).newCell(dist.get(p).toString());
            }

        }

//        JDataTable.Row SPACER = jdt.newRow().setColorClass("summary");
//        SPACER.newCell("Materials Required For Commited WPP");
        for (int i = 1; i <= jdt.columns.size() - 1; i++) {

            //Materials side---------------------
            HashMap<Material, Long> table = mpp.getMaterialsNeededForCommited(i, requestedYear, requestedMonth.value);
            for (Material m : (Set<Material>) table.keySet()) {
                if (materialRows.get(m) == null) {
                    if (last_class == "normal_odd") {
                        last_class = "normal_even";
                    } else {
                        last_class = "normal_odd";
                    }

                    JDataTable.Row r = jdt.newRow().setColorClass(last_class);
                    r.newCell(m.getName() + "<br/>" + QueryMethods.getSupplierByMfAndM(em, mff, m).getName());
                    materialRows.put(m, r);
                }

                double lotsize = Math.ceil(Double.valueOf(table.get(m)) / mpp.getLotSize(m));

                for (int j = materialRows.get(m).rowdata.size(); j < i; j++) {
                    materialRows.get(m).newCell("");
                }
                materialRows.get(m).newCell(table.get(m).toString() + "<br/>[" + lotsize + " lots X" + mpp.getLotSize(m) + "]");

            }

        }
//        CommitWeek = jdt.newRow();
//        CommitWeek.newCell("Commit all Materials");
//        JDataTable.Row commitMRP = jdt.newRow();
//        commitMRP.newCell("Generate Weekly MRP");
//        commitMRP.setColorClass("summary");
//        CommitWeek.setColorClass("summary");

        //all cells
        for (int i = 1; i <= jdt.columns.size() - 1; i++) {

            boolean hasCommittedMaterial = QueryMethods.isOrderedMaterial(em, mff, requestedMonth, requestedYear, i);
            if (lockcolumn.get(i) != null) {
//                CommitWeek.newCell("Locked");
//                commitMRP.newCell("LOCKED");
//            } else if (QueryMethods.isMaterialWeekLocked(em, mff, requestedMonth, requestedYear, i)) {
//                commitMRP.newCell("Undo Material Schedule First !");
//                CommitWeek.newCell("Undo Material Schedule First !");
            } else if (hasCommittedMaterial) {
//                commitMRP.newCell("Remove Weekly MRP Record").setCommand("UNCOMMIT_WEEK_WPP").setIdentifier(i + "_" + requestedMonth + "_" + requestedYear);
////                CommitWeek.newCell("Remove Weekly MRP Record First");
            } else {
//                commitMRP.newCell("Confirm MRP Record for Week").setCommand("COMMIT_WEEK_WPP").setIdentifier(i + "_" + requestedMonth + "_" + requestedYear);
                if (alluncommited.get(i) == null) {
//                    CommitWeek.newCell("Uncommit All").setCommand("Uncommit_All_Material").setIdentifier(i + "_" + requestedMonth.value + "_" + requestedYear);
                } else {
//                    CommitWeek.newCell("Commit All").setCommand("Commit_All_Material").setIdentifier(i + "_" + requestedMonth.value + "_" + requestedYear);
                }
            }

        }

        //only enable commencement for next month after lock
        int currenttime = (ManageProductionPlanTimerBean.cdate.getCalendar().get(Calendar.MONTH) + 1) + ManageProductionPlanTimerBean.cdate.getCalendar().get(Calendar.YEAR) * 12;
        if ((1 + requestedMonth.value) + requestedYear * 12 - currenttime != ManageProductionPlanning.FORWARDLOCK + 1) {
            jdt.keyvaluepair.put("button", "");
        }

        System.out.println("getWeeklyPlans() Requesting for wpp for" + requestedMonth + " /" + requestedYear);
        return jdt;
    }

}
