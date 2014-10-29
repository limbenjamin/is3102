/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Helper;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.swing.JOptionPane;

/**
 *
 * @author Benjamin
 */
public class LCD {
    
    private static String partnerPoleDisplayCOMPort = "COM7";
    
    public static String getPort() {
        return partnerPoleDisplayCOMPort;
    }
    
    
}
