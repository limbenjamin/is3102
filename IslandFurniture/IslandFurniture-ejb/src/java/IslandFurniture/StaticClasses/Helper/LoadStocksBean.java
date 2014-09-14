/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Material;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.StockSuppliedPK;
import IslandFurniture.EJB.Entities.Store;
import java.util.List;
import java.util.Random;
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
        FurnitureModel furniture = QueryMethods.findFurnitureByName(em, name);

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
        RetailItem retailItem = QueryMethods.findRetailItemByName(em, name);

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

    private Material addMaterial(String name, double weight) {
        Material material = QueryMethods.findMaterialByName(em, name);

        if (material == null) {
            material = new Material();
            material.setName(name);
            material.setMaterialWeight(weight);
            em.persist(material);

            return material;
        } else {
            System.out.println("Material \"" + name + "\" already exists");

            return null;
        }
    }

    private StockSupplied addStockSupplied(Stock stock, Store store, ManufacturingFacility mf) {
        StockSuppliedPK stockSuppliedPK = new StockSuppliedPK(stock.getId(), store.getId());

        StockSupplied stockSupplied = em.find(StockSupplied.class, stockSuppliedPK);

        if (stockSupplied == null) {
            stockSupplied = new StockSupplied();
            stockSupplied.setStock(stock);
            stockSupplied.setStore(store);
            stockSupplied.setManufacturingFacility(mf);
            em.persist(stockSupplied);

            return stockSupplied;
        } else {
            System.out.println("StockSupplied \"" + stockSupplied + "\" already exists");

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
            this.addRetailItem("Pisang Goreng - Original - Regular");
            this.addRetailItem("Pisang Goreng - Original - Party Pack");
            this.addRetailItem("Pisang Goreng - Lightly Salted - Regular");
            this.addRetailItem("Pisang Goreng - Lightly Salted - Party Pack");
            this.addRetailItem("Pisang Goreng - Spicy - Regular");
            this.addRetailItem("Pisang Goreng - Spicy - Party Pack");
            this.addRetailItem("Merlion Key Chain 01");
            this.addRetailItem("Merlion Key Chain 02");
            this.addRetailItem("Merlion Key Chain 03");
            this.addRetailItem("Orchid Paper Weight 01");
            this.addRetailItem("Orchid Paper Weight 02");
            this.addRetailItem("Tote Bag - Esplanade Design");
            this.addRetailItem("Tote Bag - MBS Design");
            this.addRetailItem("Tote Bag - Botanic Gardens Design");
            this.addRetailItem("Durian Crisp - Regular");
            this.addRetailItem("Durian Crisp - Party Pack");
            this.addRetailItem("Sambal Cheese Stick - Regular");
            this.addRetailItem("Sambal Cheese Stick - Party Pack");

            // Add some Materials
            this.addMaterial("Flathead Screw, Plus (5mm x 15mm)", 10);
            this.addMaterial("Flathead Screw, Minus (5mm x 15mm)", 10);
            this.addMaterial("Roundhead Screw, Plus (5mm x 15mm)", 12);
            this.addMaterial("Roundhead Screw, Minus (5mm x 15mm)", 12);
            this.addMaterial("Drawer Track, Black (500mm)", 86);
            this.addMaterial("Drawer Track, Black (250mm)", 43);
            this.addMaterial("Drawer Track, White (500mm)", 86);
            this.addMaterial("Drawer Track, White (250mm)", 43);
            this.addMaterial("Cabinet Door Hinge 01, Black", 62);
            this.addMaterial("Cabinet Door Hinge 01, White", 62);
            this.addMaterial("Rosewood Plank (30mm x 5000mm x 1000mm)", 10000);
            this.addMaterial("Rosewood Plank (15mm x 5000mm x 1000mm)", 5000);
            this.addMaterial("Cherrywood Plank (30mm x 5000mm x 1000mm)", 9000);
            this.addMaterial("Cherrywood Plank (15mm x 5000mm x 1000mm)", 4500);
            this.addMaterial("Pulpwood Plank (30mm x 5000mm x 1000mm)", 6120);
            this.addMaterial("Pulpwood Plank (15mm x 5000mm x 1000mm)", 3060);
            this.addMaterial("Steel Knob 01", 55);
            this.addMaterial("Steel Knob 02", 52);
            this.addMaterial("Steel Knob 03", 65);

            // Add StockSupplied Relationship (Dependant on prior data loading
            // of Organisation Entities
            List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();
            List<ManufacturingFacility> mfs = (List<ManufacturingFacility>) em.createNamedQuery("getAllMFs").getResultList();
            List<FurnitureModel> furnitureModels = (List<FurnitureModel>) em.createNamedQuery("getAllFurnitureModels").getResultList();
            List<RetailItem> retailItems = (List<RetailItem>) em.createNamedQuery("getAllRetailItems").getResultList();
            Random rand = new Random(1);

            for (Store eachStore : stores) {
                for (FurnitureModel eachFm : furnitureModels) {
                    if (rand.nextBoolean()) {
                        this.addStockSupplied(eachFm, eachStore, mfs.get(rand.nextInt(mfs.size())));
                    }

                }
                for (RetailItem eachRi : retailItems) {
                    if (rand.nextBoolean()) {
                        this.addStockSupplied(eachRi, eachStore, mfs.get(rand.nextInt(mfs.size())));
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false;
        }
    }

}
