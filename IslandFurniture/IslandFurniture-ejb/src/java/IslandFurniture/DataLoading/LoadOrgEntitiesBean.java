/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Currency;
import IslandFurniture.Entities.ExchangeRate;
import IslandFurniture.Entities.GlobalHQ;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Store;
import IslandFurniture.StaticClasses.QueryMethods;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadOrgEntitiesBean implements LoadOrgEntitiesBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private GlobalHQ addGlobalHQ(String name, Country country, String timeZoneID, double lati, double longi, String address) {
        GlobalHQ globalhq = (GlobalHQ) QueryMethods.findPlantByName(em, country, name);

        if (globalhq == null) {
            globalhq = new GlobalHQ();
            globalhq.setName(name);
            globalhq.setCountry(country);
            globalhq.setTimeZoneID(timeZoneID);
            globalhq.setAddress(address);
            globalhq.setLatitude(lati);
            globalhq.setLongitude(longi);
            em.persist(globalhq);

            return globalhq;
        } else {
            System.out.println("Global HQ already exists");

            return null;
        }
    }

    private Currency addCurrency(String currencyName, String currencyCode) {
        Currency currency;
        List<ExchangeRate> rateList;

        currency = QueryMethods.findCurrencyByCode(em, currencyCode);
        if (currency == null) {
            currency = QueryMethods.findCurrencyByName(em, currencyName);
            if (currency == null) {
                currency = new Currency(currencyName, currencyCode);

                em.persist(currency);
                return currency;
            } else {
                System.out.println("Currency Name already exists");
                return currency;
            }
        } else {
            return currency;
        }
    }

//    private String[][] getCurrencyArray() {
//        String[][] arr = {  
//            { "AED", "United Arab Emirates Dirham" },
//            { "AFN", "Afghan Afghani" },
//            { "ALL", "Albanian Lek" },
//            { "AMD", "Armenian Dram" },
//            { "ANG", "Netherlands Antillean Guilder" },
//            { "AOA", "Angolan Kwanza" },
//            { "ARS", "Argentine Peso" },
//            { "AUD", "Australian Dollar" },
//            { "AWG", "Aruban Florin" },
//            { "AZN", "Azerbaijani Manat" },
//            { "BAM", "Bosnia-Herzegovina Convertible Mark" },
//            { "BBD", "Barbadian Dollar" },
//            { "BDT", "Bangladeshi Taka" },
//            { "BGN", "Bulgarian Lev" },
//            { "BHD", "Bahraini Dinar" },
//            { "BIF", "Burundian Franc" },
//            { "BMD", "Bermudan Dollar" },
//            { "BND", "Brunei Dollar" },
//            { "BOB", "Bolivian Boliviano" },
//            { "BRL", "Brazilian Real" },
//            { "BSD", "Bahamian Dollar" },
//            { "BTC", "Bitcoin" },
//            { "BTN", "Bhutanese Ngultrum" },
//            { "BWP", "Botswanan Pula" },
//            { "BYR", "Belarusian Ruble" },
//            { "BZD", "Belize Dollar" },
//            { "CAD", "Canadian Dollar" },
//            { "CDF", "Congolese Franc" },
//            { "CHF", "Swiss Franc" },
//            { "CLF", "Chilean Unit of Account (UF)" },
//            { "CLP", "Chilean Peso" },
//            { "CNY", "Chinese Yuan" },
//            { "COP", "Colombian Peso" },
//            { "CRC", "Costa Rican Colón" },
//            { "CUP", "Cuban Peso" },
//            { "CVE", "Cape Verdean Escudo" },
//            { "CZK", "Czech Republic Koruna" },
//            { "DJF", "Djiboutian Franc" },
//            { "DKK", "Danish Krone" },
//            { "DOP", "Dominican Peso" },
//            { "DZD", "Algerian Dinar" },
//            { "EEK", "Estonian Kroon" },
//            { "EGP", "Egyptian Pound" },
//            { "ERN", "Eritrean Nakfa" },
//            { "ETB", "Ethiopian Birr" },
//            { "EUR", "Euro" },
//            { "FJD", "Fijian Dollar" },
//            { "FKP", "Falkland Islands Pound" },
//            { "GBP", "British Pound Sterling" },
//            { "GEL", "Georgian Lari" },
//            { "GGP", "Guernsey Pound" },
//            { "GHS", "Ghanaian Cedi" },
//            { "GIP", "Gibraltar Pound" },
//            { "GMD", "Gambian Dalasi" },
//            { "GNF", "Guinean Franc" },
//            { "GTQ", "Guatemalan Quetzal" },
//            { "GYD", "Guyanaese Dollar" },
//            { "HKD", "Hong Kong Dollar" },
//            { "HNL", "Honduran Lempira" },
//            { "HRK", "Croatian Kuna" },
//            { "HTG", "Haitian Gourde" },
//            { "HUF", "Hungarian Forint" },
//            { "IDR", "Indonesian Rupiah" },
//            { "ILS", "Israeli New Sheqel" },
//            { "IMP", "Manx pound" },
//            { "INR", "Indian Rupee" },
//            { "IQD", "Iraqi Dinar" },
//            { "IRR", "Iranian Rial" },
//            { "ISK", "Icelandic Króna" },
//            { "JEP", "Jersey Pound" },
//            { "JMD", "Jamaican Dollar" },
//            { "JOD", "Jordanian Dinar" },
//            { "JPY", "Japanese Yen" },
//            { "KES", "Kenyan Shilling" },
//            { "KGS", "Kyrgystani Som" },
//            { "KHR", "Cambodian Riel" },
//            { "KMF", "Comorian Franc" },
//            { "KPW", "North Korean Won" },
//            { "KRW", "South Korean Won" },
//            { "KWD", "Kuwaiti Dinar" },
//            { "KYD", "Cayman Islands Dollar" },
//            { "KZT", "Kazakhstani Tenge" },
//            { "LAK", "Laotian Kip" },
//            { "LBP", "Lebanese Pound" },
//            { "LKR", "Sri Lankan Rupee" },
//            { "LRD", "Liberian Dollar" },
//            { "LSL", "Lesotho Loti" },
//            { "LTL", "Lithuanian Litas" },
//            { "LVL", "Latvian Lats" },
//            { "LYD", "Libyan Dinar" },
//            { "MAD", "Moroccan Dirham" },
//            { "MDL", "Moldovan Leu" },
//            { "MGA", "Malagasy Ariary" },
//            { "MKD", "Macedonian Denar" },
//            { "MMK", "Myanma Kyat" },
//            { "MNT", "Mongolian Tugrik" },
//            { "MOP", "Macanese Pataca" },
//            { "MRO", "Mauritanian Ouguiya" },
//            { "MTL", "Maltese Lira" },
//            { "MUR", "Mauritian Rupee" },
//            { "MVR", "Maldivian Rufiyaa" },
//            { "MWK", "Malawian Kwacha" },
//            { "MXN", "Mexican Peso" },
//            { "MYR", "Malaysian Ringgit" },
//            { "MZN", "Mozambican Metical" },
//            { "NAD", "Namibian Dollar" },
//            { "NGN", "Nigerian Naira" },
//            { "NIO", "Nicaraguan Cródoba" },
//            { "NOK", "Norwegian Krone" },
//            { "NPR", "Nepalese Rupee" },
//            { "NZD", "New Zealand Dollar" },
//            { "OMR", "Omani Rial" },
//            { "PAB", "Panamanian Balboa" },
//            { "PEN", "Peruvian Nuevo Sol" },
//            { "PGK", "Papua New Guinean Kina" },
//            { "PHP", "Philippine Peso" },
//            { "PKR", "Pakistani Rupee" },
//            { "PLN", "Polish Zloty" },
//            { "PYG", "Paraguayan Guarani" },
//            { "QAR", "Qatari Rial" },
//            { "RON", "Romanian Leu" },
//            { "RSD", "Serbian Dinar" },
//            { "RUB", "Russian Ruble" },
//            { "RWF", "Rwandan Franc" },
//            { "SAR", "Saudi Riyal" },
//            { "SBD", "Solomon Islands Dollar" },
//            { "SCR", "Seychellois Rupee" },
//            { "SDG", "Sudanese Pound" },
//            { "SEK", "Swedish Krona" },
//            { "SGD", "Singapore Dollar" },
//            { "SHP", "Saint Helena Pound" },
//            { "SLL", "Sierra Leonean Leone" },
//            { "SOS", "Somali Shilling" },
//            { "SRD", "Surinamese Dollar" },
//            { "STD", "São Tomé and Príncipe Dobra" },
//            { "SVC", "Salvadoran Colón" },
//            { "SYP", "Syrian Pound" },
//            { "SZL", "Swazi Lilangeni" },
//            { "THB", "Thai Baht" },
//            { "TJS", "Tajikistani Somoni" },
//            { "TMT", "Turkmenistani Manat" },
//            { "TND", "Tunisian Dinar" },
//            { "TOP", "Tongan Paʻanga" },
//            { "TRY", "Turkish Lira" },
//            { "TTD", "Trinidad and Tobago Dollar" },
//            { "TWD", "New Taiwan Dollar" },
//            { "TZS", "Tanzanian Shilling" },
//            { "UAH", "Ukrainian Hryvnia" },
//            { "UGX", "Ugandan Shilling" },
//            { "USD", "United States Dollar" },
//            { "UYU", "Uruguayan Peso" },
//            { "UZS", "Uzbekistan Som" },
//            { "VEF", "Venezuelan Bolívar Fuerte" },
//            { "VND", "Vietnamese Dong" },
//            { "VUV", "Vanuatu Vatu" },
//            { "WST", "Samoan Tala" },
//            { "XAF", "CFA Franc BEAC" },
//            { "XAG", "Silver (troy ounce)" },
//            { "XAU", "Gold (troy ounce)" },
//            { "XCD", "East Caribbean Dollar" },
//            { "XDR", "Special Drawing Rights" },
//            { "XOF", "CFA Franc BCEAO" },
//            { "XPF", "CFP Franc" },
//            { "YER", "Yemeni Rial" },
//            { "ZAR", "South African Rand" },
//            { "ZMK", "Zambian Kwacha (pre-2013)" },
//            { "ZMW", "Zambian Kwacha" },
//            { "ZWL", "Zimbabwean Dollar" }
//        };
//        return arr;
//    }
    private Store addStore(String storeName, Country country, String timeZoneID, CountryOffice co, double lati, double longi, String address) {
        Store store = (Store) QueryMethods.findPlantByName(em, country, storeName);

        if (store == null) {
            store = new Store();
            store.setName(storeName);
            store.setCountry(country);
            store.setTimeZoneID(timeZoneID);
            store.setCountryOffice(co);
            store.setAddress(address);
            store.setLatitude(lati);
            store.setLongitude(longi);
            em.persist(store);

            co.getStores().add(store);

            return store;
        } else {
            System.out.println("Store " + storeName + " already exists in " + country.getName());

            return null;
        }
    }

    private ManufacturingFacility addManufacturingFacility(String mfName, Country country, String timeZoneID, CountryOffice co, double lati, double longi, String address) {
        ManufacturingFacility mf = (ManufacturingFacility) QueryMethods.findPlantByName(em, country, mfName);

        if (mf == null) {
            mf = new ManufacturingFacility();
            mf.setName(mfName);
            mf.setCountry(country);
            mf.setTimeZoneID(timeZoneID);
            mf.setCountryOffice(co);
            mf.setAddress(address);
            mf.setLatitude(lati);
            mf.setLongitude(longi);
            em.persist(mf);

            co.getManufacturingFacilities().add(mf);

            return mf;
        } else {
            System.out.println("Manufacturing Facility " + mfName + " already exists in " + country.getName());

            return null;
        }
    }

    private CountryOffice addCountryOffice(String coName, String code, Country country, String timeZoneID, double lati, double longi, String address) {
        CountryOffice co = (CountryOffice) QueryMethods.findPlantByName(em, country, coName);

        if (co == null) {
            co = new CountryOffice();
            co.setName(coName);
            co.setUrlCode(code);
            co.setCountry(country);
            co.setTimeZoneID(timeZoneID);
            co.setAddress(address);
            co.setLatitude(lati);
            co.setLongitude(longi);
            em.persist(co);

            return co;
        } else {
            System.out.println("Country Office  " + coName + " already exists in " + country.getName());

            return null;
        }
    }

    private Country addCountry(String countryName) {
        return this.addCountry(countryName, "");
    }

    private Country addCountry(String countryName, String countryCode) {
        Country country = QueryMethods.findCountryByName(em, countryName);

        if (country == null) {
            country = new Country();
            country.setName(countryName);
            country.setCode(countryCode);
            em.persist(country);
        }
        return country;
    }

    /**
     * Loads a sample data list of Country, Country Offices, Manufacturing
     * Facilities as well as Stores
     *
     * @return
     */
    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {

//        try {
        Country country;
        CountryOffice co;

        // Add all Countries listed in Java
        String[] locales = Locale.getISOCountries();

        for (String countryCode : locales) {
            Locale locale = new Locale("", countryCode);

            country = this.addCountry(locale.getDisplayCountry(), locale.getCountry());
            java.util.Currency javaCurrency = java.util.Currency.getInstance(locale);
            if (javaCurrency != null) {
                country.setCurrency(this.addCurrency(javaCurrency.getDisplayName(), javaCurrency.getCurrencyCode()));
            }
        }

//            String [][] currencyArray = this.getCurrencyArray();
//            for(int i=0; i<currencyArray.length; i++) {
//                this.addCurrency(currencyArray[i][1], currencyArray[i][0]);
//            }
        // Add Countries and Plants
        country = this.addCountry("Singapore");
        this.addGlobalHQ("Global HQ", country, "Asia/Singapore", 1.2881912, 103.805674, "317 Alexandra Road, Singapore 159965");
        co = this.addCountryOffice("Singapore", "sg", country, "Asia/Singapore", 1.2881912, 103.805674, "317 Alexandra Road, Singapore 159965");
        this.addStore("Alexandra", country, "Asia/Singapore", co, 1.2881912, 103.805674, "317 Alexandra Road, Singapore 159965");
        this.addStore("Tampines", country, "Asia/Singapore", co, 1.374183, 103.932748, "60 Tampines North Drive 2,  Singapore 528764");
        this.addManufacturingFacility("Tuas", country, "Asia/Singapore", co, 1.282891, 103.629289, "26 Tuas South Avenue 10, Singapore 637437");

        country = this.addCountry("Malaysia");
        co = this.addCountryOffice("Malaysia", "my", country, "Asia/Kuala_Lumpur", 3.0824863, 101.6362879, "907, 9, Jalan 9, Pjs 2, 46150 Petaling Jaya, Selangor, Malaysia");
        this.addStore("Johor Bahru - Kulai", country, "Asia/Kuala_Lumpur", co, 3.0824863, 101.6362879, "907, 9, Jalan 9, Pjs 2, 46150 Petaling Jaya, Selangor, Malaysia");

        country = this.addCountry("China");
        co = this.addCountryOffice("China", "cn", country, "Asia/Shanghai", 25.0298995, 102.7180134, "131 Baita Rd, Panlong, Kunming, Yunnan, China");
        this.addStore("Yunnan - Yuanjiang", country, "Asia/Shanghai", co, 25.0298995, 102.7180134, "131 Baita Rd, Panlong, Kunming, Yunnan, China");
        this.addManufacturingFacility("Su Zhou - Su Zhou Industrial Park", country, "Asia/Shanghai", co, 31.2970217, 120.5677953, "1326 Binhe Rd Huqiu, Suzhou, Jiangsu China, 215011");

        country = this.addCountry("Indonesia");
        co = this.addCountryOffice("Indonesia", "in", country, "Asia/Jakarta", -7.296718, 112.7601835, "Ruko Manyar Indah, Jalan Ngagel Jaya Selatan Blok J No.25-26, Surabaya, Jawa Timur 60284, Indonesia");
        this.addManufacturingFacility("Surabaya", country, "Asia/Jakarta", co, -7.296718, 112.7601835, "Ruko Manyar Indah, Jalan Ngagel Jaya Selatan Blok J No.25-26, Surabaya, Jawa Timur 60284, Indonesia");
        this.addManufacturingFacility("Sukabumi", country, "Asia/Jakarta", co, -6.9255318, 106.9295588, "Jl. Tipar Gede No.7, 13710 Indonesia");

        country = this.addCountry("Cambodia");
        co = this.addCountryOffice("Cambodia", "camb", country, "Asia/Phnom_Penh", 11.478303, 104.491256, "Kampong Speu Airport, Kampong Speu, Cambodia");
        this.addManufacturingFacility("Krong Chbar Mon", country, "Asia/Phnom_Penh", co, 11.478303, 104.491256, "Kampong Speu Airport, Kampong Speu, Cambodia");

        country = this.addCountry("Thailand");
        co = this.addCountryOffice("Thailand", "th", country, "Asia/Bangkok", 18.7085122, 98.9839431, "Saraphi District, Chiang Mai, Thailand");
        this.addStore("Bangkok - Ma Boon Krong", country, "Asia/Bangkok", co, 13.7447582, 100.5299468, "MBK Center, 444 Phayathai Rd, Bangkok, Pathumwan 10330, Thailand");
        this.addManufacturingFacility("Chiang Mai", country, "Asia/Bangkok", co, 18.7085122, 98.9839431, "Saraphi District, Chiang Mai, Thailand");

        country = this.addCountry("Vietnam");
        co = this.addCountryOffice("Vietnam", "viet", country, "Asia/Ho_Chi_Minh", 10.7843079, 106.6666286, "625 Cách Mạng Tháng 8, 15, 10, Hồ Chí Minh, Vietnam");
        this.addManufacturingFacility("Ho Chi Minh", country, "Asia/Ho_Chi_Minh", co, 10.7843079, 106.6666286, "625 Cách Mạng Tháng 8, 15, 10, Hồ Chí Minh, Vietnam");

        country = this.addCountry("Laos");
        co = this.addCountryOffice("Laos", "laos", country, "Asia/Vientiane", 17.9527945, 102.6235611, "Dongpalan Rd, Vientiane, Laos");
        this.addStore("Vientiane", country, "Asia/Vientiane", co, 17.9527945, 102.6235611, "Dongpalan Rd, Vientiane, Laos");

        country = this.addCountry("Canada");
        co = this.addCountryOffice("Canada", "ca", country, "Canada/Pacific", 43.6504454, -79.3837612, "St. Lawrence Market, 92 Front St E, Toronto, ON M5E 1C3, Canada");
        this.addStore("Toronto", country, "Canada/Eastern", co, 43.6504454, -79.3837612, "St. Lawrence Market, 92 Front St E, Toronto, ON M5E 1C3, Canada");

        em.flush();

        return true;
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//
//            return false;
//        }
    }

}
