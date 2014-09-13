/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Store;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageOrganizationalHierarchyBeanLocal {

    CountryOffice addCountryOffice(String coName, Country country);

    ManufacturingFacility addManufacturingFacility(String mfName, Country country);

    Store addStore(String storeName, Country country);

    void deleteCountryOffice(Long coId);

    void deleteManufacturingFacility(Long mfId);

    void deleteStore(Long storeId);

    List<CountryOffice> displayCountryOffice();

    List<ManufacturingFacility> displayManufacturingFacility();

    List<Store> displayStore();

    void editCountryOffice(Long coId, String name, Country country);

    void editManufacturingFacility(Long mfId, String name, Country country);

    void editStore(Long storeId, String name, Country country);

    Country findCountryByName(String countryName);

    Plant findPlantByName(Country country, String plantName);

    List<Country> getCountries();
    
}
