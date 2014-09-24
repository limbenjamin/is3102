/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Url;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import java.util.ArrayList;
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
public class LoadPrivilegeBean implements LoadPrivilegeBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @EJB
    private ManagePrivilegesBeanLocal mrpl;
    
    private Url url;
    private List<Url> urlList;
    
    
    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {
        
        String[] privileges = new String [] {
        "Modify Particulars","Dashboard","Messaging","Broadcast",
        "Change Password","Manage Plant","Manage Staff","Manage Roles","MSSR",
        "Material","Furniture","Retail Item","Create Forecast","View Notifications",
        "Review Forecast"};
        
        String[] privilegeDescription = new String [] {
        "Modify user particulars such as Phone no. and Email Address","Access dashboard","Send message to other IslandFurniture staff","Broadcast events and announcements to entire plant",
        "Change account password","Manage Stores, Manufacturing Facilities and Country Offices","Create staff account and assign roles ","Manage Roles amd privileges for each role","View MSSR",
        "Add and edit materials","Add and edit furnitures","Add and edit retail item","Create sales forecast","View all notifications",
        "Review sales forecast"};
        
        
        String[][] arr = new String [][] { 
            { "Modify Particulars", "/common/modifyparticulars.xhtml", "fa-dummy", "dummy", "false"},
            { "Dashboard", "/dash.xhtml", "fa-home", "Dashboard", "true"},
            { "Messaging", "/common/messaging.xhtml", "fa-envelope", "Messaging", "true"},
            { "Messaging", "/common/messaging2.xhtml", "fa-dummy", "dummy", "false"},
            { "Broadcast", "/common/broadcast.xhtml", "fa-home", "Broadcast", "true"}, 
            { "Change Password", "/changepassword.xhtml", "fa-dummy", "dummy", "false"},
            { "Manage Plant", "/it/manageplant.xhtml", "fa-desktop", "Manage Plant", "true"},
            { "Manage Staff", "/it/managestaff.xhtml", "fa-calendar", "Manage Staff", "true"},
            { "Manage Staff", "/it/managestaff2.xhtml", "fa-dummy", "dummy", "false"},
            { "Manage Roles", "/it/roleprivilege.xhtml", "fa-pencil", "Manage Roles", "true"},
            { "Manage Roles", "/it/roleprivilege2.xhtml", "fa-dummy", "dummy", "false"},
            { "MSSR", "/salesplanning/viewmssr.xhtml", "fa-home", "View MSSR", "true"},
            { "Material", "/knowledge/material.xhtml", "fa-home", "Material", "true"},
            { "Furniture", "/knowledge/furniture.xhtml", "fa-home", "Furniture", "true"},
            { "Retail Item", "/knowledge/retailItem.xhtml", "fa-home", "Retail Item", "true"},
            { "Create Forecast", "/salesplanning/createforecast.xhtml", "fa-home", "Create Forecast", "true"},
            { "View Notifications", "/it/notification.xhtml", "fa-home", "View Notifications", "true"},
            { "Review Forecast", "/salesplanning/reviewforecast.xhtml", "fa-home", "Review Forecast", "true"},
        };
        
        for (int i=0; i<privileges.length; i++){
            urlList = new ArrayList<Url>();
            urlList.clear();
            try {
                for (int j=0; j<arr.length; j++){
                    if (arr[j][0].equals(privileges[i])){
                        url = mrpl.createUrl(arr[j][1], arr[j][2], arr[j][3], Boolean.parseBoolean(arr[j][4]));
                        urlList.add(url);
                    }
                }
                mrpl.createPrivilege(privileges[i], privilegeDescription[i],urlList);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }      
        
        
        
        return true;
    }
}
