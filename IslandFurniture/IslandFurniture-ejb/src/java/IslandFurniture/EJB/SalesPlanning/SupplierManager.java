/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.ProcurementContract;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.Supplier;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findCountryByName;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774
 */
@Stateful
public class SupplierManager implements SupplierManagerLocal {

    @PersistenceContext 
    EntityManager em;
    
    private Supplier supplier;
    
    public List<Supplier> displaySupplierList() {
        List<Supplier> supplierList;
        try {
            supplierList = em.createNamedQuery("getAllSuppliers", Supplier.class).getResultList();
            return supplierList;            
        } catch(NoResultException NRE) {
            System.err.println("No results found");
            return null;
        }
    }
    public Supplier getSupplier(Long supplierId) {
        supplier = (Supplier) em.find(Supplier.class, supplierId);
        return supplier;
    }
    public void addSupplier(String supplierName, String countryName) {
        Country country;
        Supplier supplier;
        try {
            System.out.println("SupplierManager.addSupplier()");
            country = findCountryByName(em, countryName);
            supplier = new Supplier();
            supplier.setName(supplierName);
            supplier.setCountry(country);
            em.persist(supplier);
        } catch(NoResultException NRE) {
            System.err.println("No records found");
        }
    }
    public void editSupplier(Long id, String name, String countryName) {
        Supplier supplier;
        Country country;
        try {
            System.out.println("SupplierManager.editSupplier()");
            supplier = em.find(Supplier.class, id);
            country = findCountryByName(em, countryName);
            if(name != null)
                supplier.setName(name);
            if(countryName != null) 
                supplier.setCountry(country);
            em.persist(supplier);
        } catch(Exception ex) {
            System.out.println("Something went wrong");
        }
    }
    public void deleteSupplier(Long id) {
        Supplier supplier;
        try {
            System.out.println("SupplierManager.deleteSupplier()");
            supplier = em.find(Supplier.class, id);
            System.out.println("Don't forget to mind the constraints. I'm gonna just delete for now");
            em.remove(supplier);
            em.flush();
        } catch(Exception ex) {
            System.err.println("Something went wrong");
        }
    }
    public List<ProcurementContractDetail> displayProcurementContractDetails(String supplierID) {
        Supplier supplier;
        List<ProcurementContractDetail> detailList;
        try {
            System.out.println("SupplierManager.displayProcurementContractDetails()");
            supplier = em.find(Supplier.class, Long.parseLong(supplierID));
            System.out.println(supplier.getName());
            detailList = supplier.getProcurementContract().getProcurementContractDetails();
            return detailList;
        } catch(Exception ex) {
            System.err.println("Something went wrong");
            return null;
        }
    }
    public List<ProcuredStock> displayProcuredStock() {
        List<ProcuredStock> stockList;
        try {
            System.out.println("SupplierManager.displayProcuredStock()");
            stockList = em.createNamedQuery("getAllProcuredStock", ProcuredStock.class).getResultList();
            return stockList;
        } catch(Exception ex) {
            System.err.println("Something went wrong, most likely because search result is null");
            return null;
        }
    }
    public List<ManufacturingFacility> displayManufacturingFacility() {
        List<ManufacturingFacility> mfList;
        try {
            System.out.println("SupplierManager.displayManufacturingFacility()");
            mfList = em.createNamedQuery("getAllMFs", ManufacturingFacility.class).getResultList();
            return mfList;
        } catch(Exception ex) {
            System.err.println("Something went wrong, most likely because search result is null");
            return null;   
        }
    }
    public void deleteProcurementContractDetail(Long id) {
        ProcurementContractDetail pcd;
        try {
            System.out.println("SupplierManager.deleteProcurementContractDetails()");
            pcd = em.find(ProcurementContractDetail.class, id);
            System.out.println("Don't forget to check for constraints here. Gonna just delete for now");
            em.remove(pcd);
            em.flush();
        } catch(Exception ex) {
            System.err.println("Something went wrong");
        }
    }
    public void addProcurementContractDetail(Long supplierID, Long mfID, Long stockID) {
        Supplier supplier;
        ManufacturingFacility mf;
        ProcuredStock stock;
        ProcurementContract pc;
        List<ProcurementContractDetail> pcdList;
        ProcurementContractDetail pcd;
        try {
            System.out.println("SupplierManager.addProcurementContractDetails()");
            mf = em.find(ManufacturingFacility.class, mfID);
            stock = em.find(ProcuredStock.class, stockID);
            supplier = em.find(Supplier.class, supplierID);
            pc = supplier.getProcurementContract();
            if(pc == null) {
                pc = new ProcurementContract();
                pcdList = new ArrayList<ProcurementContractDetail>();
                pc.setProcurementContractDetails(pcdList);
                pc.setSupplier(supplier);
                supplier.setProcurementContract(pc);
            }
            
            pcd = new ProcurementContractDetail();
            System.out.println("1");
            pcd.setProcuredStock(stock);
            System.out.println("2");
            pcd.setSupplierFor(mf);
            System.out.println("3");
            pcd.setProcurementContract(pc);
            System.out.println("4");
            
            pc.getProcurementContractDetails().add(pcd);
            System.out.println("5");
        } catch(Exception ex) {
            ex.printStackTrace();
            System.err.println("Something went wrong here");
        }
    }
}
