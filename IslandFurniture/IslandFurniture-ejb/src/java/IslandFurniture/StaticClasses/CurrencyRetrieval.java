/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Scanner;

public class CurrencyRetrieval {
    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);
        String output;
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        int counter = 0;
        while(sc.hasNext()) {
            output = "";
            counter++;
            String msg = sc.nextLine();
            output += "{ \"";
            output += msg.substring(1, 4);
            output += "\", \"";
            output += msg.substring(8, msg.length()-2);
            output += "\" },";
            writer.println(output);
        }
        writer.close();
    }
}
