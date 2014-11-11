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
import IslandFurniture.Entities.ProcuredStockContract;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.ProcuredStockSupplier;
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

    private ProcuredStockContractDetail addProcurementContractDetail(ProcuredStockContract pc, ProcuredStock procuredStock, ManufacturingFacility mf, int leadTime, int lotSize, double lotPrice) {
        ProcuredStockContractDetail pcd = new ProcuredStockContractDetail();
        pcd.setProcuredStock(procuredStock);
        pcd.setSupplierFor(mf);
        pcd.setLeadTimeInDays(leadTime);
        pcd.setLotSize(lotSize);
        pcd.setProcurementContract(pc);
        pcd.setLotPrice(lotPrice);
        pcd.setCurrency(mf.getCountry().getCurrency());
        em.persist(pcd);
        
        return pcd;
    }

    private ProcuredStockSupplier addSupplier(String name, Country country, String email, String phoneNo) {
        ProcuredStockSupplier supplier = QueryMethods.findSupplierByName(em, name);

        if (supplier == null) {
            supplier = new ProcuredStockSupplier();
            supplier.setName(name);
            supplier.setCountry(country);
            supplier.setEmail(email);
            supplier.setPhoneNumber(phoneNo);
            ProcuredStockContract pc = new ProcuredStockContract();
            pc.setSupplier(supplier);
            pc.setProcuredStockContractDetails(new ArrayList<>());
            supplier.setProcuredStockContract(pc);
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
            this.addSupplier("Global Supplies Inc.", country, "dave@globalsupplies.com", "(+65) 6345 9743");
            this.addSupplier("Woodcraft Solutions", country, "woody@woods.com", "(+65) 6327 2621");
            this.addSupplier("Metalworks", country, "tinman@metal.net", "(+65) 6351 9942");
            this.addSupplier("Quality Finest", country, "mickey@qualityfinest.com", "(+65) 6742 8746");
            this.addSupplier("Nippon Colours", country, "relations@nippon.com", "(+65) 6354 8732");
            this.addSupplier("Atlantic Co.", country, "ralph@atlantic.com.sg", "(+65) 6736 8273");
            this.addSupplier("Speed Manufacture", country, "gary@speedmanu.com", "(+65) 6534 9843");

            List<ManufacturingFacility> mfList = em.createNamedQuery("getAllMFs").getResultList();
            List<ProcuredStockSupplier> suppliers = em.createNamedQuery("getAllSuppliers").getResultList();
            FurnitureModel fm;

            ProcuredStockContractDetail pcd;
            List<ProcuredStockContractDetail> pcdList;

            // Assign random supplier to each material/retail item required at each mf
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
                    ProcuredStockContract pc = suppliers.get(rand.nextInt(suppliers.size())).getProcuredStockContract();
                    pcd = this.addProcurementContractDetail(pc, eachStock, mf, rand.nextInt(7) + 4, (rand.nextInt(5) + 1) * 100, rand.nextInt(9000)/100.0 + 10);
                    pcdList.add(pcd);

                    pc.getProcuredStockContractDetails().add(pcd);
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
