/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.Purchasing;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Benjamin
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({IslandFurniture.Test.Purchasing.ManagePurchaseOrderRemoteTest.class,
                    IslandFurniture.Test.ITManagement.ManageOrganizationalHierarchyBeanRemoteTest.class
                    })

public class TestSuite {
    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    
    
    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    
    
    @Before
    public void setUp() throws Exception
    {
    }

    
    
    @After
    public void tearDown() throws Exception
    {
    }    
}
