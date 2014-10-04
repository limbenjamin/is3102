/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.Entities.BOMDetail;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcurementContract;
import IslandFurniture.Entities.ProcurementContractDetail;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.Supplier;
import IslandFurniture.StaticClasses.QueryMethods;
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

    private ProcurementContractDetail addProcurementContractDetail(ProcurementContract pc, ProcuredStock procuredStock, ManufacturingFacility mf, int leadTime, int lotSize) {
        ProcurementContractDetail pcd = new ProcurementContractDetail();
        pcd.setProcuredStock(procuredStock);
        pcd.setSupplierFor(mf);
        pcd.setLeadTimeInDays(leadTime);
        pcd.setLotSize(lotSize);
        pcd.setProcurementContract(pc);
        em.persist(pcd);
        
        return pcd;
    }

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
            Random rand = new Random(1);

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
                Set<ProcuredStock> reqStock = new HashSet();
                pcdList = new ArrayList();

                for (StockSupplied ss : mf.getSupplyingWhatTo()) {
                    if (ss.getStock() instanceof FurnitureModel) {
                        fm = (FurnitureModel) ss.getStock();

                        for (BOMDetail bomd : fm.getBom().getBomDetails()) {
                            reqStock.add(bomd.getMaterial());
                        }
                    }
                    if (ss.getStock() instanceof RetailItem) {
                        reqStock.add((RetailItem) ss.getStock());
                    }
                }

                for (ProcuredStock eachStock : reqStock) {
                    ProcurementContract pc = suppliers.get(rand.nextInt(suppliers.size())).getProcurementContract();
                    pcd = this.addProcurementContractDetail(pc, eachStock, mf, rand.nextInt(7) + 4, (rand.nextInt(5) + 1) * 100);
                    pcdList.add(pcd);

                    pc.getProcurementContractDetails().add(pcd);
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
