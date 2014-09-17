/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.StaticClasses.Helper.DataTable;
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

    public DataTable<String> getDemandPlanningTable(ManufacturingFacility MF) {
      Query q=em.createNamedQuery("MonthlyProductionPlan.FindAllOfMF");
      q.setParameter("mf", MF);
      for (MonthlyProductionPlan pp: (List<MonthlyProductionPlan>)q.getResultList()){
          
      }
      return null;
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
