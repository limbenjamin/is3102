/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.Material;
import java.util.List;
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
    
    public boolean addMaterial(String name, Double weight) {
        Material material;
        try{
            System.out.println("StockManager: 1");
            material = new Material();
            material.setMaterialName(name);
            material.setMaterialWeight(weight);
            em.persist(material);
            return true;
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public List displayMaterialList() {
        Material material;
        List materialList;
        try {
            materialList = em.createNamedQuery("Material.getMaterialList", Material.class).getResultList();
            return materialList;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public boolean updateMaterial(Long id, String name, Double weight) {
        Material material;
        try {
            material = em.find(Material.class, id);
            if(name != null) {
                System.out.println("Changing name to " + name);
                material.setMaterialName(name);
            }
            if(weight != null) {
                System.out.println("Changing weight to " + weight);
                material.setMaterialWeight(weight);
            }
            return true;
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
