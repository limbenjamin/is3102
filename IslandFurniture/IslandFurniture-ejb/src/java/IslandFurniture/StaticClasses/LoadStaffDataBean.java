/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
import IslandFurniture.StaticClasses.LoadStaffDataBeanRemote;
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
public class LoadStaffDataBean implements LoadStaffDataBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @EJB
    private ManageStaffAccountsBeanLocal msal;
    @EJB
    private ManageRolesBeanLocal mrbl;
    
    
    Country country;
    Store store;
    Staff staff;
    
    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {

        String[][] arr = new String [][] { 
            { "marilyn", "pass", "Marilyn T. Haddock", "MarilynTHaddock@limbenjamin.com", "513-355-4354","Singapore","Alexandra"},
            { "russell", "pass", "Russell C. Carter", "RussellCCarter@limbenjamin.com", "386-698-7475","Singapore","Alexandra"},
            { "michael", "pass", "Michael J. Mayhall", "MichaelJMayhall@limbenjamin.com", "978-365-1346","Singapore","Tuas"},
            { "rose", "pass", "Rose S. Cruz", "RoseSCruz@limbenjamin.com", "979-587-1564","Singapore","Alexandra"},
            { "deanna", "pass", "Deanna E. Astudillo", "DeannaEAstudillo@limbenjamin.com", "760-734-2536","Singapore","Alexandra"},
            { "nat", "pass", "Nathanael C. Foster", "NathanaelCFoster@limbenjamin.com", "843-529-4635","Singapore","Tuas"},
            { "leslie", "pass", "Leslie D. Cain", "LeslieDCain@limbenjamin.com", "413-675-8446","Singapore","Alexandra"},
            { "jonathan", "pass", "Jonathan M. Sumner", "JonathanMSumner@limbenjamin.com", "818-278-3381","Singapore","Alexandra"},
            { "amelia", "pass", "Amelia J. Decker", "AmeliaJDecker@limbenjamin.com", "785-527-3170","Singapore","Tuas"},
            { "john", "pass", "John P. Akin", "JohnPAkin@limbenjamin.com", "504-348-5535","Singapore","Tuas"},
            { "francisco", "pass", "Francisco D. Jackson", "FranciscoDJackson@limbenjamin.com", "203-318-1598","Singapore","Tuas"},  
            { "alan", "pass", "Alan D. Sykora", "AlanDSykora@limbenjamin.com", "806-533-8110","Singapore","Tuas"},  
            { "alfonso", "pass", "Alfonso D. Miller", "AlfonsoDMiller@limbenjamin.com", "218-299-6704","Singapore","Alexandra"},  
            { "brenda", "pass", "Brenda D. Benbow", "BrendaDBenbow@limbenjamin.com", "406-945-7121","Singapore","Tuas"},  
            { "daniel", "pass", "Daniel D. Jackson", "DanielDJackson@limbenjamin.com", "615-727-7075","Singapore","Tuas"},  
            { "april", "pass", "April R. Wimbush", "AprilRWimbush@limbenjamin.com", "570-677-0429","Singapore","Alexandra"},
            { "santos", "pass", "Santos M. Dominquez", "SantosMDominquez@limbenjamin.com", "507-893-4405","Singapore","Singapore"},  
            { "evelyn", "pass", "Evelyn L. Gutierrez", "EvelynLGutierrez@limbenjamin.com", "573-383-0199","Singapore","Singapore"},  
            { "mindy", "pass", "Mindy W. Martinez", "MindyWMartinez@limbenjamin.com", "314-846-8027","Singapore","Singapore"},  
            { "heike", "pass", "Heike M. Myers", "HeikeMMyers@limbenjamin.com", "269-782-1196","Singapore","Singapore"},  
            { "shannon", "pass", "Shannon E. Shaner", "ShannonEShaner@limbenjamin.com", "507-378-4352","Singapore","Singapore"},  
            { "ann", "pass", "Ann L. Parker", "AnnLParker@limbenjamin.com", "937-883-1550","Singapore","Singapore"},  
            { "gloria", "pass", "Gloria I. Anderson", "GloriaIAnderson@limbenjamin.com", "541-238-7327","Singapore","Singapore"},  
            { "tommy", "pass", "Tommy B. Rimmer", "TommyBRimmer@limbenjamin.com", "828-290-0237","Singapore","Singapore"},  
            { "helen", "pass", "Helen J. Janelle", "HelenJJanelle@limbenjamin.com", "843-621-5214","Singapore","Singapore"},
            { "robert", "pass", "Robert S. Carter", "RobertSCarter@limbenjamin.com", "704-568-2372","Singapore","Global HQ"},
            { "jerry", "pass", "Jerry K. Marshall", "JerryKMarshall@limbenjamin.com", "734-656-8252","Singapore","Global HQ"},
            { "david", "pass", "David M. Gurney", "DavidMGurney@limbenjamin.com", "609-784-7619","Singapore","Global HQ"},
            { "julie", "pass", "Julie R. Riggs", "JulieRRiggs@limbenjamin.com", "828-737-5598","Singapore","Global HQ"},
            { "anna", "pass", "Anna A. Smith", "AnnaASmith@limbenjamin.com", "352-584-2823","Singapore","Global HQ"},
            { "rolland", "pass", "Roland K. Riggs", "RolandKRiggs@limbenjamin.com", "345-584-2173","Malaysia","Malaysia"},
            { "jude", "pass", "Jude D. Anderson", "JudeDAnderson@limbenjamin.com", "385-584-3759","Malaysia","Malaysia"},
            { "james", "pass", "James Q. Myers", "JamesQMyersn@limbenjamin.com", "385-364-3239","Malaysia","Malaysia"},
            //Superusers
            { "root", "pass", "root", "root@limbenjamin.com", "123-456-7890","Singapore","Alexandra"},
            { "root1", "pass", "root1", "root1@limbenjamin.com", "123-456-7890","Singapore","Tuas"},
            { "root2", "pass", "root2", "root2@limbenjamin.com", "123-456-7890","Singapore","Singapore"},
            { "root3", "pass", "root3", "root3@limbenjamin.com", "123-456-7890","Singapore","Global HQ"},
            
        };
        
        String[] roles = new String [] {
        "IT (Store)","Cust. Service (Store)","Kitchen (Store)","Warehouse (Store)","Management (Store)",
        "IT (Mfg)","Purchasing (Mfg)","Warehouse (Mfg)","Production Planning (Mfg)","Management (Mfg)","General Staff",
        "IT (CO)","Cust. Service (CO)","Sales Planning (CO)","Sales Manager (CO)","Marketing (CO)","Management (CO)","Restaurant Planning (CO)","Warehouse (CO)","Web Admin (CO)",
        "IT (HQ)","Production & Ops (HQ)","Supply Chain (HQ)","Management (HQ)",
                                    };

        for (int i=0; i<roles.length; i++){
            try {
                mrbl.createRole(roles[i]);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }       
        
        for (int i=0; i<arr.length; i++){
            try {
                msal.createStaffAccountinBulk(arr[i][0],arr[i][1],arr[i][2],arr[i][3],arr[i][4],arr[i][5],arr[i][6]);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }
        
        return true;
    }
}
