package IslandFurniture.Test.Suite;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Benjamin
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({IslandFurniture.Test.Purchasing.ManagePurchaseOrderRemoteTest.class,
                    IslandFurniture.Test.Purchasing.SupplierManagerRemoteTest.class,
                    IslandFurniture.Test.ITManagement.ManageOrganizationalHierarchyBeanRemoteTest.class,
                    IslandFurniture.Test.ITManagement.ManagePrivilegesBeanRemoteTest.class,
                    IslandFurniture.Test.ITManagement.ManageRolesBeanRemoteTest.class,
                    IslandFurniture.Test.ITManagement.ManageStaffAccountsBeanRemoteTest.class,
                    IslandFurniture.Test.ITManagement.ManageSystemAuditLogBeanRemoteTest.class,
                    IslandFurniture.Test.CommonInfrastructure.ManageAnnouncementsBeanRemoteTest.class,
                    IslandFurniture.Test.CommonInfrastructure.ManageEventsBeanRemoteTest.class,
                    IslandFurniture.Test.CommonInfrastructure.ManageAuthenticationBeanRemoteTest.class,
                    IslandFurniture.Test.CommonInfrastructure.ManageMessagesBeanRemoteTest.class,
                    IslandFurniture.Test.CommonInfrastructure.ManageNotificationsBeanRemoteTest.class,
                    IslandFurniture.Test.CommonInfrastructure.ManageTodoBeanRemoteTest.class,
                    IslandFurniture.Test.CommonInfrastructure.ManageUserAccountBeanRemoteTest.class
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
