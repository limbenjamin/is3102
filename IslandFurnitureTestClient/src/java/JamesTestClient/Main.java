/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JamesTestClient;

import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
import IslandFurniture.StaticClasses.Helper.LoadJamesTestDataRemote;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author James Will be deleted
 */
public class Main {


    @EJB
    private static ManageProductionPlanningRemote MPP;
    
   @EJB
    private static LoadJamesTestDataRemote loadJamesTestData;

    public static void main(String[] args) {
        
        //loadJamesTestData.loadMSSRS();
        
    //MPP.setMF("SINGAPORE");
        try {
            MPP.CreateProductionPlanFromForecast();
            System.out.println("SUCCESS !");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   
    }
    


}
