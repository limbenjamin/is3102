/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.ITManagementModule;

import IslandFurniture.FW.Entities.*;
import java.security.Key;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Benjamin
 */
@Stateless
@LocalBean
public class ManageStaffAccountsBean {
    
    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private LogEntry logEntry;
    
    static String IV = "AAAAAAAAAAAAAAAA";
    static String encryptionKey = "0123456789abcdef";

    
    public boolean createStaffAccount(String username, String password, String name, String emailAddress) {
        staff = new Staff();
        staff.setUsername(username);
        //generate salt
        String salt = Long.toHexString(Double.doubleToLongBits(Math.random()));
        staff.setSalt(salt);
        String fullPassword = salt + "" + password;
        try {
            //use SHA256 hashing for passwords
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(fullPassword.getBytes());
            byte byteArray[] = messageDigest.digest();
            //convert to hex to store in db
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
             stringBuilder.append(Integer.toString((byteArray[i] & 0xff) + 0x100, 16).substring(1));
            }
            staff.setPassword(stringBuilder.toString());
        } catch (Exception ex) {
            Logger.getLogger(ManageStaffAccountsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Encrypt all personal data with AES to comply with PDPA
        try {
            Key key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encName = cipher.doFinal(name.getBytes());
            String encryptedName = new BASE64Encoder().encode(encName);
            staff.setName(name);
            byte[] encEmailAddress = cipher.doFinal(name.getBytes());
            String encryptedEmailAddress = new BASE64Encoder().encode(encEmailAddress);
            staff.setEmailAddress(emailAddress);
        } catch (Exception ex) {
            Logger.getLogger(ManageStaffAccountsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

}
