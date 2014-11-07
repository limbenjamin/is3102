/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class EncryptMethods {
        static byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5 };
    static IvParameterSpec ivspec = new IvParameterSpec(iv);
    static String encryptionKey = "0123456789abcdef"; //TODO : move the keys into store entity
    
    // Used to encrypt name, email, phone no. to comply with PDPA's "reasonable security arrangements"
    public static String AESEncrypt(String plaintext){
        String ciphertext = "";
        try {
            Key key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
            byte[] plainTextByte = cipher.doFinal(plaintext.getBytes());
            ciphertext = Base64.encodeBase64String(plainTextByte);
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidAlgorithmParameterException ex) {
            System.out.println(ex.getMessage());
        }
        return ciphertext;
    }
    
    public static String AESDecrypt(String ciphertext){
        String plaintext = "";
        try{
            Key key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
            byte[] cipherTextByte = Base64.decodeBase64(ciphertext);
            byte[] plainTextByte = cipher.doFinal(cipherTextByte);
            plaintext = new String(plainTextByte);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | IOException | InvalidAlgorithmParameterException ex) {
            System.out.println(ex.getMessage());
        }
        return plaintext;
    }
    
    public static String SHA1Hash(String fullPassword){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //use SHA256 hashing for passwords
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(fullPassword.getBytes());
            byte byteArray[] = messageDigest.digest();
            //convert to hex to store in db
            for (int i = 0; i < byteArray.length; i++) {
             stringBuilder.append(Integer.toString((byteArray[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
        return stringBuilder.toString();
    }
}
