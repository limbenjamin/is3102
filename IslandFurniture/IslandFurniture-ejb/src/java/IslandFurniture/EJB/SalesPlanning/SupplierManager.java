/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.ProcurementContract;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.StockSuppliedPK;
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
    public Supplier addSupplier(String supplierName, String countryName) {
        Country country;
        Supplier supplier;
        ProcurementContract pc;
        List<ProcurementContractDetail> pcdList;
        try {
            System.out.println("SupplierManager.addSupplier()");
            country = findCountryByName(em, countryName);
            supplier = new Supplier();
            pc = new ProcurementContract();
            pcdList = new ArrayList<ProcurementContractDetail>();
            
            pc.setSupplier(supplier);
            pc.setProcurementContractDetails(pcdList);
            
            supplier.setName(supplierName);
            supplier.setCountry(country);
            supplier.setProcurementContract(pc);
            
            em.persist(supplier);
            return supplier;
        } catch(NoResultException NRE) {
            System.err.println("No records found");
            return null;
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
            em.remove(supplier.getProcurementContract());
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
    public void deleteProcurementContractDetail(Long id, Long supplierID) {
        ProcurementContractDetail pcd;
        Supplier supplier;
        try {
            System.out.println("SupplierManager.deleteProcurementContractDetails()");
            pcd = em.find(ProcurementContractDetail.class, id);
            supplier = em.find(Supplier.class, supplierID);
            System.out.println("Don't forget to check for constraints here. Gonna just delete for now");
            supplier.getProcurementContract().getProcurementContractDetails().remove(pcd);
            em.remove(pcd);
            em.flush();
        } catch(Exception ex) {
            System.err.println("Something went wrong");
        }
    }
    public void addProcurementContractDetail(Long supplierID, Long mfID, Long stockID, Integer size, Integer leadTime) {
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
            System.out.println("MF is " + mf.getName() + ", Stock is " + stock.getName() + ". Supplier is " + supplier.getName());
            
            pc = supplier.getProcurementContract();
            if(pc == null) {
                pc = new ProcurementContract();
                pcdList = new ArrayList<ProcurementContractDetail>();
                pc.setProcurementContractDetails(pcdList);
                pc.setSupplier(supplier);
                supplier.setProcurementContract(pc);
            }
            
            pcd = new ProcurementContractDetail();
            pcd.setProcuredStock(stock);
            pcd.setSupplierFor(mf);
            pcd.setProcurementContract(pc);
            pcd.setLeadTimeInDays(leadTime);
            pcd.setLotSize(size);
            
            pc.getProcurementContractDetails().add(pcd);
            System.out.println("5");
        } catch(Exception ex) {
            ex.printStackTrace();
            System.err.println("Something went wrong here");
        }
    }
    public void editProcurementContractDetail(Long id, Integer size, Integer leadTime) {
        ProcurementContractDetail pcd;
        try {
            System.out.println("SupplierManager.editProcurementContractDetails()");
            pcd = em.find(ProcurementContractDetail.class, id);
            pcd.setLeadTimeInDays(leadTime);
            pcd.setLotSize(size);
            em.persist(pcd);
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
        }
    }
    public List<StockSupplied> getAllStockSupplied() {
        List<StockSupplied> stockSuppliedList;
        try {
            System.out.println("SupplierManager.getAllStockSupplied()");
            stockSuppliedList = em.createNamedQuery("getAllStockSupplied", StockSupplied.class).getResultList();
            return stockSuppliedList;
        } catch(NoResultException NRE) {
            System.err.println("Something went wrong. Most likely to result found");
            return null;
        }
    }
    public void deleteStockSupplyRequest(Long stockID, Long mfID, Long countryID) {
        StockSuppliedPK pk;
        StockSupplied ss;
        ManufacturingFacility mf;
        CountryOffice co;
        try {
            System.out.println("SupplierManager.deleteStockSupplyRequest()");
            pk = new StockSuppliedPK(stockID, countryID, mfID);
            ss = em.find(StockSupplied.class, pk);
            System.out.println("Found StockSupplied");
            mf = em.find(ManufacturingFacility.class, mfID);
            System.out.println("1");
            co = em.find(CountryOffice.class, countryID);
            System.out.println("2");
            
            mf.getSupplyingWhatTo().remove(ss);
            System.out.println("3");
            co.getSuppliedWithFrom().remove(ss);
            System.out.println("4");
            
            em.remove(ss);
            System.out.println("5");
            em.flush();
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
        }
    }
    public void addStockSupplyRequest(Long stockID, Long mfID, Long countryID) {
        Stock stock;
        ManufacturingFacility mf;
        CountryOffice co;
        StockSupplied ss;
        try {
            System.out.println("SupplierManager.addStockSupplyRequest()");
            ss = new StockSupplied();
            stock = em.find(Stock.class, stockID);
            mf = em.find(ManufacturingFacility.class, mfID);
            co = em.find(CountryOffice.class, countryID);
            
            ss.setCountryOffice(co);
            ss.setManufacturingFacility(mf);
            ss.setStock(stock);
            
            mf.getSupplyingWhatTo().add(ss);
            co.getSuppliedWithFrom().add(ss);
            
            em.persist(ss);
            em.flush();
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
        }
    }
    public List<CountryOffice> getListOfCountryOffice() {
        List<CountryOffice> COList; 
        try {
            System.out.println("SupplierManager.getListOfCountryOffice()");
            COList = em.createNamedQuery("getAllCountryOffices", CountryOffice.class).getResultList();
            return COList;
        } catch(NoResultException NRE) {
            System.err.println("Something went wrong");
            return null;
        }
    }
    public List<ManufacturingFacility> getListOfMF() {
        List<ManufacturingFacility> MFList;
        try {
            System.out.println("SupplierManager.getListOfMF()");
            MFList = em.createNamedQuery("getAllMFs", ManufacturingFacility.class).getResultList();
            return MFList;
        } catch(NoResultException NRE) {
            System.err.println("Something went wrong");
            return null;
        }
    }
    public List<Stock> getListOfStock() {
        List<Stock> stockList;
        ManufacturingFacility mf;
        try{
            System.out.println("SupplierManager.getListOfStock()");
            stockList = em.createNamedQuery("getAllStock", Stock.class).getResultList();
            return stockList;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
}
