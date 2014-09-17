/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

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
}
