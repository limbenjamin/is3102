/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
 

/**
 *
 * @author Benjamin
 */
public class SendEmailByPost {
    
    public static void sendEmail(String sender, String recipient, String subject, String message) throws Exception {

        String url = "http://dev.limbenjamin.com/send.php";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setDoOutput(true);
	con.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        
        Map<String, String> data = new HashMap<String, String>();
        data.put("nonce", "aA8ZrJ34nBX3BpXtsPb8QsHD");
        data.put("sender", sender);
        data.put("recipient", recipient);
        data.put("subject", subject);
        data.put("message", message);
        Set keys = data.keySet();
        Iterator keyIter = keys.iterator();
        String content = "";
        for(int i=0; keyIter.hasNext(); i++) {
                Object key = keyIter.next();
                if(i!=0) {
                        content += "&";
                }
                content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
        }
        wr.writeBytes(content);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        
    }
}
