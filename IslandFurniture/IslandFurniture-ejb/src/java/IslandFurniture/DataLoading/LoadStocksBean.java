/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.Entities.BOM;
import IslandFurniture.Entities.BOMDetail;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Material;
import IslandFurniture.Entities.ProductionCapacity;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.StockSuppliedPK;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import IslandFurniture.StaticClasses.QueryMethods;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.ejb.EJB;
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

    @EJB
    StockManagerLocal sml;

    private FurnitureModel addFurnitureModel(String name, long points, String description, FurnitureCategory fCat, FurnitureSubcategory fsCat) {
        FurnitureModel furniture = QueryMethods.findFurnitureByName(em, name);

        if (furniture == null) {
            BOM bom = new BOM();
            furniture = new FurnitureModel();
            furniture.setName(name);
            furniture.setPointsWorth(points);
            furniture.setFurnitureDescription(description);
            furniture.setBom(bom);
            furniture.setCategory(fCat);
            furniture.setSubcategory(fsCat);
            em.persist(furniture);

            return furniture;
        } else {
            System.out.println("Furniture Model \"" + name + "\" already exists");

            return null;
        }
    }

    private RetailItem addRetailItem(String name, long points) {
        RetailItem retailItem = QueryMethods.findRetailItemByName(em, name);

        if (retailItem == null) {
            retailItem = new RetailItem();
            retailItem.setName(name);
            retailItem.setPointsWorth(points);
            retailItem.setRiDescription("This is an item sold at your local IslandFurniture store. Grab yours today!");
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

    private StockSupplied addStockSupplied(Stock stock, CountryOffice co, ManufacturingFacility mf, double price) {
        StockSuppliedPK stockSuppliedPK = new StockSuppliedPK(stock.getId(), co.getId());

        StockSupplied stockSupplied = em.find(StockSupplied.class, stockSuppliedPK);

        if (stockSupplied == null) {
            stockSupplied = new StockSupplied();
            stockSupplied.setStock(stock);
            stockSupplied.setCountryOffice(co);
            stockSupplied.setManufacturingFacility(mf);
            stockSupplied.setPrice(price);
            em.persist(stockSupplied);
            co.getSuppliedWithFrom().add(stockSupplied);

            if (mf != null) {
                mf.getSupplyingWhatTo().add(stockSupplied);
            }

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
     * @param mode 0 for Auto Mode, 1 for Manual Mode
     * @return
     */
    @Override
    public boolean loadSampleData(int mode) {
        try {
            // Start Random var
            Random rand = new Random(1);

            if (mode == 0) {
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

                // Add some Furniture Models
                this.addFurnitureModel("Swivel Chair", 500, "A chair that goes round and round and round, and up and down.", FurnitureCategory.WORKSPACE, FurnitureSubcategory.CHAIRS);
                this.addFurnitureModel("Round Table", 2500, "A perfectly round solid wooden table. We guarantee you won't find a rough edge.", FurnitureCategory.LIVING_ROOM, FurnitureSubcategory.TABLES);
                this.addFurnitureModel("Coffee Table", 3000, "The Coffee table perfect for coffee. Comes in coffee colour that blends perfectly with coffeee spills.", FurnitureCategory.LIVING_ROOM, FurnitureSubcategory.TABLES);
                this.addFurnitureModel("Study Table - Dinosaur Edition", 3250, "An artpiece loved by all from young to old. Has pointy razor sharp teethed edges to cover all your severing needs!", FurnitureCategory.WORKSPACE, FurnitureSubcategory.TABLES);
                this.addFurnitureModel("Bedside Lamp H31", 80, "This lamp goes well with any bed you can imagine. Never out of place and definitely a bright addition to your lovely room.", FurnitureCategory.BEDROOM, FurnitureSubcategory.LIGHTING);
                this.addFurnitureModel("Bathroom Rug E64", 30, "With this bathroom rug, never slip and fall again! Also proven to prevent cold feet for a new confident you.", FurnitureCategory.BATHROOM, FurnitureSubcategory.TEXTILE);
                this.addFurnitureModel("Kitchen Stool", 250, "A chair without a back, perfect for the lazy bones to correct their posture.", FurnitureCategory.KITCHEN, FurnitureSubcategory.CHAIRS);

                // Add BOM to FurnitureModel
                List<FurnitureModel> fmList = em.createNamedQuery("getAllFurnitureModels").getResultList();
                List<Material> materialList = em.createNamedQuery("Material.getMaterialList").getResultList();
                BOM bom;
                BOMDetail bomDetail;
                List<BOMDetail> bomDetails;

                for (FurnitureModel fm : fmList) {
                    bom = new BOM();
                    bomDetails = new ArrayList();

                    do {
                        for (Material material : materialList) {
                            if (rand.nextBoolean()) {
                                bomDetail = new BOMDetail();
                                bomDetail.setMaterial(material);
                                bomDetail.setQuantity(rand.nextInt(4) + 1);
                                bomDetail.setBom(bom);

                                bomDetails.add(bomDetail);
                            }
                        }
                    } while (bomDetails.isEmpty());

                    bom.setBomDetails(bomDetails);

                    fm.setBom(bom);
                }

                // Add some Retail Items
                this.addRetailItem("Pisang Goreng - Original - Regular", 50);
                this.addRetailItem("Pisang Goreng - Original - Party Pack", 80);
                this.addRetailItem("Pisang Goreng - Lightly Salted - Regular", 50);
                this.addRetailItem("Pisang Goreng - Lightly Salted - Party Pack", 80);
                this.addRetailItem("Pisang Goreng - Spicy - Regular", 50);
                this.addRetailItem("Pisang Goreng - Spicy - Party Pack", 80);
                this.addRetailItem("Merlion Key Chain 01", 30);
                this.addRetailItem("Merlion Key Chain 02", 30);
                this.addRetailItem("Merlion Key Chain 03", 40);
                this.addRetailItem("Orchid Paper Weight 01", 100);
                this.addRetailItem("Orchid Paper Weight 02", 110);
                this.addRetailItem("Tote Bag - Esplanade Design", 180);
                this.addRetailItem("Tote Bag - MBS Design", 180);
                this.addRetailItem("Tote Bag - Botanic Gardens Design", 180);
                this.addRetailItem("Durian Crisp - Regular", 50);
                this.addRetailItem("Durian Crisp - Party Pack", 80);
                this.addRetailItem("Sambal Cheese Stick - Regular", 60);
                this.addRetailItem("Sambal Cheese Stick - Party Pack", 90);

                // Add StockSupplied Relationship (Dependant on prior data loading
                // of Organisation Entities)
                List<CountryOffice> coList = (List<CountryOffice>) em.createNamedQuery("getAllCountryOffices").getResultList();
                List<ManufacturingFacility> mfs = (List<ManufacturingFacility>) em.createNamedQuery("getAllMFs").getResultList();

                List<FurnitureModel> furnitureModels = (List<FurnitureModel>) em.createNamedQuery("getAllFurnitureModels").getResultList();
                List<RetailItem> retailItems = (List<RetailItem>) em.createNamedQuery("getAllRetailItems").getResultList();

                java.util.Currency javaCurrency;

                for (CountryOffice eachCo : coList) {
                    javaCurrency = java.util.Currency.getInstance(eachCo.getCountry().getCurrency().getCurrencyCode());

                    for (FurnitureModel eachFm : furnitureModels) {
                        if (rand.nextBoolean()) {
                            this.addStockSupplied(eachFm, eachCo, mfs.get(rand.nextInt(mfs.size())), Math.floor((rand.nextInt(30000) + 1.0) / Math.pow(10.0, javaCurrency.getDefaultFractionDigits()) * 100) / 100);
                        }
                    }
                    for (RetailItem eachRi : retailItems) {
                        if (rand.nextBoolean()) {
                            this.addStockSupplied(eachRi, eachCo, mfs.get(rand.nextInt(mfs.size())), Math.floor((rand.nextInt(300) + 1.0) / Math.pow(10.0, javaCurrency.getDefaultFractionDigits()) * 100) / 100);
                        }
                    }

                }

                em.flush();
            } else {
                // Manual data loading
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
                this.addMaterial("Rosewood Block (200mm x 200mm x 5000mm)", 25000);
                this.addMaterial("Cherrywood Plank (30mm x 5000mm x 1000mm)", 9000);
                this.addMaterial("Cherrywood Plank (15mm x 5000mm x 1000mm)", 4500);
                this.addMaterial("Cherrywood Block (80mm x 80mm x 800mm)", 3400);
                this.addMaterial("Pulpwood Plank (30mm x 5000mm x 1000mm)", 6120);
                this.addMaterial("Pulpwood Plank (15mm x 5000mm x 1000mm)", 3060);
                this.addMaterial("Steel Knob 01", 55);
                this.addMaterial("Steel Knob 02", 52);
                this.addMaterial("Steel Knob 03", 65);
                this.addMaterial("Castor Wheel (Plastic), White", 125);
                this.addMaterial("Castor Wheel (Metal), Black", 145);
                this.addMaterial("Ceramic Lamp Mould, White", 255);
                this.addMaterial("Screwhead Bulb (5500lm, 60W)", 255);
                this.addMaterial("Chrome Steel Wiring (White, 1000mm)", 5000);
                this.addMaterial("Dyed Thread (Blue, 20000mm)", 290);
                this.addMaterial("Dyed Thread (Green, 20000mm)", 290);
                this.addMaterial("Cloth Base (White, 450mm x 600mm)", 140);
                this.addMaterial("Chrome Steel Pipe (White, D30mm x L1000mm)", 245);

                FurnitureModel fm;
                BOM bom;
                List<BOMDetail> bomDetails = new ArrayList();

                fm = this.addFurnitureModel("Swivel Chair", 500, "A chair that goes round and round and round, and up and down.", FurnitureCategory.WORKSPACE, FurnitureSubcategory.CHAIRS);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Flathead Screw, Plus (5mm x 15mm)"), 4));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Plank (30mm x 5000mm x 1000mm)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Castor Wheel (Metal), Black"), 4));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Round Table", 2500, "A perfectly round solid wooden table. We guarantee you won't find a rough edge.", FurnitureCategory.LIVING_ROOM, FurnitureSubcategory.TABLES);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Flathead Screw, Plus (5mm x 15mm)"), 22));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Plank (30mm x 5000mm x 1000mm)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Cherrywood Block (80mm x 80mm x 800mm)"), 4));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Coffee Table", 3000, "The Coffee table perfect for coffee. Comes in coffee colour that blends perfectly with coffeee spills.", FurnitureCategory.LIVING_ROOM, FurnitureSubcategory.TABLES);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Flathead Screw, Plus (5mm x 15mm)"), 6));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Plank (30mm x 5000mm x 1000mm)"), 1));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Study Table - Dinosaur Edition", 3250, "An artpiece loved by all from young to old. Has pointy razor sharp teethed edges to cover all your severing needs!", FurnitureCategory.WORKSPACE, FurnitureSubcategory.TABLES);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Flathead Screw, Plus (5mm x 15mm)"), 6));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Plank (30mm x 5000mm x 1000mm)"), 3));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Steel Knob 01"), 3));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Bedside Lamp H31", 80, "This lamp goes well with any bed you can imagine. Never out of place and definitely a bright addition to your lovely room.", FurnitureCategory.BEDROOM, FurnitureSubcategory.LIGHTING);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Flathead Screw, Plus (5mm x 15mm)"), 2));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Ceramic Lamp Mould, White"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Screwhead Bulb (5500lm, 60W)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Chrome Steel Wiring (White, 1m)"), 1));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Bathroom Rug E64", 30, "Blue bathroom rug. With this rug, never slip and fall again! Also proven to prevent cold feet for a new confident you.", FurnitureCategory.BATHROOM, FurnitureSubcategory.TEXTILE);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Dyed Thread (Blue, 20000mm)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Cloth Base (White, 450mm x 600mm)"), 1));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Bathroom Rug E36", 30, "Green bathroom rug. With this rug, never slip and fall again! Also proven to prevent cold feet for a new confident you.", FurnitureCategory.BATHROOM, FurnitureSubcategory.TEXTILE);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Dyed Thread (Green, 20000mm)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Cloth Base (White, 450mm x 600mm)"), 1));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Kitchen Stool", 250, "A chair without a back, perfect for the lazy bones to correct their posture.", FurnitureCategory.KITCHEN, FurnitureSubcategory.CHAIRS);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Cherrywood Plank (30mm x 5000mm x 1000mm)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Chrome Steel Pipe (White, D30mm x L1000mm)"), 2));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Wooven Plaster Sofa", 12000, "Designed by reknown guest designer Italio Fernando from Rome, this sofa is the epitome of integration between design and comfort.", FurnitureCategory.LIVING_ROOM, FurnitureSubcategory.CHAIRS);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Block (200mm x 200mm x 5000mm)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Chrome Steel Pipe (White, D30mm x L1000mm)"), 6));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Dyed Thread (Green, 20000mm)"), 2));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Cloth Base (White, 450mm x 600mm)"), 10));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Gothic Bed Frame (Queen Size)", 8000, "Perfect bed frame for the odd couple. With a dash of wierdness and plenty of comfort.", FurnitureCategory.BEDROOM, FurnitureSubcategory.BEDS);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Block (200mm x 200mm x 5000mm)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Chrome Steel Pipe (White, D30mm x L1000mm)"), 12));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Cloth Base (White, 450mm x 600mm)"), 5));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Ninja Night Stand" , 4200, "Bedside stand for the stealthy. Ninja star not included.", FurnitureCategory.BEDROOM, FurnitureSubcategory.STORAGE);
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Cherrywood Block (80mm x 80mm x 800mm)"), 1));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Drawer Track, Black (250mm)"), 2));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Steel Knob 03"), 1));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                Set<RetailItem> retailItems = new HashSet();

                retailItems.add(this.addRetailItem("Pisang Goreng - Original - Regular", 50));
                retailItems.add(this.addRetailItem("Pisang Goreng - Lightly Salted - Regular", 50));
                retailItems.add(this.addRetailItem("Merlion Key Chain 01", 30));
                retailItems.add(this.addRetailItem("Orchid Paper Weight 01", 100));
                retailItems.add(this.addRetailItem("Tote Bag - Esplanade Design", 180));
                retailItems.add(this.addRetailItem("Durian Crisp - Regular", 50));
                retailItems.add(this.addRetailItem("Sambal Cheese Stick - Regular", 60));

                CountryOffice co;
                ManufacturingFacility mf;
                java.util.Currency javaCurrency;

                mf = (ManufacturingFacility) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Singapore"), "Tuas");
                co = (CountryOffice) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Singapore"), "Singapore");
                javaCurrency = java.util.Currency.getInstance(co.getCountry().getCurrency().getCurrencyCode());
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Swivel Chair"), co, mf, 50.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Coffee Table"), co, mf, 300.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Study Table - Dinosaur Edition"), co, mf, 325.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Gothic Bed Frame (Queen Size)"), co, mf, 800.00);
                for (RetailItem ri : retailItems) {
                    this.addStockSupplied(ri, co, mf, Math.floor((rand.nextInt(300) + 1.0) / Math.pow(10.0, javaCurrency.getDefaultFractionDigits()) * 100) / 100);
                }

                co = (CountryOffice) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Malaysia"), "Malaysia");
                javaCurrency = java.util.Currency.getInstance(co.getCountry().getCurrency().getCurrencyCode());
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Swivel Chair"), co, mf, 125.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Coffee Table"), co, mf, 645.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Gothic Bed Frame (Queen Size)"), co, mf, 1720.00);
                for (RetailItem ri : retailItems) {
                    this.addStockSupplied(ri, co, mf, Math.floor((rand.nextInt(300) + 1.0) / Math.pow(10.0, javaCurrency.getDefaultFractionDigits()) * 100) / 100);
                }

                mf = (ManufacturingFacility) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "China"), "Su Zhou - Su Zhou Industrial Park");
                co = (CountryOffice) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Singapore"), "Singapore");
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Round Table"), co, mf, 250.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Bedside Lamp H31"), co, mf, 8.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Bathroom Rug E64"), co, mf, 3.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Bathroom Rug E36"), co, mf, 3.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Kitchen Stool"), co, mf, 2.50);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Wooven Plaster Sofa"), co, mf, 1200.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Ninja Night Stand"), co, mf, 420.00);

                co = (CountryOffice) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Malaysia"), "Malaysia");
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Round Table"), co, mf, 530.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Wooven Plaster Sofa"), co, mf, 2940.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Study Table - Dinosaur Edition"), co, mf, 700.00);
                this.addStockSupplied(QueryMethods.findFurnitureByName(em, "Ninja Night Stand"), co, mf, 900.00);
            }

            // Populate production capacities for each manufacturing facilities, each furniture model
            List<ManufacturingFacility> mfList = (List<ManufacturingFacility>) em.createNamedQuery("getAllMFs").getResultList();
            for (ManufacturingFacility eachMf : mfList) {
                for (StockSupplied ss : eachMf.getSupplyingWhatTo()) {
                    if (ss.getStock() instanceof FurnitureModel) {
                        ProductionCapacity prodCap = new ProductionCapacity();
                        prodCap.setFurnitureModel((FurnitureModel) ss.getStock());
                        prodCap.setManufacturingFacility(eachMf);
                        prodCap.setQty(500);
                        em.merge(prodCap);
                    }
                }
            }

            sml.addNFCTagToStock("Swivel Chair", "04e48cea5d2b80");
            sml.addNFCTagToStock("Coffee Table", "04e4a0ea5d2b80");
            sml.addNFCTagToStock("Study Table - Dinosaur Edition", "04e100ea412b84");

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false;
        }
    }

}
