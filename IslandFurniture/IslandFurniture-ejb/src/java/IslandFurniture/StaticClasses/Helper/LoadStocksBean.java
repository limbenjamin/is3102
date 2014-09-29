/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.BOM;
import IslandFurniture.EJB.Entities.BOMDetail;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Material;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.StockSuppliedPK;
import IslandFurniture.EJB.Entities.Store;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
            BOM bom = new BOM();
            furniture = new FurnitureModel();
            furniture.setName(name);
            furniture.setBom(bom);
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

    private StockSupplied addStockSupplied(Stock stock, CountryOffice co, ManufacturingFacility mf) {
        StockSuppliedPK stockSuppliedPK = new StockSuppliedPK(stock.getId(), co.getId(), mf.getId());

        StockSupplied stockSupplied = em.find(StockSupplied.class, stockSuppliedPK);

        if (stockSupplied == null) {
            stockSupplied = new StockSupplied();
            stockSupplied.setStock(stock);
            stockSupplied.setCountryOffice(co);
            stockSupplied.setManufacturingFacility(mf);
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
            if (mode == 0) {
                // Start Random var
                Random rand = new Random(1);

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
                this.addFurnitureModel("Swivel Chair");
                this.addFurnitureModel("Round Table");
                this.addFurnitureModel("Coffee Table");
                this.addFurnitureModel("Study Table - Dinosaur Edition");
                this.addFurnitureModel("Bedside Lamp H31");
                this.addFurnitureModel("Bathroom Rug E64");
                this.addFurnitureModel("Kitchen Stool");

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

                // Add List of stuff each store will sell
                List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();
                List<FurnitureModel> furnitureModels = (List<FurnitureModel>) em.createNamedQuery("getAllFurnitureModels").getResultList();
                List<RetailItem> retailItems = (List<RetailItem>) em.createNamedQuery("getAllRetailItems").getResultList();

                for (Store eachStore : stores) {
                    for (FurnitureModel eachFm : furnitureModels) {
                        if (rand.nextBoolean()) {
                            eachStore.getSells().add(eachFm);
                        }
                    }
                    for (RetailItem eachRi : retailItems) {
                        if (rand.nextBoolean()) {
                            eachStore.getSells().add(eachRi);
                        }
                    }

                }

                // Add StockSupplied Relationship (Dependant on prior data loading
                // of Organisation Entities)
                List<CountryOffice> countryOffices = (List<CountryOffice>) em.createNamedQuery("getAllCountryOffices").getResultList();
                List<ManufacturingFacility> mfs = (List<ManufacturingFacility>) em.createNamedQuery("getAllMFs").getResultList();

                for (CountryOffice eachCo : countryOffices) {
                    Set<Stock> stocksSold = new HashSet();

                    // Grab all that stores sell and put in a list
                    for (Store eachStore : eachCo.getStores()) {
                        stocksSold.addAll(eachStore.getSells());
                    }

                    // Assign every item sold to an mf
                    for (Stock eachStock : stocksSold) {
                        this.addStockSupplied(eachStock, eachCo, mfs.get(rand.nextInt(mfs.size())));
                    }
                }

                em.flush();
            } else {
                // Manual data loading for first system release
                this.addMaterial("Flathead Screw, Plus (5mm x 15mm)", 10);
                this.addMaterial("Cabinet Door Hinge 01, White", 62);
                this.addMaterial("Rosewood Plank (30mm x 5000mm x 1000mm)", 10000);
                this.addMaterial("Steel Knob 01", 55);

                FurnitureModel fm;
                BOM bom;
                List<BOMDetail> bomDetails = new ArrayList();

                fm = this.addFurnitureModel("Swivel Chair");
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Flathead Screw, Plus (5mm x 15mm)"), 4));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Plank (30mm x 5000mm x 1000mm)"), 1));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Coffee Table");
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Flathead Screw, Plus (5mm x 15mm)"), 6));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Plank (30mm x 5000mm x 1000mm)"), 1));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                fm = this.addFurnitureModel("Study Table - Dinosaur Edition");
                bom = new BOM();
                bomDetails.clear();
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Flathead Screw, Plus (5mm x 15mm)"), 6));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Rosewood Plank (30mm x 5000mm x 1000mm)"), 3));
                bomDetails.add(new BOMDetail(bom, QueryMethods.findMaterialByName(em, "Steel Knob 01"), 3));
                bom.setBomDetails(bomDetails);
                fm.setBom(bom);

                Set<RetailItem> retailItems = new HashSet();
                
                retailItems.add(this.addRetailItem("Pisang Goreng - Original - Regular"));
                retailItems.add(this.addRetailItem("Pisang Goreng - Lightly Salted - Regular"));
                retailItems.add(this.addRetailItem("Merlion Key Chain 01"));
                retailItems.add(this.addRetailItem("Orchid Paper Weight 01"));
                retailItems.add(this.addRetailItem("Tote Bag - Esplanade Design"));
                retailItems.add(this.addRetailItem("Durian Crisp - Regular"));
                retailItems.add(this.addRetailItem("Sambal Cheese Stick - Regular"));

                Store store;
                store = (Store) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Singapore"), "Alexandra");
                store.getSells().add(QueryMethods.findFurnitureByName(em, "Swivel Chair"));
                store.getSells().add(QueryMethods.findFurnitureByName(em, "Coffee Table"));
                store.getSells().add(QueryMethods.findFurnitureByName(em, "Study Table - Dinosaur Edition"));
                store.getSells().addAll(retailItems);
                
                store=(Store) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Singapore"), "Tampines");
                store.getSells().add(QueryMethods.findFurnitureByName(em, "Swivel Chair"));
                store.getSells().add(QueryMethods.findFurnitureByName(em, "Coffee Table"));
                store.getSells().add(QueryMethods.findFurnitureByName(em, "Study Table - Dinosaur Edition"));
                store.getSells().addAll(retailItems);
                
                store=(Store) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Malaysia"), "Johor Bahru - Kulai");
                store.getSells().add(QueryMethods.findFurnitureByName(em, "Swivel Chair"));
                store.getSells().add(QueryMethods.findFurnitureByName(em, "Coffee Table"));
                store.getSells().addAll(retailItems);

                List<CountryOffice> countryOffices = new ArrayList();
                countryOffices.add((CountryOffice) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Singapore"), "Singapore"));
                countryOffices.add((CountryOffice) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Malaysia"), "Malaysia"));

                ManufacturingFacility mf = (ManufacturingFacility) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Singapore"), "Tuas");

                for (CountryOffice eachCo : countryOffices) {
                    Set<Stock> stocksSold = new HashSet();

                    // Grab all that stores sell and put in a list (with dupes)
                    for (Store eachStore : eachCo.getStores()) {
                        stocksSold.addAll(eachStore.getSells());
                    }

                    // Assign every item sold to an mf
                    for (Stock eachStock : stocksSold) {
                        this.addStockSupplied(eachStock, eachCo, mf);
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
