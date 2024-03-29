/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author a0101774
 */
@Remote
public interface StockManagerRemote {

    public List displayMaterialList();

    public boolean updateMaterial(Long id, String name, Double weight);

    public boolean addMaterial(String name, Double weight);

    public String addFurnitureModel(String name);

    public List<FurnitureModel> displayFurnitureList();

    public String deleteMaterial(Long materialID);

    public List<RetailItem> displayItemList();

    public String editRetailItem(RetailItem updatedItem);

    public String deleteRetailItem(Long itemID);

    public String addRetailItem(String itemName);

    public String deleteFurnitureModel(Long furnitureID);

    public String editFurnitureModel(Long furnitureID, String furnitureName);

    public FurnitureModel getFurniture(Long id);

    public String addToBOM(Long furnitureID, Long materialID, Integer materialQuantity);

    public List displayBOM(Long furnitureID);

    public String editBOMDetail(Long BOMDetailID, Integer quantity);

    public String deleteBOMDetail(Long BOMDetailID);
    
    public String editFurnitureCategory(Long furnitureID, FurnitureCategory category);

    public String editFurnitureSubcategory(Long furnitureID, FurnitureSubcategory category);

    public String editFurnitureDescription(Long furnitureID, String description);

    public Stock getStock(Long id);
    
    void addNFCTagToStock(String stockName, String NFCTagId);

    public void editThumbnail(Long fID, byte[] content);
    
}
