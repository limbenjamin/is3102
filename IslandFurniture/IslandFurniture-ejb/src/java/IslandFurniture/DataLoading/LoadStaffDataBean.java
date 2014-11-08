/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.DataLoading;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
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
            { "marilyn", "pass", "Marilyn T. Haddock", "MarilynTHaddock@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000000"},
            { "russell", "pass", "Russell C. Carter", "RussellCCarter@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000001"},
            { "michael", "pass", "Michael J. Mayhall", "MichaelJMayhall@limbenjamin.com", "+6581273798","Singapore","Tuas","00000002"},
            { "rose", "pass", "Rose S. Cruz", "RoseSCruz@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000003"},
            { "deanna", "pass", "Deanna E. Astudillo", "DeannaEAstudillo@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000004"},
            { "nat", "pass", "Nathanael C. Foster", "NathanaelCFoster@limbenjamin.com", "+6581273798","Singapore","Tuas","00000005"},
            { "leslie", "pass", "Leslie D. Cain", "LeslieDCain@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000006"},
            { "jonathan", "pass", "Jonathan M. Sumner", "JonathanMSumner@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000007"},
            { "amelia", "pass", "Amelia J. Decker", "AmeliaJDecker@limbenjamin.com", "+6581273798","Singapore","Tuas","00000008"},
            { "john", "pass", "John P. Akin", "JohnPAkin@limbenjamin.com", "+6581273798","Singapore","Tuas","00000009"},
            { "francisco", "pass", "Francisco D. Jackson", "FranciscoDJackson@limbenjamin.com", "+6581273798","Singapore","Tuas","00000010"},  
            { "alan", "pass", "Alan D. Sykora", "AlanDSykora@limbenjamin.com", "+6581273798","Singapore","Tuas","00000011"},  
            { "alfonso", "pass", "Alfonso D. Miller", "AlfonsoDMiller@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000012"},  
            { "brenda", "pass", "Brenda D. Benbow", "BrendaDBenbow@limbenjamin.com", "+6581273798","Singapore","Tuas","00000013"},  
            { "daniel", "pass", "Daniel D. Jackson", "DanielDJackson@limbenjamin.com", "+6581273798","Singapore","Tuas","00000014"},  
            { "april", "pass", "April R. Wimbush", "AprilRWimbush@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000015"},
            { "santos", "pass", "Santos M. Dominquez", "SantosMDominquez@limbenjamin.com", "+6581273798","Singapore","Singapore","00000016"},  
            { "evelyn", "pass", "Evelyn L. Gutierrez", "EvelynLGutierrez@limbenjamin.com", "+6581273798","Singapore","Singapore","00000017"},  
            { "mindy", "pass", "Mindy W. Martinez", "MindyWMartinez@limbenjamin.com", "+6581273798","Singapore","Singapore","00000018"},  
            { "heike", "pass", "Heike M. Myers", "HeikeMMyers@limbenjamin.com", "+6581273798","Singapore","Singapore","00000019"},  
            { "shannon", "pass", "Shannon E. Shaner", "ShannonEShaner@limbenjamin.com", "+6581273798","Singapore","Singapore","00000020"},  
            { "ann", "pass", "Ann L. Parker", "AnnLParker@limbenjamin.com", "+6581273798","Singapore","Singapore","00000021"},  
            { "gloria", "pass", "Gloria I. Anderson", "GloriaIAnderson@limbenjamin.com", "+6581273798","Singapore","Singapore","00000022"},  
            { "tommy", "pass", "Tommy B. Rimmer", "TommyBRimmer@limbenjamin.com", "+6581273798","Singapore","Singapore","00000023"},  
            { "helen", "pass", "Helen J. Janelle", "HelenJJanelle@limbenjamin.com", "+6581273798","Singapore","Singapore","00000024"},
            { "robert", "pass", "Robert S. Carter", "RobertSCarter@limbenjamin.com", "+6581273798","Singapore","Global HQ","00000025"},
            { "jerry", "pass", "Jerry K. Marshall", "JerryKMarshall@limbenjamin.com", "+6581273798","Singapore","Global HQ","00000026"},
            { "david", "pass", "David M. Gurney", "DavidMGurney@limbenjamin.com", "+6581273798","Singapore","Global HQ","00000027"},
            { "julie", "pass", "Julie R. Riggs", "JulieRRiggs@limbenjamin.com", "+6581273798","Singapore","Global HQ","00000028"},
            { "anna", "pass", "Anna A. Smith", "AnnaASmith@limbenjamin.com", "+6581273798","Singapore","Global HQ","00000029"},
            { "rolland", "pass", "Roland K. Riggs", "RolandKRiggs@limbenjamin.com", "+6581273798","Malaysia","Malaysia","00000030"},
            { "jude", "pass", "Jude D. Anderson", "JudeDAnderson@limbenjamin.com", "+6581273798","Malaysia","Malaysia","00000031"},
            { "james", "pass", "James Q. Myers", "JamesQMyersn@limbenjamin.com", "+6581273798","Malaysia","Malaysia","00000032"},
            { "leonard", "pass", "Leonard D. Sanchez", "LeonardDSanchez@limbenjamin.com", "+6581273798","Singapore","Alexandra","B00DBD31"},
            //Superusers
            { "root", "pass", "root", "root@limbenjamin.com", "+6581273798","Singapore","Alexandra","00000033"},
            { "root1", "pass", "root1", "root1@limbenjamin.com", "+6581273798","Singapore","Tuas","00000034"},
            { "root2", "pass", "root2", "root2@limbenjamin.com", "+6581273798","Singapore","Singapore","00000035"},
            { "root3", "pass", "root3", "root3@limbenjamin.com", "+6581273798","Singapore","Global HQ","00000036"},
            
        };
        
        String[] roles = new String [] {
        "IT (Store)","Cust. Service (Store)","Kitchen (Store)","Cashier (Store)","Warehouse (Store)","Management (Store)",
        "IT (Mfg)","Purchasing (Mfg)","Warehouse (Mfg)","Production Planning (Mfg)","Management (Mfg)","General Staff",
        "IT (CO)","Cust. Service (CO)","Sales Planning (CO)","Sales Manager (CO)","Marketing (CO)","Management (CO)","Restaurant Planning (CO)","Warehouse (CO)","Web Admin (CO)",
        "IT (HQ)","Production & Ops (HQ)","Supply Chain (HQ)","Management (HQ)","Cashier Supervisor (Store)",
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
                msal.createStaffAccountinBulk(arr[i][0],arr[i][1],arr[i][2],arr[i][3],arr[i][4],arr[i][5],arr[i][6],arr[i][7]);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }
        
        return true;
    }
}
