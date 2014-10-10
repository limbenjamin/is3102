/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
/**
 *
 * @author Benjamin
 */
public class Connector {
    
    
    public static String postForm(List params, List values, String path) throws Exception {
    String url = "https://localhost/ws/rest/";
    url += path;    
    System.err.println(url);    
    TrustManager[] trustAllCerts = new TrustManager[] {
       new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

       }
    };
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
    };
    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); 
        
    try {               
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            //add request header
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            
            Map<String, String> data = new HashMap<String, String>();
            for (int i=0;i<params.size();i++){
                data.put(String.valueOf(params.get(i)), String.valueOf(values.get(i)));
            }
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
            BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();
        } catch (ProtocolException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    return "error";
    }
}