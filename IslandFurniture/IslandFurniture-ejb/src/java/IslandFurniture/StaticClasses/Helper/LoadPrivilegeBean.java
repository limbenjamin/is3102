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
        "Review Forecast","Manage Production Status","Goods Issued","Goods Receipt",
        "Inventory Report","Inventory Monitoring","Inventory Transfer","Storage Location","Purchase Order",
        "Production Planner","Supplier","Stock Supply"};
        
        String[] privilegeDescription = new String [] {
        "Modify user particulars such as Phone no. and Email Address","Access dashboard","Send message to other IslandFurniture staff","Broadcast events and announcements to entire plant",
        "Change account password","Manage Stores, Manufacturing Facilities and Country Offices","Create staff account and assign roles ","Manage Roles amd privileges for each role","View MSSR",
        "Add and edit materials","Add and edit furnitures","Add and edit retail item","Create sales forecast","View all notifications",
        "Review sales forecast","Manage production status","Issue goods","Receive goods","View Inventory report","Monitor inventory","Transfer inventory","Manage Storage Locations",
        "Manage purchase orders","Do production planning","View supplier","View stock supply"};
        
        
        String[][] arr = new String [][] { 
            { "Modify Particulars", "/common/modifyparticulars.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Dashboard", "/dash.xhtml", "fa-home", "Dashboard", "true","1"},
            { "Messaging", "/common/messaging.xhtml", "fa-envelope", "Messaging", "true","2"},
            { "Messaging", "/common/messaging2.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Broadcast", "/common/broadcast.xhtml", "fa-calendar", "Broadcast", "true","3"}, 
            { "Change Password", "/changepassword.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Manage Plant", "/it/manageplant.xhtml", "fa-building", "Manage Plant", "true","4"},
            { "Manage Staff", "/it/managestaff.xhtml", "fa-users", "Manage Staff", "true","5"},
            { "Manage Staff", "/it/managestaff2.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Manage Roles", "/it/roleprivilege.xhtml", "fa-star", "Manage Roles", "true","6"},
            { "Manage Roles", "/it/roleprivilege2.xhtml", "fa-dummy", "dummy", "false","0"},
            { "MSSR", "/salesplanning/viewmssr.xhtml", "fa-bar-chart-o", "View MSSR", "true","7"},
            { "Material", "/knowledge/material.xhtml", "fa-cube", "Material", "true","8"},
            { "Furniture", "/knowledge/furniture.xhtml", "fa-archive", "Furniture", "true","9"},
            { "Retail Item", "/knowledge/retailItem.xhtml", "fa-coffee", "Retail Item", "true","10"},
            { "Create Forecast", "/salesplanning/createforecast.xhtml", "fa-lightbulb-o", "Create Forecast", "true","11"},
            { "View Notifications", "/it/notification.xhtml", "fa-bell-o", "View Notifications", "true","12"},
            { "Review Forecast", "/salesplanning/reviewforecast.xhtml", "fa-file-text-o", "Review Forecast", "true","13"},
            { "Manage Production Status", "/manufacturing/productionorder.xhtml", "fa-wrench", "Manage Production Status", "true","14"},
            { "Goods Issued", "/supplychain/goodsissued.xhtml", "fa-file-text-o", "Goods Issued", "true","15"},
            { "Goods Issued", "/supplychain/goodsissueddocument.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Goods Issued", "/supplychain/goodsissueddocumentposted.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Goods Issued", "/supplychain/goodsissueddocumentcommit.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Goods Receipt", "/supplychain/goodsreceipt.xhtml", "fa-file-text-o", "Goods Receipt", "true","16"},
            { "Goods Receipt", "/supplychain/goodsreceiptdocuentposted.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Report", "/supplychain/inventorymonitoring_reportmgmt.xhtml", "fa-file-text-o", "Inventory Report", "true","16"},
            { "Inventory Monitoring", "/supplychain/inventorymonitoring.xhtml", "fa-eye", "Inventory Monitoring", "true","17"},
            { "Inventory Monitoring", "/supplychain/inventorymonitoring_report.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Monitoring", "/supplychain/inventorymonitoring_location.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Monitoring", "/supplychain/inventorymonitoring_stock.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Monitoring", "/supplychain/inventorymonitoring_ststock.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Monitoring", "/supplychain/inventorymonitoring_stlocation.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Transfer", "/supplychain/inventorytransfer.xhtml", "fa-exchange", "Inventory Transfer", "true","18"},
            { "Inventory Transfer", "/supplychain/inventorytransfer_movementlocation.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Transfer", "/supplychain/inventorytransfer_movementstock.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Storage Location", "/supplychain/storagelocation.xhtml", "fa-trello", "Storage Location", "true","19"},
            { "Purchase Order", "/purchasing/purchaseorder.xhtml", "fa-shopping-cart", "Purchase Order", "true","20"},
            { "Purchase Order", "/purchasing/purchaseorder2.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Production Planner", "/productionplanning/index.xhtml", "fa-calendar-o", "Production Planner", "true","21"},
            { "Supplier", "/salesplanning/supplier.xhtml", "fa-user", "Supplier", "true","22"},
            { "Supplier", "/salesplanning/procurementContract.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Stock Supply", "/salesplanning/stockSupplier.xhtml", "fa-cubes", "Stock Supply Request", "true","23"},
        };
        
        for (int i=0; i<privileges.length; i++){
            urlList = new ArrayList<Url>();
            urlList.clear();
            try {
                for (int j=0; j<arr.length; j++){
                    if (arr[j][0].equals(privileges[i])){
                        url = mrpl.createUrl(arr[j][1], arr[j][2], arr[j][3], Boolean.parseBoolean(arr[j][4]), Integer.parseInt(arr[j][5]));
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
