/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.RetailItem;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadStocksBean implements LoadStocksBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private FurnitureModel addFurnitureModel(String name) {
        FurnitureModel furniture = (FurnitureModel) QueryMethods.findFurnitureByName(em, name);

        if (furniture == null) {
            furniture = new FurnitureModel();
            furniture.setName(name);
            em.persist(furniture);

            return furniture;
        } else {
            System.out.println("Furniture Model \"" + name + "\" already exists");

            return null;
        }
    }
    
    private RetailItem addRetailItem(String name) {
        RetailItem retailItem = (RetailItem) QueryMethods.findRetailItemByName(em, name);

        if (retailItem == null) {
            retailItem = new RetailItem();
            retailItem.setName(name);
            em.persist(retailItem);

            return retailItem;
        } else {
            System.out.println("Retail Item \"" + name + "\" already exists");

            return null;
        }
    }

    /**
     * Loads a sample data list of FurnitureModel, RetailItems, Materials and
     * their StockSupplied Relationships
     *
     * @return
     */
    @Override
    public boolean loadSampleData() {
        try {
            // Add some Furniture Models
            this.addFurnitureModel("Swivel Chair");
            this.addFurnitureModel("Round Table");
            this.addFurnitureModel("Coffee Table");
            this.addFurnitureModel("Study Table - Dinosaur Edition");
            this.addFurnitureModel("Bedside Lamp H31");
            this.addFurnitureModel("Bathroom Rug E64");
            this.addFurnitureModel("Kitchen Stool");
            
            // Add some Retail Items
            
            
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false;
        }
    }

}
