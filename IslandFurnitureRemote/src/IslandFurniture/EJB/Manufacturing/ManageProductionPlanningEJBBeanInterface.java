/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurnitures.DataStructures.JDataTable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author James
 */
@Local
public interface ManageProductionPlanningEJBBeanInterface {

    public HashMap<String, String>
            getAuthorizedMF(String AUTH);

    Object getDemandPlanningTable(String MF);

    boolean changeMPP(String mppID, int newValue);

    boolean updateListOfEntities(ArrayList<Object> listOfEntities) throws Exception;

    Object getCapacityList(String MF);
}
