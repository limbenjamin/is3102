/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.BOM;
import IslandFurniture.EJB.Entities.BOMDetail;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.Material;
import IslandFurniture.StaticClasses.Helper.QueryMethods;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findMaterialByName;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
            material.setName(name);
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
            material = findMaterialByName(em, name);
            System.out.println(material.getName());
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
    public boolean addFurnitureModel(String name) {
        FurnitureModel fm;
        try {
            System.out.println("StockManager:addFurnitureModel()");
            fm = new FurnitureModel(name);
            em.persist(fm);
            return true;
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public List<FurnitureModel> displayFurnitureList() {
        List furnitureList;
        try {
            System.out.println("StockManager:displayFurnitureList()");
            furnitureList = em.createNamedQuery("getAllFurnitureModels", FurnitureModel.class).getResultList();
            return furnitureList;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    // Something wrong here. Need debug. 
    public void editFurnitureModel(String furnitureName) {
        FurnitureModel fm;
        try {
            System.out.println("StockManager:editFurnitureModel()");
            fm = (FurnitureModel)em.createNamedQuery("findFurnitureByName").setParameter("name", furnitureName).getSingleResult();
            fm.setName("something else");
        } catch(NoResultException ex) {
            System.err.println("Can't find name");
        }
    }
    public void addToBOM(Long materialID, Long furnitureID) {
        Material material;
        FurnitureModel furnitureModel;
        BOM bom;
        BOMDetail BOMdetail ;
        try {
            System.out.println("StockManager:addToBOM");
            material = em.find(Material.class, materialID);
            furnitureModel = em.find(FurnitureModel.class, furnitureID);
            if(furnitureModel.getBom() == null) {
                System.out.println("BOM is null");
                bom = new BOM();
                furnitureModel.setBom(bom);
                em.persist(furnitureModel);
            } else {
                bom = furnitureModel.getBom();
            }
            
            BOMdetail = new BOMDetail();
            BOMdetail.setMaterial(material);
            BOMdetail.setBom(bom);
            bom.getBomDetails().add(BOMdetail);
            em.flush();
           
        } catch(NoResultException NRE) {
            System.err.println("Can't find name");
        }
    }
}
