/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.CountryOffice;
import java.util.List;

/**
 *
 * @author Zee
 */
public interface ManageCountryBeanLocal {

    CountryOffice getCountry(Long id);
    
    List<CountryOffice> getCountries();
    
}
