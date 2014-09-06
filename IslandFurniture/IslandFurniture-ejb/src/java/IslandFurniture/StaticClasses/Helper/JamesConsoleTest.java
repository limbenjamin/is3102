/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanning;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.omg.CORBA.PRIVATE_MEMBER;

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
