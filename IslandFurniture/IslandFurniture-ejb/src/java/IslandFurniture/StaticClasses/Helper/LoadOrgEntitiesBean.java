/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.RemoteInterfaces.LoadOrgEntitiesBeanRemote;
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

    private Store addStore(String storeName, Country country) {
        Store store = new Store();
        store.setName(storeName);
        store.setCountry(country);
        em.persist(store);

        return store;
    }

    private ManufacturingFacility addManufacturingFacility(String mfName, Country country) {
        ManufacturingFacility mf = new ManufacturingFacility();
        mf.setName(mfName);
        mf.setCountry(country);
        em.persist(mf);

        return mf;
    }

    private CountryOffice addCountryOffice(String coName, Country country) {
        CountryOffice co = new CountryOffice();
        co.setName(coName);
        co.setCountry(country);
        em.persist(co);

        return co;
    }

    private Country addCountry(String countryName) {
        Country country = new Country();
        country.setName(countryName);
        em.persist(country);

        return country;
    }

    /**
     * Loads a sample data list of Country, Country Offices, Manufacturing Facilities as well as Stores
     * 
     * @return
     */
    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {
        // Add Countries and Plants
        try {
            Country country;

            country = this.addCountry("Singapore");
            this.addCountryOffice("Singapore", country);
            this.addStore("Alexandra", country);
            this.addStore("Tampines", country);

            country = this.addCountry("Malaysia");
            this.addCountryOffice("Malaysia", country);
            this.addStore("Johor Bahru - Kulai", country);

            country = this.addCountry("China");
            this.addCountryOffice("China", country);
            this.addStore("Yunnan - Yuanjiang", country);
            this.addManufacturingFacility("Su Zhou - Su Zhou Industrial Park", country);

            country = this.addCountry("Indonesia");
            this.addCountryOffice("Indonesia", country);
            this.addManufacturingFacility("Surabaya", country);
            this.addManufacturingFacility("Sukabumi", country);

            country = this.addCountry("Cambodia");
            this.addCountryOffice("Cambodia", country);
            this.addManufacturingFacility("Krong Chbar Mon", country);

            country = this.addCountry("Thailand");
            this.addCountryOffice("Thailand", country);
            this.addStore("Bangkok - Ma Boon Krong", country);

            country = this.addCountry("Vietnam");
            this.addCountryOffice("Vietnam", country);
            this.addManufacturingFacility("Chiang Mai", country);

            country = this.addCountry("Laos");
            this.addCountryOffice("Laos", country);
            this.addStore("Vientiane", country);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
