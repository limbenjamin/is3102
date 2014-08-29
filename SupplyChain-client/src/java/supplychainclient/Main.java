/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package supplychainclient;

import ejb.SupplyChainBeanRemote;
import java.util.Scanner;
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
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Select and Enter:\n 1) Display inbound shipment schedule for selected time period\n 2) Create Goods Receipt documents\n 3) Display a list of Goods Receipt documents\n 4) View contents of single Goods Receipt document\n 5) View StorageLocation contents, giving a breakdown by StockUnits");
            System.out.println(" 6) Assign quantity of stocks from a single StockUnit to multiple StorageLocations\n 7) Display list of StockUnits and their location of selected Stock\n 8) Create Goods Issued document\n 9) Update details of unconfirmed Goods Issued doc\n10) View selected Goods Issued document");
            System.out.println("11) Display a list of Goods Issued document\n12) Confirm Goods Issued document and update inventories\n13) Display inventory report\n14) Display total inventory count of selected Stock\n15) Update Global HQ about current plant's stock inventory for all stocks supplied by the plant\n16) Update quantity for a list of StockUnit\n\n0) Exit");
            System.out.print("\nOption: ");
            String input = sc.next();
            System.out.println("You chose :" + input);
            if(input.equals("0"))
                break;
            assignMethod(input);
        }
    }
    public static void assignMethod(String input) {
        switch(Integer.parseInt(input)){
            case 1: 
                break;
            case 2: ;
                break;
            case 3: ;
                break;
            case 4: ;
                break;
            case 5: ;
                break;
            case 6: ;
                break;
            case 7: ;
                break;
            case 8: ;
                break;
            case 9: ;
                break;
            case 10: ;
                break;
            case 11: ;
                break;
            case 12: ;
                break;
            case 13: ;
                break;
            case 14: ;
                break;
            case 15: ;
                break;
            case 16: ;
                break;
                
        }
                
    }
}
