/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.Currency;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author a0101774
 */
@Stateless
public class CurrencyManager implements CurrencyManagerLocal {

    @PersistenceContext
    EntityManager em;

    @Override
    public String retrieveFullList() {
        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s; 
        String msg = null;
        StringBuffer inputLine = new StringBuffer();
        String[] currencyCode;
        Double[] currencyPrice; 
        JSONParser parser = new JSONParser();
        try {
            System.out.println("CurrencyManager.downloadURL");
            u = new URL("http://openexchangerates.org/api/latest.json?app_id=3e821a92a4f14e03b09de0e579a89fdf");
            is = u.openStream();  
            dis = new DataInputStream(new BufferedInputStream(is));
            while ((s = dis.readLine()) != null)
                inputLine.append(s); 
            System.out.println(inputLine); 
            is.close();
            return inputLine.toString();

        } catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
            return null;

        } catch (IOException ioe) {

            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            return null;
            
        } 
    }
    public Double retrieveExchangeRate(String currencyList, String currencyCode) {
        JSONParser parser = new JSONParser();
        try {
            Object parserObject = parser.parse(currencyList);
            JSONObject jsonObject = (JSONObject) parserObject;
            JSONObject currency = (JSONObject) jsonObject.get("rates");
            Double exchangeRate = (Double) currency.get(currencyCode);
            System.out.println("Exchange rate of " + currencyCode + " is " + exchangeRate);
            return exchangeRate;
        } catch(Exception ex) {
            return null;
        }
    }
    public String addCurrency(Long countryID, String currencyCode) {
        Country country;
        List<Currency> currencyList;
        try {
            country = em.find(Country.class, countryID);
            currencyList = new ArrayList<Currency>();
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }

    public List<Currency> getAllCurrency() {
        List<Currency> currencyList;
        try {
            currencyList = em.createNamedQuery("getAllCurrencies", Currency.class).getResultList();
            return currencyList;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;  
        }
    }
    public Currency getCurrency(Long currencyID) {
        Currency currency;
        try {
            System.out.println("CurrencyManager.getCurrency()");
            currency = em.find(Currency.class, currencyID);
            return currency;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;  
        }
    }
}
