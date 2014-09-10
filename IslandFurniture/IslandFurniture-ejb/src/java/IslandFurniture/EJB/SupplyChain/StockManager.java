/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.Material;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774
 */
@Stateful
public class StockManager implements StockManagerLocal {

    @PersistenceContext
    EntityManager em;
    
    public boolean addMaterial() {
        Material material;
        try{
            System.out.println("StockManager: 1");
            material = new Material();
            System.out.println("StockManager: 2");
            return true;
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
