/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataloader;

import IslandFurniture.DataLoading.LoadJamesTestDataRemote;
import IslandFurniture.DataLoading.LoadKitchenDataBeanRemote;
import IslandFurniture.DataLoading.LoadOrgEntitiesBeanRemote;
import IslandFurniture.DataLoading.LoadPrivilegeBeanRemote;
import IslandFurniture.DataLoading.LoadSalesForecastBeanRemote;
import IslandFurniture.DataLoading.LoadStaffDataBeanRemote;
import IslandFurniture.DataLoading.LoadStocksBeanRemote;
import IslandFurniture.DataLoading.LoadStorageDataBeanRemote;
import IslandFurniture.DataLoading.LoadSupplierBeanRemote;
import IslandFurniture.DataLoading.LoadTransactionBeanRemote;
import IslandFurniture.DataLoading.MapPrivilegeDataBeanRemote;
import IslandFurniture.DataLoading.MapStaffDataBeanRemote;
import java.util.Scanner;
import javax.ejb.EJB;

/**
 * IMPORTANT! Data Loading client for all types of sample data. Some segments do
 * not check for duplicates (e.g. transactions), hence please do not run too
 * many times (might overload your database)!
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class Main {
    @EJB
    private static LoadKitchenDataBeanRemote loadKitchenDataBean;

    @EJB
    private static LoadStorageDataBeanRemote loadStorageDataBean;

    @EJB
    private static LoadSupplierBeanRemote loadSupplierBean;

    @EJB
    private static LoadStocksBeanRemote loadStocksBean;
    @EJB
    private static LoadSalesForecastBeanRemote loadSalesForecastBean;

    @EJB
    private static LoadTransactionBeanRemote loadTransactionBean;

    @EJB
    private static LoadOrgEntitiesBeanRemote loadOrgEntitiesBean;

    @EJB
    private static LoadStaffDataBeanRemote loadStaffDataBean;

    @EJB
    private static MapStaffDataBeanRemote mapStaffDataBean;

    @EJB
    private static LoadJamesTestDataRemote loadJamesTestData;

    @EJB
    private static LoadPrivilegeBeanRemote loadPrivilegeBean;

    @EJB
    private static MapPrivilegeDataBeanRemote mapPrivilegeDataBean;

    private static final int AUTO = 0;
    private static final int MANUAL = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Load Organisation Entities (Store, Manufacturing Facility, Country Office)
        System.out.print("Load Organisation Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadOrgEntitiesBean.loadSampleData()) {
                System.out.println("Organisation Data loaded successfully!");
            } else {
                System.out.println("Failed to load Organisation data. Check for existing data and/or recreate islandFurniture database");
            }
        }

        // Load Stock Entities (FurnitureModel, RetailItem, Materials) & StockSupplied relationships
        System.out.print("Load Stock Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadStocksBean.loadSampleData(MANUAL)) {
                System.out.println("Stock data (Furniture Model, Retail Items, Materials, StockSupplied relationships) loaded successfully!");
            } else {
                System.out.println("Failed to load Stock data. Check for existing data and/or recreate islandFurniture database");
            }
        }

        // Load Stock Entities (FurnitureModel, RetailItem, Materials) & StockSupplied relationships
        System.out.print("Load Supplier Related Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadSupplierBean.loadSampleData()) {
                System.out.println("Supplier data (incl. procurement contracts) loaded successfully!");
            } else {
                System.out.println("Failed to load Supplier data. Check for existing data and/or recreate islandFurniture database");
            }
        }

        // Load Stock Entities (FurnitureModel, RetailItem, Materials) & StockSupplied relationships
        System.out.print("Load Restaurant/Kitchen Related Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadKitchenDataBean.loadSampleData()) {
                System.out.println("Kitchen data (incl. Suppliers all the way to Ingredients) loaded successfully!");
            } else {
                System.out.println("Failed to load Kitchen data. Check for existing data and/or recreate islandFurniture database");
            }
        }

        // Load Storage Related Entites (StorageArea, StorageBin)
        System.out.print("Load Storage Related Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadStorageDataBean.loadSampleData()) {
                System.out.println("Storage data (StorageArea and Bins) loaded successfully!");
            } else {
                System.out.println("Failed to load Storage data. Check for existing data and/or recreate islandFurniture database");
            }
        }

        // Load Transaction Data (Furniture & Retail Items only for now)
        System.out.print("Load Transaction Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadTransactionBean.loadSampleData()) {
                System.out.println("Transaction data loaded successfully!");
            } else {
                System.out.println("Failed to load Transaction data. Check for existing data and/or recreate islandFurniture database");
            }
        }

        // Load Staff Data
        System.out.println("Note: If Organisation Data is not loaded, this function will throw error.");
        System.out.print("Load and map Staff, Roles and Privilege Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadStaffDataBean.loadSampleData()) {
                System.out.println("Staff data loaded successfully!");
            } else {
                System.out.println("Failed to load staff data. Check for errors in server log.");
            }
            if (mapStaffDataBean.loadSampleData()) {
                System.out.println("Staff data mapped successfully!");
            } else {
                System.out.println("Failed to map staff data. Check for errors in server log.");
            }
            if (loadPrivilegeBean.loadSampleData()) {
                System.out.println("Privilege data loaded successfully!");
            } else {
                System.out.println("Failed to load privilege data. Check for errors in server log.");
            }
            if (mapPrivilegeDataBean.loadSampleData()) {
                System.out.println("Privilege data mapped successfully!");
            } else {
                System.out.println("Failed to map privilege data. Check for errors in server log.");
            }
        }

        // Load Sales Forecasts from given set of Transactions
        System.out.print("Generate sales forecasts? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadSalesForecastBean.loadSampleData()) {
                System.out.println("Sales Forecast data generated successfully from transactions!");
            } else {
                System.out.println("Failed to generate sales forecast data. Check for errors in server log.");
            }
        }

//        // Load Production Capacity Data
//        //Added by James
//        //shift down later
//        System.out.print("Load Production Capacity Data? (y/n):");
//        if (sc.nextLine().equalsIgnoreCase("y")) {
//            if (loadJamesTestData.loadProductionCapacityData()) {
//                System.out.println("Production Capacity Data Emulated");
//            } else {
//                System.out.println("Failed to emulate Production Capacity Data");
//            }
//        }
    }

}
