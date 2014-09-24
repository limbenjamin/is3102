/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.GlobalHQ;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Store;
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

    private GlobalHQ addGlobalHQ(String name, Country country, String timeZoneID) {
        GlobalHQ globalhq = (GlobalHQ) QueryMethods.findPlantByName(em, country, name);

        if (globalhq == null) {
            globalhq = new GlobalHQ();
            globalhq.setName(name);
            globalhq.setCountry(country);
            globalhq.setTimeZoneID(timeZoneID);
            em.persist(globalhq);

            return globalhq;
        } else {
            System.out.println("Global HQ already exists");

            return null;
        }
    }

    private Store addStore(String storeName, Country country, String timeZoneID, CountryOffice co) {
        Store store = (Store) QueryMethods.findPlantByName(em, country, storeName);

        if (store == null) {
            store = new Store();
            store.setName(storeName);
            store.setCountry(country);
            store.setTimeZoneID(timeZoneID);
            store.setCountryOffice(co);
            em.persist(store);

            co.getStores().add(store);

            return store;
        } else {
            System.out.println("Store " + storeName + " already exists in " + country.getName());

            return null;
        }
    }

    private ManufacturingFacility addManufacturingFacility(String mfName, Country country, String timeZoneID, CountryOffice co) {
        ManufacturingFacility mf = (ManufacturingFacility) QueryMethods.findPlantByName(em, country, mfName);

        if (mf == null) {
            mf = new ManufacturingFacility();
            mf.setName(mfName);
            mf.setCountry(country);
            mf.setTimeZoneID(timeZoneID);
            mf.setCountryOffice(co);
            em.persist(mf);

            co.getManufacturingFacilities().add(mf);

            return mf;
        } else {
            System.out.println("Manufacturing Facility " + mfName + " already exists in " + country.getName());

            return null;
        }
    }

    private CountryOffice addCountryOffice(String coName, Country country, String timeZoneID) {
        CountryOffice co = (CountryOffice) QueryMethods.findPlantByName(em, country, coName);

        if (co == null) {
            co = new CountryOffice();
            co.setName(coName);
            co.setCountry(country);
            co.setTimeZoneID(timeZoneID);
            em.persist(co);

            return co;
        } else {
            System.out.println("Country Office  " + coName + " already exists in " + country.getName());

            return null;
        }
    }

    private Country addCountry(String countryName) {
        Country country = QueryMethods.findCountryByName(em, countryName);

        if (country == null) {
            country = new Country();
            country.setName(countryName);
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

        try {
            Country country;
            CountryOffice co;

            // Add ll Countries listed in Java
            String[] locales = Locale.getISOCountries();

            for (String countryCode : locales) {
                Locale locale = new Locale("", countryCode);

                this.addCountry(locale.getDisplayCountry());
            }

            // Add Countries and Plants
            country = this.addCountry("Singapore");

            this.addGlobalHQ("Global HQ", country, "Asia/Singapore");
            co = this.addCountryOffice("Singapore", country, "Asia/Singapore");
            this.addStore("Alexandra", country, "Asia/Singapore", co);
            this.addStore("Tampines", country, "Asia/Singapore", co);
            this.addManufacturingFacility("Tuas", country, "Asia/Singapore", co);

            country = this.addCountry("Malaysia");
            co = this.addCountryOffice("Malaysia", country, "Asia/Kuala_Lumpur");
            this.addStore("Johor Bahru - Kulai", country, "Asia/Kuala_Lumpur", co);

            country = this.addCountry("China");
            co = this.addCountryOffice("China", country, "Asia/Shanghai");
            this.addStore("Yunnan - Yuanjiang", country, "Asia/Shanghai", co);
            this.addManufacturingFacility("Su Zhou - Su Zhou Industrial Park", country, "Asia/Shanghai", co);

            country = this.addCountry("Indonesia");
            co = this.addCountryOffice("Indonesia", country, "Asia/Jakarta");
            this.addManufacturingFacility("Surabaya", country, "Asia/Jakarta", co);
            this.addManufacturingFacility("Sukabumi", country, "Asia/Jakarta", co);

            country = this.addCountry("Cambodia");
            co = this.addCountryOffice("Cambodia", country, "Asia/Phnom_Penh");
            this.addManufacturingFacility("Krong Chbar Mon", country, "Asia/Phnom_Penh", co);

            country = this.addCountry("Thailand");
            co = this.addCountryOffice("Thailand", country, "Asia/Bangkok");
            this.addStore("Bangkok - Ma Boon Krong", country, "Asia/Bangkok", co);
            this.addManufacturingFacility("Chiang Mai", country, "Asia/Bangkok", co);

            country = this.addCountry("Vietnam");
            co = this.addCountryOffice("Vietnam", country, "Asia/Ho_Chi_Minh");
            this.addManufacturingFacility("Ho Chi Minh", country, "Asia/Ho_Chi_Minh", co);

            country = this.addCountry("Laos");
            co = this.addCountryOffice("Laos", country, "Asia/Vientiane");
            this.addStore("Vientiane", country, "Asia/Vientiane", co);

            country = this.addCountry("Canada");
            co = this.addCountryOffice("Canada", country, "Canada/Pacific");
            this.addStore("Toronto", country, "Canada/Eastern", co);

            em.flush();

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false;
        }
    }

}
