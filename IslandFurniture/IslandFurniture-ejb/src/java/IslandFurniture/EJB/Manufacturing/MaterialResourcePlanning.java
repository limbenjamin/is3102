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
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.StaticClasses.Helper.QueryMethods;
import IslandFurnitures.DataStructures.JDataTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class MaterialResourcePlanning implements MaterialResourcePlanningView {

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
    public JDataTable<String> getDemandPlanningTable(String MF) {
        ManufacturingFacility mff = (ManufacturingFacility) em.createQuery("Select Mf from ManufacturingFacility Mf where Mf.name='" + MF + "'").getSingleResult();
        return (getDemandPlanningTable(mff));
    }

    public JDataTable<String> getDemandPlanningTable(ManufacturingFacility MF) {
        Query q = em.createNamedQuery("MonthlyProductionPlan.FindAllOfMF");
        q.setParameter("mf", MF);
        JDataTable<String> dt = new JDataTable<String>();

        String Cur_FM = "";
        ArrayList<String> demand_row = null;
        ArrayList<String> planned_row = null;
        ArrayList<String> cc_row = null;
//Finally Summarize MF Capacity

        JDataTable.Row<String> fcc_Row = dt.NewRowDefered("percentage.2dp");
        fcc_Row.rowheader = "Remaining Capacity";
        fcc_Row.rowgroup = "Summary";

        for (MonthlyProductionPlan pp : (List<MonthlyProductionPlan>) q.getResultList()) {

            if (!Cur_FM.equals(pp.getFurnitureModel().getName())) {
                Cur_FM = pp.getFurnitureModel().getName();

                JDataTable.Row<String> new_Row = dt.NewRow();
                new_Row.rowheader = "Required Quantity";
                new_Row.rowgroup = Cur_FM;
                demand_row = new_Row.rowdata;

                new_Row = dt.NewRow();
                new_Row.rowheader = "Planned Quantity";
                new_Row.rowgroup = Cur_FM;
                planned_row = new_Row.rowdata;

                new_Row = dt.NewRow("percentage.2dp");
                new_Row.rowheader = "Used Capacity";
                new_Row.rowgroup = Cur_FM;
                cc_row = new_Row.rowdata;
            }

            if (!dt.columns.ColumnsHeader.contains(pp.getMonth().toString() + "/" + pp.getYear())) {
                dt.columns.ColumnsHeader.add(pp.getMonth().toString() + "/" + pp.getYear());
                fcc_Row.rowdata.add(String.valueOf(pp.getManufacturingFacility().getCurrentFreeCapacity(em, pp.getMonth(), pp.getYear())));
            }

            //Start off with Demand Requirement.
            demand_row.add(String.valueOf(QueryMethods.getTotalDemand(em, pp, MF)));
            planned_row.add(String.valueOf(pp.getQTY()));
            double used_capacity = (QueryMethods.getTotalDemand(em, pp, MF) + 0.0) / pp.getManufacturingFacility().findProductionCapacity(pp.getFurnitureModel()).getQty();
            cc_row.add(String.valueOf(used_capacity));

        }

        dt.Internalrows.add(fcc_Row);

        return dt;
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
