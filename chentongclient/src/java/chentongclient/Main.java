/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chentongclient;

import IslandFurniture.EJB.RemoteInterfaces.LoadOrgEntitiesBeanRemote;
import javax.ejb.EJB;

/**
 * Data Loading client for sales forecasting
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class Main {

    @EJB
    private static LoadOrgEntitiesBeanRemote loadOrgEntitiesBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Load Organisation Entities (Store, Manufacturing Facility, Country Office)

        if (loadOrgEntitiesBean.loadSampleData()) {
            System.out.println("Data loaded successfully");
        }
        else{
            System.out.println("Failed to load data. Check for existing data and/or recreate islandFurniture database");
        }
    }

}
