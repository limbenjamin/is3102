/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package benclient;

import IslandFurniture.ITManagementModule.ManageStaffAccountRemote;
import javax.ejb.EJB;

/**
 *
 * @author Benjamin
 */
public class Main {
    
    @EJB
    private static ManageStaffAccountRemote msar;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       msar.createStaffAccount("username", "pass", "name", "email@email.com");
    }
    
}
