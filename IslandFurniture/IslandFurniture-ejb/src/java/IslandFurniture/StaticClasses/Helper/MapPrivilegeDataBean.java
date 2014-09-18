/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Role;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
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
public class MapPrivilegeDataBean implements MapPrivilegeDataBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @EJB
    private ManageRolesBeanLocal mrbl;
    
    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {
        
        Role role;
        
        String[] privileges = new String [] {
        "Login","Modify Particulars","Messaging","Broadcast","Dashboard",
        "Change Password","Manage Plant","Manage Staff","Manage Roles","MSSR",
        "Material","Furniture"};
        
        String[] roles = new String [] {
        "IT (Store)","Cust. Service (Store)","Kitchen (Store)","Warehouse (Store)","Management (Store)",
        "IT (Mfg)","Purchasing (Mfg)","Warehouse (Mfg)","Production Planning (Mfg)","Management (Mfg)","General Staff",
        "IT (CO)","Cust. Service (CO)","Sales Planning (CO)","Marketing (CO)","Management (CO)","Restaurant Planning (CO)","Warehouse (CO)","Web Admin (CO)",
        "IT (HQ)","Production & Ops (HQ)","Supply Chain (HQ)","Management (HQ)"
                                    };

            try {
                role = mrbl.getRoleFromName("General Staff");                
                mrbl.addPrivilegeToRole(role.getId(), "Login");
                mrbl.addPrivilegeToRole(role.getId(), "Modify Particulars");
                mrbl.addPrivilegeToRole(role.getId(), "Messaging");
                mrbl.addPrivilegeToRole(role.getId(), "Dashboard");
                mrbl.addPrivilegeToRole(role.getId(), "Change Password");

                role = mrbl.getRoleFromName("IT (Store)");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Staff");

                role = mrbl.getRoleFromName("Management (Store)");
                mrbl.addPrivilegeToRole(role.getId(), "Broadcast");

                role = mrbl.getRoleFromName("Sales Planning (CO)");
                mrbl.addPrivilegeToRole(role.getId(), "MSSR");

                role = mrbl.getRoleFromName("IT (CO)");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Staff"); 

                role = mrbl.getRoleFromName("Management (CO)");
                mrbl.addPrivilegeToRole(role.getId(), "Broadcast");

                role = mrbl.getRoleFromName("IT (Mfg)");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Staff");

                role = mrbl.getRoleFromName("Management (Mfg)");
                mrbl.addPrivilegeToRole(role.getId(), "Broadcast");

                role = mrbl.getRoleFromName("IT (HQ)");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Plant");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Staff");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Roles");

                role = mrbl.getRoleFromName("Production & Ops (HQ)");
                mrbl.addPrivilegeToRole(role.getId(), "Material");
                mrbl.addPrivilegeToRole(role.getId(), "Furniture");

                role = mrbl.getRoleFromName("Supply Chain (HQ)");
                mrbl.addPrivilegeToRole(role.getId(), "Material");
                mrbl.addPrivilegeToRole(role.getId(), "Furniture");

                role = mrbl.getRoleFromName("Management (HQ)");
                mrbl.addPrivilegeToRole(role.getId(), "Broadcast");
                
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }       
        return true;
    }

}
