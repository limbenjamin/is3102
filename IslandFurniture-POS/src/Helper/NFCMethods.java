/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Helper;

import java.awt.Color;
import java.nio.ByteBuffer;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

/**
 *
 * @author Benjamin
 */
public class NFCMethods {
    
    private CardTerminal acr122uCardTerminal = null;

    public String getID(CardTerminal term){
        byte[] byteArrayReadUID = { (byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00 };
        acr122uCardTerminal = term;
        try 
        {
            acr122uCardTerminal.waitForCardPresent(0);
            Card card = acr122uCardTerminal.connect("T=1");
            CardChannel cardChannel = card.getBasicChannel();
            return send(byteArrayReadUID, cardChannel);                        
        }
        catch (Exception ex) 
        {
            return "";
        }
    }
    
    private String connectToCard(byte[] byteArrayReadUID) 
    {
        try 
        {
            acr122uCardTerminal.waitForCardPresent(0);
            Card card = acr122uCardTerminal.connect("T=1");
            CardChannel cardChannel = card.getBasicChannel();
            
            return send(byteArrayReadUID, cardChannel);                        
        }
        catch (Exception ex) 
        {
            return "";
        }
    }

    
    
    public String send(byte[] command, CardChannel cardChannel) 
    {
        String response = "";

        byte[] byteArrayResponse = new byte[258];
        ByteBuffer bufferedCommand = ByteBuffer.wrap(command);
        ByteBuffer bufferedResponse = ByteBuffer.wrap(byteArrayResponse);

        // output = The length of the received response APDU
        int output = 0;

        try 
        {
            output = cardChannel.transmit(bufferedCommand, bufferedResponse);
        } 
        catch (CardException ex) 
        {
            ex.printStackTrace();
        }

        for (int i = 0; i < output; i++) 
        {
            response += String.format("%02X", byteArrayResponse[i]);
        }

        return response;
    }
}
