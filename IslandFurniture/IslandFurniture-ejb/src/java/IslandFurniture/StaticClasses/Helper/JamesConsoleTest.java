/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanning;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.omg.CORBA.PRIVATE_MEMBER;

/**
 *
 * @author James
 * Will be deleted
 */
public class JamesConsoleTest {
    @EJB
        private static ManageProductionPlanning manager;
    @PersistenceContext(unitName = "IslandFurniture")
    private static EntityManager em;
        
    public static void main(String[] args){
        create_test_data();
    }
    
    public static void create_test_data()
    {
      
        Country cn = new Country();
        cn.setName("SINGAPORE");
        ManufacturingFacility mf=new ManufacturingFacility();
        mf.setCountry(cn);
        FurnitureModel fm=new FurnitureModel();
        fm.setName("FURNITURE");
        ProductionCapacity pc = new ProductionCapacity();
        pc.setStock(fm);
        pc.setQty(100);
        em.persist(cn);
        em.persist(pc);
        em.persist(fm);
        em.persist(mf);
        
    }
}
