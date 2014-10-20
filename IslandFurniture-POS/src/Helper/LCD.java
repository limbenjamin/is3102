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
    
    private static String partnerPoleDisplayCOMPort = "COM4";
    
    public static void initPartnerPoleDisplay(OutputStream partnerPoleDisplayOutputStream, SerialPort serialPort)
    {
        Enumeration commPortList = CommPortIdentifier.getPortIdentifiers();
        
        while (commPortList.hasMoreElements()) 
        {
            CommPortIdentifier commPort = (CommPortIdentifier) commPortList.nextElement();
            
            if (commPort.getPortType() == CommPortIdentifier.PORT_SERIAL &&
                    commPort.getName().equals(partnerPoleDisplayCOMPort))
            {
                try
                {
                    serialPort = (SerialPort) commPort.open("UnifiedPointOfSale", 5000);
                    partnerPoleDisplayOutputStream = serialPort.getOutputStream();
                }
                catch(PortInUseException ex)
                {
                    JOptionPane.showMessageDialog(null, "Unable to initialize Partner Pole Display: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(IOException ex)
                {
                    JOptionPane.showMessageDialog(null, "Unable to initialize Partner Pole Display: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public static void closePartnerPoleDisplay(OutputStream partnerPoleDisplayOutputStream, SerialPort serialPort){
        if(serialPort != null)
        {
            try
            {
                byte[] clear = {0x0C};
                partnerPoleDisplayOutputStream.write(clear);
                partnerPoleDisplayOutputStream.close();
                serialPort.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }       
    }
}
