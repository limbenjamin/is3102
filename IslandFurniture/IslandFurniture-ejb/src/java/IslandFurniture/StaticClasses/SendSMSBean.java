/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Role;
import IslandFurniture.Entities.Staff;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

                CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier("COM6");
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
    
    public static void sendSMSByPrivilegeFromPlant(String smsMessage, Privilege privilege, Plant plant){
        List<Role> roleList = privilege.getRoles();
        Role role;
        Set<Staff> staffHash = new HashSet();
        List<Staff> staffList;
        Staff staff;
        Iterator<Role> iterator = roleList.iterator();
        
        
        while (iterator.hasNext()) {
            role = iterator.next();
            staffList = role.getStaffs();
            Iterator<Staff> iterator2 = staffList.iterator();
            while (iterator2.hasNext()) {
                staff = iterator2.next();
                staffHash.add(staff);
            }
        }
        Iterator<Staff> iterator2 = staffHash.iterator();
        while (iterator2.hasNext()) {
            staff = iterator2.next();
            if (staff.getPlant().equals(plant)){
                SendSMSBean.sendSMS(staff.getPhoneNo(), smsMessage);
            }
        }
    }
}
