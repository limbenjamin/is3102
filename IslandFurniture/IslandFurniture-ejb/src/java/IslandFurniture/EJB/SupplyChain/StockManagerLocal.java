/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.RetailItem;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface StockManagerLocal {

    public List displayMaterialList();

    public boolean updateMaterial(Long id, String name, Double weight);

    public boolean addMaterial(String name, Double weight);

    public FurnitureModel addFurnitureModel(String name, Double price);

    public List<FurnitureModel> displayFurnitureList();

    public void deleteMaterial(Long materialID);

    public List<RetailItem> displayItemList();

    public void editRetailItem(Long itemID, String itemName, Double itemPrice);

    public boolean deleteRetailItem(Long itemID);

    public boolean addRetailItem(String itemName, Double itemPrice);

    public void deleteFurnitureModel(Long furnitureID);

    public void editFurnitureModel(Long furnitureID, String furnitureName, Double price);

    public FurnitureModel getFurniture(Long id);

    public void addToBOM(Long furnitureID, Long materialID, Integer materialQuantity);

    public List displayBOM(Long furnitureID);

    public void editBOMDetail(Long BOMDetailID, Integer quantity);

    public void deleteBOMDetail(Long BOMDetailID);

    public void addFurnitureColour(Long furnitureID, String colour);
    
}
