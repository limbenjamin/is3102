/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.FurnitureModel;
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

    public boolean addFurnitureModel(String name);

    public List<FurnitureModel> displayFurnitureList();

    public void editFurnitureModel(String name, String price);

    public void addToBOM(String materialName, String furnitureName);

    public void addFurnitureColour(String furnitureName, String colour);

    public List displayBOM(String furnitureName);

    public void deleteMaterial(String materialName);
    
}
