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
import IslandFurniture.EJB.Entities.RetailItem;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findFurnitureByName;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findMaterialByName;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findRetailItemByName;
import java.util.ArrayList;
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
            System.out.println("StockManager.addMaterial()");
            material = findMaterialByName(em, name);
            if(material != null) 
                material.setHidden(false);
            else {
                material = new Material();
                material.setName(name);
                material.setMaterialWeight(weight);
            }
            em.persist(material);
            return true;
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public List displayMaterialList() {
        Material material;
        List<Material> materialList;
        try {
            materialList = em.createNamedQuery("Material.getMaterialList", Material.class).getResultList();
            for(int i=0; i<materialList.size(); i++) {
                if(materialList.get(i).isHidden())
                    materialList.remove(i);
            }
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
    public void deleteMaterial(String materialName) {
        Material material;
        try{
            material = findMaterialByName(em, materialName);
            material.setHidden(true);
        } catch(Exception ex) {
            ex.printStackTrace();
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
    
    public void editFurnitureModel(String furnitureName, String price) {
        FurnitureModel fm;
        try {
            System.out.println("StockManager:editFurnitureModel()");
            fm = findFurnitureByName(em, furnitureName);
            System.out.println("Updating furniture information of " + fm.getName());
            if(price != null && fm.getPrice() != null) {
                System.out.println("Changing price from " + fm.getPrice().toString() + " to " + price);
                fm.setPrice(Double.parseDouble(price));
            } else if(price != null) {
                System.out.println("Changing price from null to " + price);
                fm.setPrice(Double.parseDouble(price));
            } else ;
        } catch(NoResultException ex) {
            System.err.println("Can't find name");
        }
    }
    public void addFurnitureColour(String furnitureName, String colour) {
        FurnitureModel fm;
        List colourList;
        try {
            System.out.println("StockManager:addFurnitureColour()");
            fm = findFurnitureByName(em, furnitureName);
            System.out.println("Adding new furniture colour of " + fm.getName());
            if(colour != null) {
                System.out.println("Adding " + colour + " to colour option");
                colourList = fm.getColour();
                if(colourList != null) {
                    colourList.add(colour);
                    fm.setColour(colourList);
                    em.persist(fm);
                } else {
                    System.out.println("colourList is null");
                    colourList = new ArrayList<String>();
                    colourList.add(colour);
                    fm.setColour(colourList);
                    em.persist(fm);
                }
            }
        } catch(NoResultException ex) {
            System.err.println("Can't find name");
        }
    }
    public void addToBOM(String materialName, String furnitureName) {
        Material material;
        FurnitureModel fm;
        BOM bom;
        BOMDetail BOMdetail ;
        try {
            System.out.println("StockManager:addToBOM");
            material = findMaterialByName(em, materialName);
            fm = findFurnitureByName(em, furnitureName);
            if(fm.getBom() == null) {
                System.out.println("BOM is null");
                bom = new BOM();
                fm.setBom(bom);
                em.persist(fm);
            } else {
                bom = fm.getBom();
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
    public List displayBOM(String furnitureName) {
        FurnitureModel fm;
        List<Material> materialList;
        List<BOMDetail> BOMdetailsList;
        try {
            System.out.println("StockManager:displayBOM()");
            fm = findFurnitureByName(em, furnitureName);
            BOMdetailsList = fm.getBom().getBomDetails();
            materialList = new ArrayList<Material>();
            for(int i=0; i<BOMdetailsList.size(); i++) {
                materialList.add(BOMdetailsList.get(i).getMaterial());
                System.out.println(BOMdetailsList.get(i).getMaterial().getName());
            }
            return materialList;
        } catch(NoResultException NRE) {
            System.err.println("No Furniture called " + furnitureName);
            return null;
        }
    }
    public List<RetailItem> displayItemList() {
        List<RetailItem> itemList;
        try {
            itemList = em.createNamedQuery("getAllRetailItems", RetailItem.class).getResultList();
            return itemList;
        } catch(NoResultException NRE) {
            System.err.println("No results found");
            return null;
        }
        
    }
    public void editRetailItem(Long itemID, String itemName, Double itemPrice) {
        RetailItem item;
        try {
            System.out.println("StockManager.editRetailItem()");
            item = em.find(RetailItem.class, itemID);
            item.setName(itemName);
            item.setPrice(itemPrice);
        } catch(Exception ex) {
            System.out.println("Something went wrong");
        }
    }
    public boolean deleteRetailItem(Long itemID) {
        RetailItem item;
        try {
            System.out.println("StockManager.deleteRetailItem()");
            item = em.find(RetailItem.class, itemID);
            if(item.getSoldBy() != null) {
                System.err.println("Can't delete " + item.getName() +".\n But still gonna delete for now to remind myself I need to perform logical deletion");
                em.remove(item);
                em.flush();
                return false;
            } else {
                em.remove(item);
                em.flush();
                return true;
            }
        } catch(Exception ex) {
            System.err.println("Something went wrong");
            return false;
        }
    }
    public boolean addRetailItem(String itemName, Double itemPrice) {
        RetailItem item;
        try {
            System.out.println("StockManager.addRetailItem()");
            item = findRetailItemByName(em, itemName);
            if(item == null) {
                item = new RetailItem();
                item.setName(itemName);
                item.setPrice(itemPrice);
                em.persist(item);
                return true;
            } else
                return false;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return false;
        }
    }
}
