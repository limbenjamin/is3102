/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chentongclient;

import IslandFurniture.EJB.RemoteInterfaces.LoadOrgEntitiesBeanRemote;
import IslandFurniture.EJB.RemoteInterfaces.LoadTransactionBeanRemote;
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
    private static LoadTransactionBeanRemote loadTransactionBean;

    @EJB
    private static LoadOrgEntitiesBeanRemote loadOrgEntitiesBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Load Organisation Entities (Store, Manufacturing Facility, Country Office) & FurnitureModels
        System.out.print("Load Organisation & Furniture Data? (y/n):");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (loadOrgEntitiesBean.loadSampleData()) {
                System.out.println("Organisation Data & Furniture data loaded successfully!");
            } else {
                System.out.println("Failed to load Organisation and furniture data. Check for existing data and/or recreate islandFurniture database");
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
    }

}
