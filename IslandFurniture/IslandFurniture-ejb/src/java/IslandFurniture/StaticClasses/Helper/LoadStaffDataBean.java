/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Store;
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
    private ManageStaffAccountsBeanLocal msar;
    Country country;
    Store store;
    Staff staff;
    
    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {

        try {
            msar.createStaffAccount("marilyn", "pass", "Marilyn T. Haddock", "MarilynTHaddock@teleworm.us", "513-355-4354","Laos","Vientiane");
            msar.createStaffAccount("russell", "pass", "Russell C. Carter", "RussellCCarter@teleworm.us", "386-698-7475","Thailand","Bangkok - Ma Boon Krong");
            msar.createStaffAccount("michael", "pass", "Michael J. Mayhall", "MichaelJMayhall@rhyta.com", "978-365-1346","Indonesia","Sukabumi");
            msar.createStaffAccount("rose", "pass", "Rose S. Cruz", "RoseSCruz@rhyta.com", "979-587-1564","Singapore","Alexandra");

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            
            return false;
        }
    }
}
