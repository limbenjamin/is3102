/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chentongclient;

import IslandFurniture.StaticClasses.Helper.LoadOrgEntitiesBeanRemote;
import IslandFurniture.StaticClasses.Helper.LoadSalesForecastBeanRemote;
import IslandFurniture.StaticClasses.Helper.LoadTransactionBeanRemote;
import IslandFurniture.StaticClasses.Helper.LoadStaffDataBeanRemote;
import IslandFurniture.StaticClasses.Helper.LoadStocksBeanRemote;
import java.util.Scanner;
import javax.ejb.EJB;

/**
 * IMPORTANT! Data Loading client for organisation data and transaction data.
 * Around 16k lines of Transaction Detail data loaded when loading transaction
 * data. Please do not run too many times.
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class Main {

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
            if (loadStocksBean.loadSampleData()) {
                System.out.println("Stock data loaded successfully!");
            } else {
                System.out.println("Failed to load Stock data. Check for existing data and/or recreate islandFurniture database");
            }
        }

        // Load Transaction Data (Furniture Only for now)
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
        System.out.print("Load Staff Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadStaffDataBean.loadSampleData()) {
                System.out.println("Staff data generated successfully from transactions!");
            } else {
                System.out.println("Failed to load staff data. Check for errors in server log.");
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

    }

}
