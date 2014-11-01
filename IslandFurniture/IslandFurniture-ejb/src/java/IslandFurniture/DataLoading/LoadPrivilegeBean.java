/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.DataLoading;

import IslandFurniture.Entities.Url;
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
        "Production Planner","Procured Stock Supplier","Stock Supply","Procurement Plan","View Audit Log",
        "Ingredient","Dish","Menu","Currency","Store Inventory","External Inventory Transfer","Inventory Replenishment",
        "Promotion Manager","Pricing Management","Ingredient Management","Web Management","Member Management",
        "Manage Redeemable", "Manage Redemption", "Furniture Return", "Customer Feedback", "Product Information"};
        
        String[] privilegeDescription = new String [] {
        "Modify user particulars such as Phone no. and Email Address","Access dashboard","Send message to other IslandFurniture staff","Broadcast events and announcements to entire plant",
        "Change account password","Manage Stores, Manufacturing Facilities and Country Offices","Create staff account and assign roles ","Manage Roles amd privileges for each role","View MSSR",
        "Add and edit materials","Add and edit furnitures","Add and edit retail item","Create sales forecast","View all notifications",
        "Review sales forecast","Manage production status","Issue goods","Receive goods","View Inventory report","Monitor inventory","Transfer inventory","Manage Storage Locations",
        "Manage purchase orders","Do production planning","View supplier","View stock supply","Generate Procurement Plan","View Audit Log",
        "Manage Ingrdients","Manage Dishes and Recipes","Manage Menu items and details","Manage Currency","Manage Store Inventory","External Inventory Transfer",
        "Replenish Inventory","Manage Promotions","Manage Prices","Manage Ingredients","Manage Web functions","Manage Membership",
        "Manage Redeemable", "Manage Redemption", "Furniture Return", "Customer Feedback", "Product Information"};
        
        
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
            { "Material", "/manufacturing/material.xhtml", "fa-cube", "Material", "true","8"},
            { "Furniture", "/manufacturing/furniture.xhtml", "fa-archive", "Furniture", "true","9"},
            { "Retail Item", "/manufacturing/retailitem.xhtml", "fa-coffee", "Retail Item", "true","10"},
            { "Create Forecast", "/salesplanning/createforecast.xhtml", "fa-lightbulb-o", "Create Forecast", "true","11"},
            { "View Notifications", "/it/notification.xhtml", "fa-bell-o", "View Notifications", "true","12"},
            { "Review Forecast", "/salesplanning/reviewforecast.xhtml", "fa-file-text-o", "Review Forecast", "true","13"},
            { "Manage Production Status", "/manufacturing/productionorder.xhtml", "fa-wrench", "Manage Production Status", "true","14"},
            { "Goods Issued", "/inventorymgt/goodsissued.xhtml", "fa-file-text-o", "Goods Issued", "true","15"},
            { "Goods Issued", "/inventorymgt/goodsissueddocument.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Goods Issued", "/inventorymgt/goodsissueddocumentposted.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Goods Issued", "/inventorymgt/goodsissueddocumentcommit.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Goods Receipt", "/inventorymgt/goodsreceipt.xhtml", "fa-file-text-o", "Goods Receipt", "true","16"},
            { "Goods Receipt", "/inventorymgt/goodsreceiptdocuentposted.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Report", "/inventorymgt/inventorymonitoring_reportmgmt.xhtml", "fa-file-text-o", "Inventory Report", "true","16"},
            { "Inventory Monitoring", "/inventorymgt/inventorymonitoring.xhtml", "fa-eye", "Inventory Monitoring", "true","17"},
            { "Inventory Monitoring", "/inventorymgt/inventorymonitoring_report.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Monitoring", "/inventorymgt/inventorymonitoring_location.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Monitoring", "/inventorymgt/inventorymonitoring_stock.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Monitoring", "/inventorymgt/inventorymonitoring_ststock.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Monitoring", "/inventorymgt/inventorymonitoring_stlocation.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Transfer", "/inventorymgt/inventorytransfer.xhtml", "fa-exchange", "Internal Inventory Transfer", "true","18"},
            { "Inventory Transfer", "/inventorymgt/inventorytransfer_movementlocation.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Inventory Transfer", "/inventorymgt/inventorytransfer_movementstock.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Storage Location", "/inventorymgt/storagelocation.xhtml", "fa-trello", "Storage Location", "true","19"},
            { "Purchase Order", "/purchasing/purchaseorder.xhtml", "fa-shopping-cart", "Purchase Order", "true","20"},
            { "Purchase Order", "/purchasing/purchaseorder2.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Production Planner", "/manufacturing/productionplanning.xhtml", "fa-calendar-o", "Production Planner", "true","21"},
            { "Procured Stock Supplier", "/purchasing/supplier.xhtml", "fa-user", "Procured Stock Supplier", "true","22"},
            { "Procured Stock Supplier", "/purchasing/procurementcontract.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Stock Supply", "/manufacturing/stocksupplier.xhtml", "fa-cubes", "Stock Supply Request", "true","23"},
            { "Procurement Plan", "/manufacturing/procurementplan.xhtml", "fa-cube", "Procurement Plan", "true","24"},
            { "Ingredient", "/kitchen/ingredient.xhtml", "fa-cube", "Ingredient", "true","25"},
            { "Dish", "/kitchen/dish.xhtml", "fa-cube", "Dish Item", "true","26"},
            { "Dish", "/kitchen/recipe.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Menu", "/kitchen/menuitem.xhtml", "fa-cube", "Restaurant Menu", "true","27"},
            { "Menu", "/kitchen/menuitemdetail.xhtml", "fa-dummy", "dummy", "false","0"},
            { "Currency", "/salesplanning/currency.xhtml", "fa-cube", "Currency", "true","28"},
            {"Store Inventory", "/inventorymgt/storesection.xhtml", "fa-cube", "Store Section", "true","29"},
            {"Store Inventory", "/inventorymgt/storefrontinventory.xhtml", "fa-cube", "Storefront Inventory", "true","30"},
            {"External Inventory Transfer", "/inventorymgt/inventorytransfer_external.xhtml", "fa-cube", "External Inventory Transfer", "true","30"},
            {"External Inventory Transfer", "/inventorymgt/inventorytransfer_externaldetail.xhtml", "fa-dummy", "dummy", "false","0"},    
            {"External Inventory Transfer", "/inventorymgt/inventorytransfer_externalfulfilled.xhtml", "fa-dummy", "dummy", "false","0"},
            {"External Inventory Transfer", "/inventorymgt/inventorytransfer_externalpending.xhtml", "fa-dummy", "dummy", "false","0"},    
            {"External Inventory Transfer", "/inventorymgt/inventorytransfer_externalposted.xhtml", "fa-dummy", "dummy", "false","0"},
            {"Inventory Replenishment", "/inventorymgt/inventorytransfer_replenish.xhtml", "fa-cube", "Inventory Replenishment", "true","31"},
            {"Promotion Manager", "/ocrm/promotionmanager.xhtml", "fa-cube", "Promotion Manager", "true","32"},
            {"Pricing Management", "/salesplanning/priceplanning.xhtml", "fa-cube", "Pricing and Point Mgmt", "true","33"},
            {"Ingredient Management", "/kitchen/ingredientgoodsreceipt.xhtml", "fa-cube", "Ingredient Goods Receipt", "true","34"},
            {"Ingredient Management", "/kitchen/ingredientgoodsreceiptdocument.xhtml", "fa-dummy", "dummy", "false","0"},
            {"Ingredient Management", "/kitchen/ingredientinventory.xhtml", "fa-cube", "Ingredient Inventory", "true","35"},
            {"Web Management", "/ocrm/featuredproducts.xhtml", "fa-cube", "Featured Products", "true","36"},
            {"Web Management", "/ocrm/webbanner.xhtml", "fa-cube", "Web Banner", "true","37"},
            {"Web Management", "/ocrm/webbannerdetail.xhtml", "fa-dummy", "dummy", "false","0"},
            {"Member Management", "/ocrm/membershiptier.xhtml", "fa-cube", "Membership Tier", "true","38"},
            {"Manage Redeemable", "/ocrm/redeemableitem.xhtml", "fa-cube", "Manage Redeemable", "true","39"},
            {"Manage Redemption", "/ocrm/redemption.xhtml", "fa-cube", "Manage Redemption", "true","40"},
            {"Furniture Return", "/ocrm/furniturereturn.xhtml", "fa-cube", "Furniture Return", "true","41"},
            {"Customer Feedback", "/ocrm/feedback.xhtml", "fa-cube", "Customer Feedback", "true","42"},
            {"Product Information", "/ocrm/productinfo.xhtml", "fa-cube", "Product Information", "true","43"}
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
