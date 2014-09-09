
package supplychainclient;


import java.util.Scanner;
import javax.ejb.EJB;
import IslandFurniture.EJB.SupplyChain.ManageStockRemote;

public class Main {
    @EJB
    private static ManageStockRemote manageStock;

    public static void main(String[] args) {
        
        System.out.println("Hello World!");
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Select and Enter:\n 1) Add Material");
            System.out.print("\nOption: ");
            String input = sc.nextLine();
            System.out.println("You chose :" + input);
            if(input.equals("0"))
                break;
            assignMethod(input);
        }
    }
    public static void assignMethod(String input) {
  //      manageStock = new ManageStock();
        switch(Integer.parseInt(input)) {
            case 1: 
                if(manageStock == null) 
                    System.out.println("null");
                else 
                    manageStock.addMaterial("screw");
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
