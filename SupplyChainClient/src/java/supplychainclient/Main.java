/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supplychainclient;

import ejb.SupplyChainBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author a0101774
 */
public class Main {
    @EJB
    private static SupplyChainBeanRemote supplyChainBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
    
}
