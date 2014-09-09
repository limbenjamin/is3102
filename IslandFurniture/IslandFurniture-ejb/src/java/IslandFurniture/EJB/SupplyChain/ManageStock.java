/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774
 */
@Stateless
public class ManageStock implements ManageStockRemote, ManageStockLocal {
    @PersistenceContext
    private EntityManager em;
    
    public ManageStock() {
    }
    
    @Override
    public boolean addMaterial(String name) {
        Material material;
        System.out.println("inside here");
        try {
            material = new Material(name);
            System.out.println(name);
            em.persist(material);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        
    }    
}
