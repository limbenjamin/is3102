/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
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
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public MonthlyProductionPlan CreateProductionPlan(long Month, long Year,FurnitureModel furnitureModel) {
        return null;
    }
}
