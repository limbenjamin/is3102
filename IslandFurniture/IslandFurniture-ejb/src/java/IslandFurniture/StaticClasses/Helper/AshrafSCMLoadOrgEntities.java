/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.RemoteInterfaces.AshrafSCMLoadOrgEntitiesRemote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author KamilulAshraf
 */
@Stateless
public class AshrafSCMLoadOrgEntities implements AshrafSCMLoadOrgEntitiesRemote{
   
    @PersistenceContext
    private EntityManager em;

    private Store addStore(String storeName, Country country) {
        Store store = new Store();
        store.setName(storeName);
        store.setCountry(country);
        em.persist(store);
        em.flush();
        return store;
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

    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {

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


            country = this.addCountry("Indonesia");
            this.addCountryOffice("Indonesia", country);

            country = this.addCountry("Cambodia");
            this.addCountryOffice("Cambodia", country);

            country = this.addCountry("Thailand");
            this.addCountryOffice("Thailand", country);
            this.addStore("Bangkok - Ma Boon Krong", country);

            country = this.addCountry("Vietnam");
            this.addCountryOffice("Vietnam", country);

            country = this.addCountry("Laos");
            this.addCountryOffice("Laos", country);
            this.addStore("Vientiane", country);

            return true;

    }
}
