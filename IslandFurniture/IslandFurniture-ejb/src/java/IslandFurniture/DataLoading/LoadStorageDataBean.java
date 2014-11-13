/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Enums.StorageAreaType;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.Entities.StorefrontInventory;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.StaticClasses.QueryMethods;
import java.util.ArrayList;
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
public class LoadStorageDataBean implements LoadStorageDataBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private StorageArea addStorageArea(String name, Plant plant, StorageAreaType type) {
        StorageArea sa = QueryMethods.findStorageAreaByName(em, name, plant);

        if (sa == null) {
            sa = new StorageArea();
            sa.setName(name);
            sa.setPlant(plant);
            sa.setType(type);
            em.persist(sa);

            return sa;
        } else {
            System.out.println("Storage Area \"" + name + "\" already exists");

            return null;
        }
    }

    private StorageBin addStorageBin(String name, StorageArea sa) {
        StorageBin sb = QueryMethods.findStorageBinByName(em, name, sa);

        if (sb == null) {
            sb = new StorageBin();
            sb.setName(name);
            sb.setStorageArea(sa);
            em.persist(sb);

            return sb;
        } else {
            System.out.println("Storage Bin \"" + name + "\" already exists");

            return null;
        }
    }

    @Override
    public boolean loadSampleData() {
        Random rand = new Random(1);
        
        List<Plant> plants = em.createNamedQuery("getAllPlants").getResultList();
        StorageArea sa;
        List<StorageArea> saList;
        StorageBin sb;
        List<StorageBin> sbList;
        
        StoreSection storeSect;
        StorefrontInventory storeInv;
        List<StorefrontInventory> storeInvList;

        for (Plant plant : plants) {
            if (plant instanceof Store || plant instanceof ManufacturingFacility || plant instanceof CountryOffice) {
                saList = new ArrayList();

                sa = this.addStorageArea("Receiving Area", plant, StorageAreaType.RECEIVING);
                saList.add(sa);

                sbList = new ArrayList();
                // Add Storage Bin
                for (int i = 0; i < 5; i++) {
                    sb = this.addStorageBin("RB" + i, sa);
                    sbList.add(sb);
                }
                sa.setStorageBins(sbList);

                sa = this.addStorageArea("Shipping Area", plant, StorageAreaType.SHIPPING);
                saList.add(sa);

                sbList = new ArrayList();
                // Add Storage Bin
                for (int i = 0; i < 5; i++) {
                    sb = this.addStorageBin("HB" + i, sa);
                    sbList.add(sb);
                }
                sa.setStorageBins(sbList);

                sa = this.addStorageArea("Storage Area", plant, StorageAreaType.STORAGE);
                saList.add(sa);

                sbList = new ArrayList();
                // Add Storage Bin
                for (int i = 0; i < 20; i++) {
                    sb = this.addStorageBin("SB" + i, sa);
                    sbList.add(sb);
                }
                sa.setStorageBins(sbList);

                if (plant instanceof ManufacturingFacility) {
                    sa = this.addStorageArea("Production Area", plant, StorageAreaType.PRODUCTION);
                    saList.add(sa);

                    sbList = new ArrayList();
                    // Add Storage Bin
                    for (int i = 0; i < 5; i++) {
                        sb = this.addStorageBin("PB" + i, sa);
                        sbList.add(sb);
                    }
                    sa.setStorageBins(sbList);
                }

                plant.setStorageAreas(saList);
                
                // Populate Storefront Inventories and Sections
                if (plant instanceof Store) {
                    // Create Store Sections
                    storeSect = new StoreSection();
                    storeSect.setName("Storefront Warehouse");
                    storeSect.setStore((Store) plant);
                    storeSect.setStoreLevel(1);
                    storeSect.setDescription("The warehouse area accessible to the public for self-service furniture retrieval.");
                    em.persist(storeSect);
                    ((Store) plant).getStoreSections().add(storeSect);

                    // Create storefront Inventories
                    storeInvList = new ArrayList();
                    for (StockSupplied ss : ((Store) plant).getCountryOffice().getSuppliedWithFrom()) {
                        int maxQty = rand.nextInt(11) * 10 + 50;
                        storeInv = new StorefrontInventory();
                        storeInv.setStock(ss.getStock());
                        storeInv.setStore((Store) plant);
                        storeInv.setMaxQty(maxQty);
                        storeInv.setRepQty((int) Math.floor(maxQty * 0.3));
                        storeInv.setQty((int) Math.floor(maxQty * (rand.nextInt(5) * 0.1 + 0.1)));
                        storeInv.setLocationInStore(storeSect);
                        em.persist(storeInv);

                        ((Store) plant).getStorefrontInventories().add(storeInv);
                        storeInvList.add(storeInv);
                    }
                    storeSect.setStorefrontInventories(storeInvList);

                    // Create the rest of the Store Sections
                    for (FurnitureCategory fCat : FurnitureCategory.values()) {
                        storeSect = new StoreSection();
                        storeSect.setName(fCat.toString().substring(0, 1) + fCat.toString().toLowerCase().substring(1));
                        storeSect.setStore((Store) plant);
                        storeSect.setStoreLevel(1);
                        storeSect.setDescription("Area of the store containing " + fCat.toString().toLowerCase() + " products.");
                        em.persist(storeSect);
                        ((Store) plant).getStoreSections().add(storeSect);
                    }
                }
            }
        }

        return true;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
