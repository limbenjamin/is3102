/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.Entities.BOM;
import IslandFurniture.Entities.BOMDetail;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Material;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import IslandFurniture.StaticClasses.QueryMethods;
import static IslandFurniture.StaticClasses.QueryMethods.findFurnitureByName;
import static IslandFurniture.StaticClasses.QueryMethods.findMaterialByName;
import static IslandFurniture.StaticClasses.QueryMethods.findPCDByStock;
import static IslandFurniture.StaticClasses.QueryMethods.findRetailItemByName;
import static IslandFurniture.StaticClasses.QueryMethods.getBomDetailByMaterial;
import static IslandFurniture.StaticClasses.QueryMethods.getStockSuppliedByStock;
import java.text.DecimalFormat;
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
    
    @Override
    public boolean addMaterial(String name, Double weight) {
        Material material;
        try{
            System.out.println("StockManager.addMaterial()");
            material = findMaterialByName(em, name);
            if(material != null) {
                if(!material.isHidden()) {
                    System.out.println("Material " + name + " already exists in database");
                    return false;
                } else 
                    material.setHidden(false);
            } else {
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
    @Override
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
    @Override
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
    @Override
    public String deleteMaterial(Long materialID) {
        Material material;
        List<BOMDetail> bomList;
        List<ProcuredStockContractDetail> pcdList;
        try{
            System.out.println("StockManager.deleteMaterial()");
            material = em.find(Material.class, materialID);
            bomList = getBomDetailByMaterial(em, material);
            pcdList = findPCDByStock(em, (ProcuredStock)material);
            if(bomList.size() > 0) {
                System.err.println("Invalid deletion due to existing BOM");
                if(pcdList.size() > 0) { 
                    System.err.println("Invalid deletion due to existing Procurement Contract Detail for " + material.getName());
                    return "Invalid deletion due to existing BOM<br/>Invalid deletion due to existing Procurement Contract Detail";
                }
                return "Invalid deletion due to existing BOM";
            } else if(pcdList.size() > 0) {
                System.err.println("Invalid deletion due to existing Procurement Contract Detail for " + material.getName());
                return "Invalid deletion due to existing Procurement Contract Detail for \"" + material.getName() + "\"";
            } else
                em.remove(material);
            return null;
        } catch(Exception ex) {
            ex.printStackTrace();
            return "Unexpected error occured during deletion";
        }
    }
    @Override
    public String addFurnitureModel(String name) {
        FurnitureModel fm;
        BOM bom;
        List<BOMDetail> bomList;
        String msg = null;
        try {
            System.out.println("StockManager.addFurnitureModel()");
            fm = findFurnitureByName(em, name);
            if(fm == null) {
                fm = new FurnitureModel(name);
                bom = new BOM();
                bomList = new ArrayList<>();

                bom.setBomDetails(bomList);
                fm.setBom(bom);
                em.persist(fm);
                msg = "" + fm.getId() + "#0"; 
            } else {
                System.out.println("FurnitureModel " + name + " already exists. Directing to Furniture Details");
                msg = "" + fm.getId() + "#FurnitureModel " + name + " already exists. Directed to Furniture Details";
            }
            return msg;
        } catch(Exception ex) {
            ex.printStackTrace();
            return "Unexpected error occured";
        }
    }
    @Override
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
    @Override
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
    @Override
    public String editFurnitureModel(Long furnitureID, String furnitureName) {
        FurnitureModel fm;
        try {
            System.out.println("StockManager.editFurnitureModel()");
            fm = em.find(FurnitureModel.class, furnitureID);
            System.out.println("Updating furniture information of " + fm.getName());
            fm.setName(furnitureName);
            em.persist(fm);
            return null;
        } catch(NoResultException ex) {
            System.err.println("Can't find name");
            return "Unexpected error occured";
        }
    }
    @Override
    public String deleteFurnitureModel(Long furnitureID) {
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
            if(QueryMethods.getStockSuppliedByStock(em, fm).size() >= 1) {
                System.err.println("Invalid deletion as it is currently sold by a store");
                return "Invalid deletion as it is currently sold by a store";
            }
            if(stockList.size() >= 1) {
                System.out.println("Invalid delete due to existence of Stock Supply Request");
                return "Invalid deletion due to existence of Stock Supply Request";
            }
            else {
                for(int i=0; i<bomDetailList.size(); i++) 
                    em.remove(bomDetailList.get(i));
                em.remove(bom);
                em.remove(fm);
            }
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    @Override
    public String editFurnitureCategory(Long furnitureID, FurnitureCategory category) {
        FurnitureModel fm;
        try {
            System.out.println("StockManager.editFurnitureCategory()");
            fm = em.find(FurnitureModel.class, furnitureID);
            fm.setCategory(category);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    @Override
    public String editFurnitureSubcategory(Long furnitureID, FurnitureSubcategory category) {
        FurnitureModel fm;
        try {
            System.out.println("StockManager.editFurnitureSubcategory()");
            fm = em.find(FurnitureModel.class, furnitureID);
            fm.setSubcategory(category);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    @Override
    public String editFurnitureDescription(Long furnitureID, String description) {
        FurnitureModel fm;
        try {
            System.out.println("StockManager.editFurnitureDescription()");
            fm = em.find(FurnitureModel.class, furnitureID);
            fm.setFurnitureDescription(description);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    @Override
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
    @Override
    public String addToBOM(Long furnitureID, Long materialID, Integer materialQuantity) {
        Material material;
        FurnitureModel fm;
        BOM bom;
        BOMDetail BOMdetail = null;
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
            for(int i=0; i<bom.getBomDetails().size(); i++) {
                if(bom.getBomDetails().get(i).getMaterial().getId().equals(materialID)) {
                    BOMdetail = bom.getBomDetails().get(i);
                    break;
                }
            }
            if(BOMdetail != null) {
                System.out.println("BOMdetail already exists");
                return "BOM Detail already exists ";
            } else {
                BOMdetail = new BOMDetail();
                BOMdetail.setMaterial(material);
                BOMdetail.setQuantity(materialQuantity);
                BOMdetail.setBom(bom);
                bom.getBomDetails().add(BOMdetail);
                System.out.println("Successfully added new BOMDetail");
            }
            em.flush();
            return null;
        } catch(NoResultException NRE) {
            System.err.println("Can't find name");
            return "Unexpected error occured";
        }
    }
    @Override
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
    @Override
    public String editBOMDetail(Long BOMDetailID, Integer quantity) {
        BOMDetail BOMdetail;
        try {
            System.out.println("StockManager.editBOMDetail()");
            BOMdetail = em.find(BOMDetail.class, BOMDetailID);
            BOMdetail.setQuantity(quantity);
            em.persist(BOMdetail);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpect error occured";
        }
    }
    @Override
    public String deleteBOMDetail(Long BOMDetailID) {
        BOMDetail BOMdetail;
        BOM bom;
        try {
            System.out.println("StockManager.deleteBOMDetail()");
            BOMdetail = em.find(BOMDetail.class, BOMDetailID);
            bom = BOMdetail.getBom();
            bom.getBomDetails().remove(BOMdetail);
            em.remove(BOMdetail);
            em.persist(bom);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpect error occured";
        } 
    }
    @Override
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
    @Override
    public String editRetailItem(Long itemID, String itemName, Double itemPrice) {
        RetailItem item;
        try {
            System.out.println("StockManager.editRetailItem()");
            item = em.find(RetailItem.class, itemID);
            item.setName(itemName);
            item.setPrice(itemPrice);
            return null;
        } catch(Exception ex) {
            System.out.println("Something went wrong");
            return "Unexpected error occured";
        }
    }
    @Override
    public String deleteRetailItem(Long itemID) {
        RetailItem item;
        List<StockSupplied> stockList;
        List<ProcuredStockContractDetail> pcdList;
        try {
            System.out.println("StockManager.deleteRetailItem()");
            item = em.find(RetailItem.class, itemID);
            stockList = getStockSuppliedByStock(em, item); 
            pcdList = findPCDByStock(em, (ProcuredStock)item);
            if(QueryMethods.getStockSuppliedByStock(em, item).size() >= 1) {
                System.err.println("Can't delete " + item.getName() + " because it is currently sold by a store");
                if(pcdList.size() > 0) { 
                    System.err.println("Invalid deletion due to existing Procurement Contract Detail for " + item.getName());
                    return "Invalid deletion as it is currently sold by a store<br/>Invalid deletion due to existing Procurement Contract Detail";
                }
                return "Unable to delete \"" + item.getName() + "\" as it is currently sold by a store";
            } else if(stockList.size() >= 1) {
                System.out.println("Invalid delete due to existence of Stock Supply Request");
                return "Unable to delete \"" + item.getName() + "\" due to existence of Stock Supply Request";
            } else {
                em.remove(item);
                return null;
            }
        } catch(Exception ex) {
            System.err.println("Something went wrong");
            return "Unexpected error occured";
        }
    }
    @Override
    public String addRetailItem(String itemName, Double itemPrice) {
        RetailItem item;
        try {
            System.out.println("StockManager.addRetailItem()");
            item = findRetailItemByName(em, itemName);
            if(item == null) {
                item = new RetailItem();
                item.setName(itemName);
                item.setPrice(itemPrice);
                em.persist(item);
                return null;
            } else {
                System.out.println("Item " + itemName + " already exist. Unable to add.");
                return "Retail Item \"" + itemName + "\" already exist in the database";
            }
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    public Double priceConveter(Double originalPrice, Double exchangeRate) {
        DecimalFormat df = new DecimalFormat("#.#");
        Integer basePrice;
        Double realPrice;
        Double price;
        
        System.out.println("Original Price is $" + originalPrice + ". Exchange Rate is $" + exchangeRate + ". Converted Rate is $" + (originalPrice * exchangeRate));
        realPrice = originalPrice * exchangeRate;
        basePrice = realPrice.intValue();
        if(realPrice <= 10) {
                price = originalPrice * exchangeRate;
                System.out.println("Selling price is $" + df.format(price));	
                return price;
        } else if(realPrice <= 50) {
                System.out.println("Selling price is $" + basePrice);
                return basePrice + 0.0;
        } else if(realPrice <= 200) {
                price = 0.0 + basePrice - (basePrice % 5);
                System.out.println("Selling price is $" + price);
                return price;
        } else if(realPrice <= 1000) {
                price = 0.0 + basePrice - (basePrice % 10);
                System.out.println("Selling price is $" + price);
                return price;
        } else if(realPrice <= 5000) {
                price = 0.0 + basePrice - (basePrice % 50);
                System.out.println("Selling price is $" + price);
                return price;
        } else if(realPrice <= 10000) {
                price = 0.0 + basePrice - (basePrice % 100);
                System.out.println("Selling price is $" + price);
                return price;
        } else {
                price = 0.0 + basePrice - (basePrice % 500);
                System.out.println("Selling price is $" + price);
                return price;
        }
    }
}
