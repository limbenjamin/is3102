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
import IslandFurniture.TEST.JamesTestDataBeanRemote;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author James
 */
@Stateless
public class JamesTestDataBean implements JamesTestDataBeanRemote{
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    public void createtestdata(){
                Country cn = new Country();
        cn.setName("SINGAPORE");
        ManufacturingFacility mf = new ManufacturingFacility();
        mf.setCountry(cn);
        FurnitureModel fm = new FurnitureModel();
        fm.setName("FURNITURE");
        ProductionCapacity pc = new ProductionCapacity();
        pc.setStock(fm);
        pc.setQty(100);
        
        persist(pc);
        persist(mf);
        persist(fm);
        persist(cn);
        
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
