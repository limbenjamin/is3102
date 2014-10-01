/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.CommonInfrastructure.ManageMessagesBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateless
public class MapStaffDataBean implements MapStaffDataBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @EJB
    private ManageStaffAccountsBeanLocal msal;
    @EJB
    private ManageRolesBeanLocal mrbl;
    
    //for first system release, preload robert with 2 empty threads
    @EJB
    private ManageMessagesBeanLocal msgbean;
    
    Country country;
    Store store;
    Staff staff;
    
    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {

        String[][] arr = new String [][] { 
            { "marilyn", "pass", "Marilyn T. Haddock", "MarilynTHaddock@teleworm.us", "513-355-4354","Laos","Vientiane"},
            { "russell", "pass", "Russell C. Carter", "RussellCCarter@teleworm.us", "386-698-7475","Thailand","Bangkok - Ma Boon Krong"},
            { "michael", "pass", "Michael J. Mayhall", "MichaelJMayhall@rhyta.com", "978-365-1346","Indonesia","Sukabumi"},
            { "rose", "pass", "Rose S. Cruz", "RoseSCruz@rhyta.com", "979-587-1564","Singapore","Alexandra"},
            { "deanna", "pass", "Deanna E. Astudillo", "DeannaEAstudillo@jourrapide.com", "760-734-2536","Singapore","Tampines"},
            { "nat", "pass", "Nathanael C. Foster", "NathanaelCFoster@rhyta.com", "843-529-4635","Singapore","Tuas"},
            { "leslie", "pass", "Leslie D. Cain", "LeslieDCain@teleworm.us", "413-675-8446","Malaysia","Johor Bahru - Kulai"},
            { "jonathan", "pass", "Jonathan M. Sumner", "JonathanMSumner@jourrapide.com", "818-278-3381","China","Yunnan - Yuanjiang"},
            { "amelia", "pass", "Amelia J. Decker", "AmeliaJDecker@rhyta.com", "785-527-3170","China","Su Zhou - Su Zhou Industrial Park"},
            { "john", "pass", "John P. Akin", "JohnPAkin@dayrep.com", "504-348-5535","Indonesia","Surabaya"},
            { "francisco", "pass", "Francisco D. Jackson", "FranciscoDJackson@rhyta.com", "203-318-1598","Cambodia","Krong Chbar Mon"},  
            { "alan", "pass", "Alan D. Sykora", "AlanDSykora@armyspy.com", "806-533-8110","Vietnam","Chiang Mai"},  
            { "alfonso", "pass", "Alfonso D. Miller", "AlfonsoDMiller@jourrapide.com", "218-299-6704","Canada","Toronto"},  
            { "brenda", "pass", "Brenda D. Benbow", "BrendaDBenbow@teleworm.us", "406-945-7121","Indonesia","Surabaya"},  
            { "daniel", "pass", "Daniel D. Jackson", "DanielDJackson@jourrapide.com", "615-727-7075","Indonesia","Surabaya"},  
            { "april", "pass", "April R. Wimbush", "AprilRWimbush@teleworm.us", "570-677-0429","Singapore","Alexandra"},
            { "santos", "pass", "Santos M. Dominquez", "SantosMDominquez@rhyta.com", "507-893-4405","Singapore","Singapore"},  
            { "evelyn", "pass", "Evelyn L. Gutierrez", "EvelynLGutierrez@rhyta.com", "573-383-0199","Malaysia","Malaysia"},  
            //{ "margaret", "pass", "Margaret W. Martinez", "MargaretWMartinez@rhyta.com", "314-846-8027","China","China"},  
            { "heike", "pass", "Heike M. Myers", "HeikeMMyers@teleworm.us", "269-782-1196","Indonesia","Indonesia"},  
            { "shannon", "pass", "Shannon E. Shaner", "ShannonEShaner@armyspys.com", "507-378-4352","Cambodia","Cambodia"},  
            { "ann", "pass", "Ann L. Parker", "AnnLParker@rhyta.com", "937-883-1550","Thailand","Thailand"},  
            { "gloria", "pass", "Gloria I. Anderson", "GloriaIAnderson@dayrep.com", "541-238-7327","Vietnam","Vietnam"},  
            { "tommy", "pass", "Tommy B. Rimmer", "TommyBRimmer@dayrep.com", "828-290-0237","Laos","Laos"},  
            { "helen", "pass", "Helen J. Janelle", "HelenJJanelle@jourrapide.com", "843-621-5214","Canada","Canada"},
            { "robert", "pass", "Robert S. Carter", "RobertSCarter@rhyta.com", "704-568-2372","Singapore","Global HQ"},
            { "jerry", "pass", "Jerry K. Marshall", "JerryKMarshall@jourrapide.com", "734-656-8252","Singapore","Global HQ"},
            { "david", "pass", "David M. Gurney", "DavidMGurney@armyspy.com", "609-784-7619","Singapore","Global HQ"},
            { "julie", "pass", "Julie R. Riggs", "JulieRRiggs@teleworm.us", "828-737-5598","Singapore","Global HQ"},
            { "anna", "pass", "Anna A. Smith", "AnnaASmith@jourrapide.com", "352-584-2823","Singapore","Global HQ"},
            { "rolland", "pass", "Roland K. Riggs", "RolandKRiggs@limbenjamin.com", "345-584-2173","Malaysia","Malaysia"},
            { "jude", "pass", "Jude D. Anderson", "JudeDAnderson@limbenjamin.com", "385-584-3759","Malaysia","Malaysia"},
            { "james", "pass", "James Q. Myers", "JamesQMyersn@limbenjamin.com", "385-364-3239","Malaysia","Malaysia"},
        };
        
        String[] roles = new String [] {
            "IT (Store)","Cust. Service (Store)","IT (Mfg)","IT (Store)","Warehouse (Store)","Purchasing (Mfg)",
            "Management (Store)","IT (Store)","Warehouse (Mfg)","Warehouse (Mfg)","Production Planning (Mfg)",
            "Purchasing (Mfg)","Cust. Service (Store)","Production Planning (Mfg)","Management (Mfg)","Kitchen (Store)",
            "IT (CO)","Sales Manager (Mfg)"/*,"Sales Planning (CO)"*/,"Marketing (CO)","Management (CO)",//for first system release,load evelyn as sales manager here
            "Cust. Service (CO)","Restaurant Planning (CO)","Warehouse (CO)","Web Admin (CO)","IT (HQ)","IT (HQ)",
            "Production & Ops (HQ)","Supply Chain (HQ)","Management (HQ)","Kitchen (Store)","Sales Manager (Mfg)","IT (CO)",
                                    };
        
        for (int i=0; i<arr.length; i++){
            try {
                msal.addRoleToStaffByUsername(arr[i][0], "General Staff");
                msal.addRoleToStaffByUsername(arr[i][0], roles[i]);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }
        
        //add extra sales manager role to evelyn.
        //msal.addRoleToStaffByUsername("evelyn", "Sales Manager (Mfg)");
        
        //add all roles to rose (SUPER USER) except for IT (Store) and General Staff which has been added above.;
       String[] allRoles = new String [] {
        "Cust. Service (Store)","Kitchen (Store)","Warehouse (Store)","Management (Store)",
        "IT (Mfg)","Purchasing (Mfg)","Warehouse (Mfg)","Production Planning (Mfg)","Management (Mfg)",
        "IT (CO)","Cust. Service (CO)",/*"Sales Planning (CO)",*/"Marketing (CO)","Management (CO)","Restaurant Planning (CO)","Warehouse (CO)","Web Admin (CO)",
        "IT (HQ)","Production & Ops (HQ)","Supply Chain (HQ)","Management (HQ)", "Sales Manager (Mfg)"
                                    };
        
        for (int i=0; i<allRoles.length; i++){
            try {
                msal.addRoleToStaffByUsername("rose", allRoles[i]);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }
        
        //for first system release, preload robert with 2 empty threads
        
        try {
            msgbean.createNewThread("project A discussion group", "robert,jerry,david");
            msgbean.createNewThread("jerry (private msg)", "robert,jerry");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        
        return true;
    }
}
