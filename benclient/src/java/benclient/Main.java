/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package benclient;

import IslandFurniture.ITManagementModule.ManageStaffAccountRemote;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
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
        msar.createStaffAccount("user", "pass", "name", "email@email.com", "91234567");
        msar.createStaffAccount("user2", "pass", "name", "email2@email.com", "91234568");
        msar.createStaffAccount("user3", "pass", "name", "email3@email.com", "91234569");
        msar.createStaffAccount("user4", "pass", "name", "email4@email.com", "91234560");
        for (Object obj : msar.displayAllStaffAccounts()) {
            Vector vector = (Vector) obj;
            System.out.println(vector.get(0));
            System.out.println(vector.get(1));
            System.out.println(vector.get(2));
            System.out.println(vector.get(3));
            System.out.println(vector.get(4));
        }
    }
    
}
