/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import java.util.ArrayList;
import java.util.HashMap;
import javax.ejb.Local;

/**
 *
 * @author James
 */
@Local
public interface ManageProductionPlanningLocalInterface {

    public HashMap<String, String>
            getAuthorizedMF(String AUTH);

    Object getDemandPlanningTable(String MF) throws Exception;

    boolean changeMPP(String mppID, int newValue);

    boolean updateListOfEntities(ArrayList<Object> listOfEntities) throws Exception;

    Object getCapacityList(String MF);
    
    Object getWeeklyPlans(String period,String MFNAME) throws Exception;
    
    Object getWeeklyMRPTable(String period, String MFNAME) throws Exception;
    
    void addOneMonth();
    void minusOneMonth();
}
