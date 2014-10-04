/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
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

    public String addFurnitureModel(String name, Double price);

    public List<FurnitureModel> displayFurnitureList();

    public String deleteMaterial(Long materialID);

    public List<RetailItem> displayItemList();

    public String editRetailItem(Long itemID, String itemName, Double itemPrice);

    public String deleteRetailItem(Long itemID);

    public String addRetailItem(String itemName, Double itemPrice);

    public String deleteFurnitureModel(Long furnitureID);

    public String editFurnitureModel(Long furnitureID, String furnitureName, Double price);

    public FurnitureModel getFurniture(Long id);

    public String addToBOM(Long furnitureID, Long materialID, Integer materialQuantity);

    public List displayBOM(Long furnitureID);

    public String editBOMDetail(Long BOMDetailID, Integer quantity);

    public String deleteBOMDetail(Long BOMDetailID);

    public void addFurnitureColour(Long furnitureID, String colour);
    
}
