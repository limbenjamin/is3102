/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author James
 */
@Local
public interface UtilityBeanLocal {

    Staff getStaffByUsername(String username);

    CountryOffice getCountryOffice(Plant plant);
    
    public List<MembershipTier> getAllTiers();
    
}
