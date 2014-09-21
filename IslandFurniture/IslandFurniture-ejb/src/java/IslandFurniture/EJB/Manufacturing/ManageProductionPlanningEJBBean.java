/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.MonthlyProductionPlanPK;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurniture.StaticClasses.Helper.QueryMethods;
import IslandFurnitures.DataStructures.JDataTable;
import IslandFurnitures.DataStructures.JDataTable.Cell;
import ch.qos.cal10n.util.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Stateless
public class ManageProductionPlanningEJBBean implements ManageProductionPlanningEJBBeanInterface {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    public HashMap<String, String>
            getAuthorizedMF(String AUTH) {

        Query q = em.createNamedQuery("getAllMFs");
        HashMap<String, String> temp = new HashMap<String, String>();
        for (ManufacturingFacility MF : (List<ManufacturingFacility>) q.getResultList()) {
            temp.put(MF.getName(), MF.getCountry().getName() + "/" + MF.getName());
        }

        return (temp);

    }

    @Override
    public Object getCapacityList(String MF) {
        ManufacturingFacility mff = (ManufacturingFacility) em.createQuery("Select Mf from ManufacturingFacility Mf where Mf.name='" + MF + "'").getSingleResult();
        Query q = em.createNamedQuery("ProductionCapacity.findPCbyMF");
        q.setParameter("MFNAME", MF);
        JDataTable<String> dt = new JDataTable<String>();
        dt.Title = "Capacity Management";

        dt.columns.add("Furniture Model");
        dt.columns.add("Maximum Daily Production");

        for (ProductionCapacity pc : (List<ProductionCapacity>) q.getResultList()) {

            JDataTable.Row r = dt.newBindedRow(pc);
            r.newCell(pc.getFurnitureModel().getName()).setIsEditable(true);
            r.newBindedCell(pc.getQty().toString(), "Qty");

        }

        return dt;

    }

    @Override
    public Object getDemandPlanningTable(String MF) {
        ManufacturingFacility mff = (ManufacturingFacility) em.createQuery("Select Mf from ManufacturingFacility Mf where Mf.name='" + MF + "'").getSingleResult();
        return (getDemandPlanningTable(mff));
    }

    @Override
    public boolean changeMPP(String mppID, int newValue) {
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
        System.out.println("changeMP(): " + mppID + " TO " + newValue);

        return true;
    }

    @Override
    public boolean updateListOfEntities(ArrayList<Object> listOfEntities) throws Exception {
        for (Object o : listOfEntities) {

            MonthlyProductionPlan mpp = (MonthlyProductionPlan) o;

            int capacity = mpp.getManufacturingFacility().findProductionCapacity(mpp.getFurnitureModel()).getCapacity(mpp.getMonth(), mpp.getYear());
            if (mpp.getQTY() > capacity) {
                throw new Exception(mpp.getMonth() + "/" + mpp.getYear() + " PLANNED CAPCITY @" + mpp.getQTY() + " exceeds Capacity of" + capacity);
            }

            if (mpp.getQTY() < 0) {
                throw new Exception("A number greater than zero is expected !");
            }

            em.merge(o);

        }
        System.out.println("updateListOfEntities(): Success");
        return true;
    }

    public JDataTable<String> getDemandPlanningTable(ManufacturingFacility MF) {
        Query q = em.createNamedQuery("MonthlyProductionPlan.FindAllOfMF");
        q.setParameter("mf", MF);
        JDataTable<String> dt = new JDataTable<String>();
        dt.columns.add("Furniture Model");
        dt.columns.add("Data");
        String Cur_FM = "";
        JDataTable.Row demand_row = null;
        JDataTable.Row planned_row = null;
        JDataTable.Row max_CAP = null;
        JDataTable.Row cc_row = null;
        JDataTable.Row pc_row = null;

//Finally Summarize MF Capacity
        JDataTable.Row fcc_Row = dt.NewRowDefered("percentage.2dp");
        fcc_Row.rowheader = "Remaining Capacity";
        fcc_Row.rowgroup = "Summary";
        fcc_Row.setColorClass("summary");
        fcc_Row.newCell("");
        fcc_Row.newCell("Remaining Capacity");
        

        
        String colorclass = "normal_even";

        for (MonthlyProductionPlan pp : (List<MonthlyProductionPlan>) q.getResultList()) {

            if (!Cur_FM.equals(pp.getFurnitureModel().getName())) {
                if (colorclass.equals("normal_odd")) {
                    colorclass = "normal_even";
                } else {
                    colorclass = "normal_odd";
                }

                Cur_FM = pp.getFurnitureModel().getName();

                JDataTable.Row new_Row = dt.newRow();
                new_Row.rowheader = "Required Quantity";
                new_Row.rowgroup = Cur_FM;
                new_Row.setColorClass(colorclass);
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell(new_Row.rowheader); //second
                demand_row = new_Row;

                new_Row = dt.newRow();
                new_Row.rowheader = "Planned Quantity";
                new_Row.setColorClass(colorclass);
                new_Row.rowgroup = Cur_FM;
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell(new_Row.rowheader); //second
                planned_row = new_Row;

                new_Row = dt.newRow();
                new_Row.rowheader = "Month Max Capacity";
                new_Row.setColorClass(colorclass);
                new_Row.rowgroup = Cur_FM;
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell(new_Row.rowheader); //second
                max_CAP = new_Row;

                new_Row = dt.newRow("percentage.2dp");
                new_Row.rowheader = "Required Capacity";
                new_Row.setColorClass(colorclass);
                new_Row.rowgroup = Cur_FM;
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell(new_Row.rowheader); //second
                cc_row = new_Row;

                new_Row = dt.newRow("percentage.2dp");
                new_Row.rowheader = "Consumed Capacity";
                new_Row.setColorClass(colorclass);
                new_Row.rowgroup = Cur_FM;
                new_Row.newCell(Cur_FM); //first column
                new_Row.newCell(new_Row.rowheader); //second
                pc_row = new_Row;
            }

            if (!dt.columns.contains(pp.getMonth().toString() + "/" + pp.getYear())) {
                dt.columns.add(pp.getMonth().toString() + "/" + pp.getYear());
                fcc_Row.newCell(String.valueOf(QueryMethods.getCurrentFreeCapacity(em, pp.getManufacturingFacility(), pp.getMonth(), pp.getYear())));
            }

            //Start off with Demand Requirement.
            demand_row.newCell(String.valueOf(QueryMethods.getTotalDemand(em, pp, MF)));

            double used_capacity = (QueryMethods.getTotalDemand(em, pp, MF) + 0.0) / pp.getManufacturingFacility().findProductionCapacity(pp.getFurnitureModel()).getCapacity(pp.getMonth(), pp.getYear());
            cc_row.newCell((String.valueOf(used_capacity)));

            if (pp.isLocked()) {
                planned_row.newBindedCell(pp.getQTY().toString(), "QTY").setBinded_entity(pp).setIsEditable(false); //Editable Cell
                pc_row.newCell(String.valueOf(((pp.getQTY() + 0.0) / pp.getManufacturingFacility().findProductionCapacity(pp.getFurnitureModel()).getCapacity(pp.getMonth(), pp.getYear()))));

            } else {
                planned_row.newBindedCell(pp.getQTY().toString(), "QTY").setBinded_entity(pp).setIsEditable(true); //Editable Cell
                pc_row.newCell(String.valueOf(((pp.getQTY() + 0.0) / pp.getManufacturingFacility().findProductionCapacity(pp.getFurnitureModel()).getCapacity(pp.getMonth(), pp.getYear()))));

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
}
