/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.ACRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author James
 */
@Local
public interface basketAnalysisBeanLocal {

    List<FurnitureModel> findListOfrelated(FurnitureModel fm, CountryOffice co);

    List<FurnitureModel> findListOfrelated(String fm, String co);
    
}
