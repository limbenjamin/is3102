/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Purchasing;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Currency;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface SupplierManagerLocal {

    public List<ProcuredStockSupplier> displaySupplierList();
    
    public ProcuredStockSupplier getSupplier(Long supplierId);

    public String deleteSupplier(Long id);

    public List<ProcuredStockContractDetail> displayProcurementContractDetails(String supplierID);

    public List<ProcuredStock> displayProcuredStock();

    public List<ManufacturingFacility> displayManufacturingFacility();

    public String deleteProcurementContractDetail(Long id, Long supplierID);

    public String addProcurementContractDetail(Long supplierID, Long mfID, Long stockID, Integer size, Integer leadTime, Double lotPrice, Long currencyID); 

    public String editProcurementContractDetail(Long id, Integer size, Integer leadTime, Double lotPrice, Long currencyID);

    public List<StockSupplied> getAllStockSupplied();

    public String deleteStockSupplyRequest(Long stockID, Long mfID, Long countryID);

    public List<CountryOffice> getListOfCountryOffice();

    public List<ManufacturingFacility> getListOfMF();

    public String addStockSupplyRequest(Long stockID, Long mfID, Long countryID);

    public List<Stock> getListOfStock();

    public boolean editSupplier(Long id, String name, String countryName, String phoneNumber, String email);

    public String addSupplier(String supplierName, String countryName, String phoneNo, String email);

    public List<Stock> checkForValidPCD(Long stockID, Long mfID);
    
}
