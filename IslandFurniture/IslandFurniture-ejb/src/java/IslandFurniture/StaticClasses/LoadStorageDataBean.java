/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Enums.StorageAreaType;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.Entities.Store;
import IslandFurniture.StaticClasses.LoadStorageDataBeanRemote;
import java.util.ArrayList;
import java.util.List;
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
        List<Plant> plants = em.createNamedQuery("getAllPlants").getResultList();
        StorageArea sa;
        List<StorageArea> saList;
        StorageBin sb;
        List<StorageBin> sbList;

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
            }
        }

        return true;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
