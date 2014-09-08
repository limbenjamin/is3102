/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Store;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadPlantBean implements LoadPlantBeanLocal {
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public Store addStore(String storeCode, String storeName) {
        Store store = new Store();
        store.setCode(storeCode);
        store.setName(storeName);
        em.persist(store);
        
        return store;
    }
    
//    @Override
//    public ManufacturingFacility addManufacturingFacility(String mfCode, String mfName) {
//        ManufacturingFacility mf = new ManufacturingFacility();
//        mf.setCode(mfCode);
//        mf.setName(mfName);
//        em.persist(mf);
//        
//        return mf;
//    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        em.persist(object);
    }
}
