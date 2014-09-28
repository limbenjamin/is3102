/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.BOMDetail;
import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.ProcurementContract;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.StockSuppliedPK;
import IslandFurniture.EJB.Entities.Supplier;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findCountryByName;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findPCDByStockAndMF;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findPCDByStockMFAndSupplier;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findSupplierByName;
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
    public String addSupplier(String supplierName, String countryName, String phoneNo, String email) {
        Country country;
        Supplier supplier;
        ProcurementContract pc;
        List<ProcurementContractDetail> pcdList;
        try {
            System.out.println("SupplierManager.addSupplier()");
            country = findCountryByName(em, countryName);
            supplier = findSupplierByName(em, supplierName);
            if(supplier != null) {
                System.out.println("Supplier " + supplierName + " already exists");
                return "" + supplier.getId() + "#Supplier \"" + supplierName + "\" already exists in database. Redirect to Procurement Contract";
            }
             
            supplier = new Supplier();
            pc = new ProcurementContract();
            pcdList = new ArrayList<ProcurementContractDetail>();
            
            pc.setSupplier(supplier);
            pc.setProcurementContractDetails(pcdList);
            
            supplier.setName(supplierName);
            supplier.setCountry(country);
            supplier.setProcurementContract(pc);
            supplier.setEmail(email);
            supplier.setPhoneNumber(phoneNo);
            
            em.persist(supplier);
            return "" + supplier.getId() + "#0";
        } catch(NoResultException NRE) {
            System.err.println("No records found");
            return null;
        }
    }
    public boolean editSupplier(Long id, String name, String countryName, String phoneNumber, String email) {
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
            if(phoneNumber != null)
                supplier.setPhoneNumber(phoneNumber);
            if(email != null)
                supplier.setEmail(email);
            em.persist(supplier);
            return true;
        } catch(Exception ex) {
            System.out.println("Something went wrong");
            return false;
        }
    }
    public String deleteSupplier(Long id) {
        Supplier supplier = null;
        List<PurchaseOrder> poList;
        try {
            System.out.println("SupplierManager.deleteSupplier()");
            poList = em.createNamedQuery("getAllPurchaseOrders", PurchaseOrder.class).getResultList();
            for(int i=0; i<poList.size(); i++) {
                if(poList.get(i).getSupplier().getId().equals(id)) {
                    supplier = poList.get(i).getSupplier();
                    break;
                }
            }
            if(supplier != null) {
                System.out.println("Existing purchase order linked to Supplier. Unable to delete");
                return "Invalid deletion due to existing purchase order linked to Supplier \"" + supplier.getName() + "\"";
            }
            else { 
                supplier = em.find(Supplier.class, id);
                for(int i=0; i<supplier.getProcurementContract().getProcurementContractDetails().size(); i++) 
                    em.remove(supplier.getProcurementContract().getProcurementContractDetails().get(i));
                System.out.println("Removed PCD"); 
                em.remove(supplier.getProcurementContract());
                System.out.println("Removed PC");
                em.remove(supplier);
                System.out.println("Removed supplier");
                em.flush();
                return null;
            }
        } catch(Exception ex) {
            System.err.println("Existing purchase order linked Supplier. Unable to delete");
            return "Unexpected error occured";
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
    public String deleteProcurementContractDetail(Long id, Long supplierID) {
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
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong");
            return "Unexpected error occured";
        }
    }
    public String addProcurementContractDetail(Long supplierID, Long mfID, Long stockID, Integer size, Integer leadTime) {
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
            
            pcd = findPCDByStockMFAndSupplier(em, stock, mf, supplier); 
            
            if(pcd != null) {
                System.out.println("ProcurementContractDetail already exist");
                return "Procurement Contract Detail already exist";
            }
            else {            
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
                return null;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    public String editProcurementContractDetail(Long id, Integer size, Integer leadTime) {
        ProcurementContractDetail pcd;
        try {
            System.out.println("SupplierManager.editProcurementContractDetails()");
            pcd = em.find(ProcurementContractDetail.class, id);
            pcd.setLeadTimeInDays(leadTime);
            pcd.setLotSize(size);
            em.persist(pcd);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
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
    public String deleteStockSupplyRequest(Long stockID, Long mfID, Long countryID) {
        StockSuppliedPK pk;
        StockSupplied ss;
        ManufacturingFacility mf;
        CountryOffice co;
        try {
            System.out.println("SupplierManager.deleteStockSupplyRequest()");
            pk = new StockSuppliedPK(stockID, countryID, mfID);
            ss = em.find(StockSupplied.class, pk);
            if(ss == null)
                System.out.println("StockSupplied is null");
            mf = em.find(ManufacturingFacility.class, mfID); 
            co = em.find(CountryOffice.class, countryID);
            
            em.remove(ss);
            System.out.println("Successfully deleted Stock Supply Request");
            mf.getSupplyingWhatTo().remove(ss);
            co.getSuppliedWithFrom().remove(ss);
            
            em.flush();
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    public String addStockSupplyRequest(Long stockID, Long mfID, Long countryID) {
        Stock stock;
        ManufacturingFacility mf;
        CountryOffice co;
        StockSupplied ss;
        StockSuppliedPK pk;
        try {
            System.out.println("SupplierManager.addStockSupplyRequest()");
            pk = new StockSuppliedPK(stockID, countryID, mfID);
            ss = em.find(StockSupplied.class, pk);
            if(ss != null) {
                System.out.println("Request already exists");
                return "Stock Supply Request already exists";
            }
            else {     
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
                return null;
            }
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
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
        List<Stock> tempList;
        ManufacturingFacility mf;
        try{
            System.out.println("SupplierManager.getListOfStock()");
            tempList = em.createNamedQuery("getAllStock", Stock.class).getResultList();
            stockList = new ArrayList<Stock>(); 
            for(int i=0; i<tempList.size(); i++) {
                if(tempList.get(i) instanceof RetailItem || tempList.get(i) instanceof FurnitureModel)
                    stockList.add(tempList.get(i));
            }
            return stockList;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
    public List<Stock> checkForValidPCD(Long stockID, Long mfID) {
        Stock stock;
        ManufacturingFacility mf;
        List<Stock> pcdList = new ArrayList<Stock>();
        FurnitureModel fm;
        List<BOMDetail> bomList;
        try {
            stock = em.find(Stock.class, stockID);
            mf = em.find(ManufacturingFacility.class, mfID);
            System.out.println("Checking for existence of ProcurementContractDetail for " + stock.getName() + " in " + mf.getName());
            if(stock instanceof RetailItem) {
                if(checkForPCD((ProcuredStock)stock, mf)) {
                    System.out.println(mf.getName() + " carries " + stock.getName());
                    return null;
                } else {
                    System.out.println(mf.getName() + " doesn't carry Retail Item " + stock.getName());
                    pcdList.add(stock);
                    return pcdList;
                }
            } else if(stock instanceof FurnitureModel) {
                fm = (FurnitureModel)stock;
                bomList = fm.getBom().getBomDetails();
                for(int i=0; i<bomList.size(); i++) {
                    if(!checkForPCD((ProcuredStock)(bomList.get(i).getMaterial()), mf)) {
                        System.out.println(mf.getName() + " doesn't carry Material " + bomList.get(i).getMaterial().getName());
                        pcdList.add(bomList.get(i).getMaterial());
                    }
                }
                if(pcdList.size() > 0)
                    return pcdList;
                else 
                    return null;
            } else 
                return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
    public boolean checkForPCD(ProcuredStock stock, ManufacturingFacility mf) {
        List<ProcurementContractDetail> pcdList;
        try {
            pcdList = findPCDByStockAndMF(em, stock, mf);
            if(pcdList.size() < 1) {
                return false;
            } else
                return true;
            
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return false;
        }
    }
}
