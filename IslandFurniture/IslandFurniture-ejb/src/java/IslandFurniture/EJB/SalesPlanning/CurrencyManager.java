/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.Currency;
import IslandFurniture.Entities.ExchangeRate;
import static IslandFurniture.StaticClasses.QueryMethods.findCurrencyByCode;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
            while ((s = dis.readLine()) != null) {
                inputLine.append(s);
            }
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

    @Override
    public Double retrieveExchangeRate(String currencyList, String currencyCode) {
        JSONParser parser = new JSONParser();
        try {
            Object parserObject = parser.parse(currencyList);
            JSONObject jsonObject = (JSONObject) parserObject;
            JSONObject currency = (JSONObject) jsonObject.get("rates");
            Double exchangeRate = (Double) currency.get(currencyCode);
            Double SGD = (Double) currency.get("SGD");
            System.out.println("Exchange rate of " + currencyCode + " is " + exchangeRate + ". SGD is " + SGD);
            return exchangeRate / SGD;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Currency> getAllCurrency() {
        List<Currency> currencyList;
        try {
            currencyList = em.createNamedQuery("getAllCurrencies", Currency.class).getResultList();
            return currencyList;
        } catch (Exception ex) {
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
        } catch (Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }

    public Double updateExchangeRate(String currencyCode) {
        Currency currency;
        String currencyList;
        Double rate;
        ExchangeRate exchangeRate;
        List<ExchangeRate> rateList;
        DecimalFormat df;
        try {
            df = new DecimalFormat("#.000000");
            currencyList = retrieveFullList();
            rate = retrieveExchangeRate(currencyList, currencyCode);
            rate = Double.parseDouble(df.format(rate));
            currency = findCurrencyByCode(em, currencyCode);
            rateList = currency.getExchangeRates();
            if (rateList == null) {
                rateList = new ArrayList<ExchangeRate>();
                currency.setExchangeRates(rateList);
            }
            exchangeRate = new ExchangeRate();
            exchangeRate.setExchangeRate(rate);
            exchangeRate.setEffectiveDate(Calendar.getInstance());
            currency.getExchangeRates().add(exchangeRate);
            em.flush();
            return rate;
        } catch (Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }

    @Override
    public Double computeRetailPriceWithExchangeRate(Double originalPrice, Double exchangeRate) {
        DecimalFormat df = new DecimalFormat("#.00");
        Integer basePrice;
        Double realPrice;
        Double price;
        System.out.println("Original Price is $" + originalPrice + ". Exchange Rate is $" + exchangeRate + ". Converted Rate is $" + (originalPrice * exchangeRate));
        realPrice = originalPrice * exchangeRate;
        basePrice = realPrice.intValue();
        if (realPrice <= 10) {
            price = originalPrice * exchangeRate;
        } else if (realPrice <= 50) {
            price = basePrice + 0.0;
        } else if (realPrice <= 200) {
            price = basePrice - (basePrice % 5 + 0.0);
        } else if (realPrice <= 1000) {
            price = basePrice - (basePrice % 10 + 0.0);
        } else if (realPrice <= 5000) {
            price = basePrice - (basePrice % 50 + 0.0);
        } else if (realPrice <= 10000) {
            price = basePrice - (basePrice % 100 + 0.0);
        } else {
            price = 0.0 + basePrice - (basePrice % 500);
        }
//        System.out.println("Selling price is $" + price);
        return Double.parseDouble(df.format(price));
    }
}
