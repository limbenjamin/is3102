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
        
        
        String[] roles = new String [] {
        "IT (Store)","Cust. Service (Store)","Kitchen (Store)","Warehouse (Store)","Management (Store)",
        "IT (Mfg)","Purchasing (Mfg)","Warehouse (Mfg)","Production Planning (Mfg)","Management (Mfg)","General Staff",
        "IT (CO)","Cust. Service (CO)","Sales Planning (CO)","Marketing (CO)","Management (CO)","Restaurant Planning (CO)","Warehouse (CO)","Web Admin (CO)",
        "IT (HQ)","Production & Ops (HQ)","Supply Chain (HQ)","Management (HQ)", "Sales Manager (Mfg)"
                                    };

            try {
                role = mrbl.getRoleFromName("General Staff");                
                mrbl.addPrivilegeToRole(role.getId(), "Dashboard");
                mrbl.addPrivilegeToRole(role.getId(), "Modify Particulars");
                mrbl.addPrivilegeToRole(role.getId(), "Messaging");
                mrbl.addPrivilegeToRole(role.getId(), "Change Password");

                role = mrbl.getRoleFromName("IT (Store)");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Staff");

                role = mrbl.getRoleFromName("Warehouse (Store)");
                mrbl.addPrivilegeToRole(role.getId(), "Goods Issued");
                mrbl.addPrivilegeToRole(role.getId(), "Goods Receipt");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Monitoring");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Transfer");
                mrbl.addPrivilegeToRole(role.getId(), "Storage Location");
                
                role = mrbl.getRoleFromName("Management (Store)");
                mrbl.addPrivilegeToRole(role.getId(), "Broadcast");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Report");
                
                role = mrbl.getRoleFromName("Sales Planning (CO)");
                mrbl.addPrivilegeToRole(role.getId(), "MSSR");
                mrbl.addPrivilegeToRole(role.getId(), "Create Forecast");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Report");

                role = mrbl.getRoleFromName("Sales Manager (Mfg)");
                mrbl.addPrivilegeToRole(role.getId(), "Review Forecast");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Report");
                
                role = mrbl.getRoleFromName("IT (CO)");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Staff"); 

                role = mrbl.getRoleFromName("Warehouse (CO)");
                mrbl.addPrivilegeToRole(role.getId(), "Goods Issued");
                mrbl.addPrivilegeToRole(role.getId(), "Goods Receipt");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Monitoring");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Transfer");
                mrbl.addPrivilegeToRole(role.getId(), "Storage Location");
                
                role = mrbl.getRoleFromName("Web Admin (CO)");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Monitoring");
                
                
                role = mrbl.getRoleFromName("Management (CO)");
                mrbl.addPrivilegeToRole(role.getId(), "Broadcast");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Report");

                role = mrbl.getRoleFromName("IT (Mfg)");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Staff");
                mrbl.addPrivilegeToRole(role.getId(), "Production Planner");
                
                role = mrbl.getRoleFromName("Purchasing (Mfg)");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Report");
                mrbl.addPrivilegeToRole(role.getId(), "Purchase Order");
                mrbl.addPrivilegeToRole(role.getId(), "Production Planner");
                
                role = mrbl.getRoleFromName("Warehouse (Mfg)");
                mrbl.addPrivilegeToRole(role.getId(), "Goods Issued");
                mrbl.addPrivilegeToRole(role.getId(), "Goods Receipt");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Monitoring");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Transfer");
                mrbl.addPrivilegeToRole(role.getId(), "Storage Location");
                mrbl.addPrivilegeToRole(role.getId(), "Production Planner");

                role = mrbl.getRoleFromName("Production Planning (Mfg)");
                mrbl.addPrivilegeToRole(role.getId(), "Storage Location");
                mrbl.addPrivilegeToRole(role.getId(), "Production Planner");
                
                role = mrbl.getRoleFromName("Management (Mfg)");
                mrbl.addPrivilegeToRole(role.getId(), "Broadcast");
                mrbl.addPrivilegeToRole(role.getId(), "Inventory Report");
                mrbl.addPrivilegeToRole(role.getId(), "Purchase Order");
                mrbl.addPrivilegeToRole(role.getId(), "Storage Location");
                mrbl.addPrivilegeToRole(role.getId(), "Production Planner");

                role = mrbl.getRoleFromName("IT (HQ)");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Plant");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Staff");
                mrbl.addPrivilegeToRole(role.getId(), "Manage Roles");
                mrbl.addPrivilegeToRole(role.getId(), "View Notifications");

                role = mrbl.getRoleFromName("Production & Ops (HQ)");
                mrbl.addPrivilegeToRole(role.getId(), "Material");
                mrbl.addPrivilegeToRole(role.getId(), "Furniture");
                mrbl.addPrivilegeToRole(role.getId(), "Retail Item");
                mrbl.addPrivilegeToRole(role.getId(), "Stock Supply");
                
                role = mrbl.getRoleFromName("Supply Chain (HQ)");
                mrbl.addPrivilegeToRole(role.getId(), "Material");
                mrbl.addPrivilegeToRole(role.getId(), "Furniture");
                mrbl.addPrivilegeToRole(role.getId(), "Retail Item");
                mrbl.addPrivilegeToRole(role.getId(), "Supplier");

                role = mrbl.getRoleFromName("Management (HQ)");
                mrbl.addPrivilegeToRole(role.getId(), "Broadcast");
                
                
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }       
        return true;
    }

}
