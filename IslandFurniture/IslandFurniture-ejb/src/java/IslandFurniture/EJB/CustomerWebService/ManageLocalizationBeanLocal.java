/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.CountryOffice;
import javax.ejb.Local;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Local
public interface ManageLocalizationBeanLocal {

    boolean isValidCoCode(String coCode);
    
    CountryOffice findCoByCode(String coCode);
    
}
