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
import IslandFurniture.EJB.Entities.StockSupplied;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findFurnitureByName;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findMaterialByName;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findRetailItemByName;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.getBOMDetailByMaterial;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.getStockSuppliedByStock;
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
            System.out.println("StockManager.updateMaterial()");
            material = findMaterialByName(em, name);
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
    public void deleteMaterial(Long materialID) {
        Material material;
        List<BOMDetail> bomList;
        try{
            System.out.println("StockManager.deleteMaterial()");
            material = em.find(Material.class, materialID);
            bomList = getBOMDetailByMaterial(em, material);
            if(bomList.size() > 0)
                System.err.println("Invalid deletion due to existing BOM");
            else
                em.remove(material);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public FurnitureModel addFurnitureModel(String name, Double price) {
        FurnitureModel fm;
        BOM bom;
        List<BOMDetail> bomList;
        try {
            System.out.println("StockManager.addFurnitureModel()");
            fm = new FurnitureModel(name);
            bom = new BOM();
            bomList = new ArrayList<BOMDetail>();
            
            bom.setBomDetails(bomList);
            fm.setPrice(price);
            fm.setBom(bom);
            em.persist(fm);
            return fm;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public FurnitureModel getFurniture(Long id) {
        FurnitureModel furniture;
        try {
            System.out.println("StockManager.getFurniture()");
            furniture = em.find(FurnitureModel.class, id);
            return furniture;            
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
    public List<FurnitureModel> displayFurnitureList() {
        List furnitureList;
        try {
            System.out.println("StockManager.displayFurnitureList()");
            furnitureList = em.createNamedQuery("getAllFurnitureModels", FurnitureModel.class).getResultList();
            return furnitureList;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public void editFurnitureModel(Long furnitureID, String furnitureName, Double price) {
        FurnitureModel fm;
        try {
            System.out.println("StockManager.editFurnitureModel()");
            fm = em.find(FurnitureModel.class, furnitureID);
            System.out.println("Updating furniture information of " + fm.getName());
            fm.setName(furnitureName);
            fm.setPrice(price);
            em.persist(fm);
        } catch(NoResultException ex) {
            System.err.println("Can't find name");
        }
    }
    public void deleteFurnitureModel(Long furnitureID) {
        FurnitureModel fm;
        List<StockSupplied> stockList;
        List<BOMDetail> bomDetailList;
        BOM bom;
        try {
            System.out.println("StockManager.deleteFurnitureModel()");
            System.out.println(furnitureID);
            fm = em.find(FurnitureModel.class, furnitureID);
            stockList = getStockSuppliedByStock(em, fm);
            bom = fm.getBom();
            bomDetailList = bom.getBomDetails();
            
            System.out.println("Will go ahead to delete everything now");
            if(stockList.size() >= 1)
                System.out.println("Invalid delete due to existence of Stock Supply Request");
            else {
                for(int i=0; i<bomDetailList.size(); i++) 
                    em.remove(bomDetailList.get(i));
                em.remove(bom);
                em.remove(fm);
            }
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
        }
    }
    public void addFurnitureColour(Long furnitureID, String colour) {
        FurnitureModel fm;
        List colourList;
        try {
            System.out.println("StockManager.addFurnitureColour()");
            fm = em.find(FurnitureModel.class, furnitureID);
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
    public void addToBOM(Long furnitureID, Long materialID, Integer materialQuantity) {
        Material material;
        FurnitureModel fm;
        BOM bom;
        BOMDetail BOMdetail ;
        try {
            System.out.println("StockManager:addToBOM");
            material = em.find(Material.class, materialID);
            fm = em.find(FurnitureModel.class, furnitureID);
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
            BOMdetail.setQuantity(materialQuantity);
            BOMdetail.setBom(bom);
            bom.getBomDetails().add(BOMdetail);
            System.out.println("Successfully added new BOMDetail");
            em.flush();
           
        } catch(NoResultException NRE) {
            System.err.println("Can't find name");
        }
    }
    public List displayBOM(Long furnitureID) {
        FurnitureModel fm;
        List<BOMDetail> BOMdetailsList;
        try {
            System.out.println("StockManager.displayBOM()");
            fm = em.find(FurnitureModel.class, furnitureID);
            BOMdetailsList = fm.getBom().getBomDetails();
            return BOMdetailsList;
        } catch(NoResultException NRE) {
            System.err.println("No such Furniture found");
            return null;
        }
    }
    public void editBOMDetail(Long BOMDetailID, Integer quantity) {
        BOMDetail BOMdetail;
        try {
            System.out.println("StockManager.editBOMDetail()");
            BOMdetail = em.find(BOMDetail.class, BOMDetailID);
            BOMdetail.setQuantity(quantity);
            em.persist(BOMdetail);
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
        }
    }
    public void deleteBOMDetail(Long BOMDetailID) {
        BOMDetail BOMdetail;
        BOM bom;
        try {
            System.out.println("StockManager.deleteBOMDetail()");
            BOMdetail = em.find(BOMDetail.class, BOMDetailID);
            bom = BOMdetail.getBom();
            bom.getBomDetails().remove(BOMdetail);
            em.remove(BOMdetail);
            em.persist(bom);
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
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
        List<StockSupplied> stockList;
        try {
            System.out.println("StockManager.deleteRetailItem()");
            item = em.find(RetailItem.class, itemID);
            stockList = getStockSuppliedByStock(em, item);
            if(item.getSoldBy().size() >= 1) {
                System.err.println("Can't delete " + item.getName() + " because it is currently sold by a store");
                return false;
            } else if(stockList.size() >= 1) {
                System.out.println("Invalid delete due to existence of Stock Supply Request");
                return false;
            } else {
                em.remove(item);
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
