/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.Supplier;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface SupplierManagerLocal {

    public List<Supplier> displaySupplierList();
    
    public Supplier getSupplier(Long supplierId);

    public Supplier addSupplier(String supplierName, String countryName);

    public void editSupplier(Long id, String name, String countryName);

    public void deleteSupplier(Long id);

    public List<ProcurementContractDetail> displayProcurementContractDetails(String supplierID);

    public List<ProcuredStock> displayProcuredStock();

    public List<ManufacturingFacility> displayManufacturingFacility();

    public void deleteProcurementContractDetail(Long id, Long supplierID);

    public void addProcurementContractDetail(Long supplierID, Long mfID, Long stockID, Integer size, Integer leadTime);

    public void editProcurementContractDetail(Long id, Integer size, Integer leadTime);
    
}
