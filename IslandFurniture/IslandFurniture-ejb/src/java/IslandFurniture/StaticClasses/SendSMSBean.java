/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.OutputStream;
import javax.jms.MapMessage;

/**
 *
 * @author Benjamin
 */
public class SendSMSBean {
    
    
    public static void sendSMS(String smsRecipientNumber, String smsMessage){ 
    
        try{
            System.err.println("Sending SMS: " + smsRecipientNumber + ": " + smsMessage);

            try{
                String initString1 = "AT" + (char)13;
                String initString2 = "AT+CMGF=1" + (char)13;
                String cmdString1 = "AT+CMGS=" + smsRecipientNumber + (char)13;
                String cmdString2 = smsMessage + (char)26;

                System.err.println("initString1: " + initString1);
                System.err.println("initString2: " + initString2);
                System.err.println("cmdString1: " + cmdString1);
                System.err.println("cmdString2: " + cmdString2);

                CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier("COM7");
                SerialPort serialPort = (SerialPort)commPortIdentifier.open("SMS", 2000);
                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                OutputStream outputStream = serialPort.getOutputStream();                    

                outputStream.write(initString1.getBytes());                    
                outputStream.write(initString2.getBytes());                    
                outputStream.write(cmdString1.getBytes());                    
                outputStream.write(cmdString2.getBytes());

                Thread.sleep(2000);

                serialPort.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }                

            System.err.println("Sent");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
