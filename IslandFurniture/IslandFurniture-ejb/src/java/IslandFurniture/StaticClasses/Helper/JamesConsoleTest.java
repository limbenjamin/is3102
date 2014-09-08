/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Manufacturing.ManageProductionPlanning;
import javax.ejb.EJB;

/**
 *
 * @author James Will be deleted
 */
public class JamesConsoleTest {

    @EJB
    ManageProductionPlanning manager;

    @EJB
    JamesTestDataBean jt;

    public static void main(String[] args) {
        JamesConsoleTest test = new JamesConsoleTest();
        test.createdata();
    }
    
    public void createdata()
    {
        jt.createtestdata();
    }


}
