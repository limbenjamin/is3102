/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurnitures.DataStructures.JDataTable;
import java.util.HashMap;
import javax.ejb.Remote;

/**
 *
 * @author James
 */
@Remote
public interface MaterialResourcePlanningView {

    
        public HashMap<String, String>
            getAuthorizedMF(String AUTH);
        JDataTable<String> getDemandPlanningTable(String MF);
        boolean changeMPP(String mppID, int newValue);
}
