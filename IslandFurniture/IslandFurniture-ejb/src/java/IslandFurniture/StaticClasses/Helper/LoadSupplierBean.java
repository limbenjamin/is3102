/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.BOMDetail;
import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Material;
import IslandFurniture.EJB.Entities.ProcurementContract;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Supplier;
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
public class LoadSupplierBean implements LoadSupplierBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private Supplier addSupplier(String name, Country country) {
        Supplier supplier = QueryMethods.findSupplierByName(em, name);

        if (supplier == null) {
            supplier = new Supplier();
            supplier.setName(name);
            supplier.setCountry(country);
            ProcurementContract pc = new ProcurementContract();
            pc.setSupplier(supplier);
            supplier.setProcurementContract(pc);
            em.persist(supplier);

            return supplier;
        } else {
            System.out.println("Supplier \"" + name + "\" already exists");

            return null;
        }
    }

    @Override
    public boolean loadSampleData() {
        try {
            // Initiate Random
            Random rand = new Random();

            Country country = QueryMethods.findCountryByName(em, "Singapore");
            this.addSupplier("Global Supplies Inc.", country);
            this.addSupplier("Woodcraft Solutions", country);
            this.addSupplier("Metalworks", country);
            this.addSupplier("Quality Finest", country);
            this.addSupplier("Nippon Colours", country);
            this.addSupplier("Atlantic Co.", country);
            this.addSupplier("Speed Manufacture", country);

            List<ManufacturingFacility> mfList = em.createNamedQuery("getAllMFs").getResultList();
            List<Supplier> suppliers = em.createNamedQuery("getAllSuppliers").getResultList();
            FurnitureModel fm;

            ProcurementContractDetail pcd;
            List<ProcurementContractDetail> pcdList;

            for (ManufacturingFacility mf : mfList) {
                Set<Material> reqMaterials = new HashSet();
                pcdList = new ArrayList();

                for (StockSupplied ss : mf.getSupplyingWhatTo()) {
                    if (ss.getStock() instanceof FurnitureModel) {
                        fm = (FurnitureModel) ss.getStock();

                        for (BOMDetail bomd : fm.getBom().getBomDetails()) {
                            reqMaterials.add(bomd.getMaterial());
                        }
                    }
                }

                for (Material eachMat : reqMaterials) {
                    pcd = new ProcurementContractDetail();
                    pcd.setProcuredStock(eachMat);
                    pcd.setSupplierFor(mf);
                    pcd.setLeadTimeInDays(rand.nextInt(7) + 4);
                    pcd.setLotSize((rand.nextInt(5) + 1) * 100);
                    em.persist(pcd);

                    pcdList.add(pcd);

                    suppliers.get(rand.nextInt(suppliers.size())).getProcurementContract().setProcurementContractDetails(pcdList);
                }

                mf.setSuppliedBy(pcdList);
            }

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false;
        }

    }

}
