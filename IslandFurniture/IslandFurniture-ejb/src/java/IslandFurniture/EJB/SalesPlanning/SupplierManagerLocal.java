/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

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

    public void addSupplier(String supplierName, String countryName);

    public void editSupplier(Long id, String name, String countryName);

    public void deleteSupplier(Long id);
    
}
