/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Store;
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
        Store store = (Store) QueryMethods.findPlantByName(em, country, storeName);

        if (store == null) {
            store = new Store();
            store.setName(storeName);
            store.setCountry(country);
            em.persist(store);

            return store;
        } else {
            System.out.println("Store " + storeName + " already exists in " + country.getName());

            return null;
        }
    }

    private ManufacturingFacility addManufacturingFacility(String mfName, Country country) {
        ManufacturingFacility mf = (ManufacturingFacility) QueryMethods.findPlantByName(em, country, mfName);

        if (mf == null) {
            mf = new ManufacturingFacility();
            mf.setName(mfName);
            mf.setCountry(country);
            em.persist(mf);

            return mf;
        } else {
            System.out.println("Manufacturing Facility " + mfName + " already exists in " + country.getName());

            return null;
        }
    }

    private CountryOffice addCountryOffice(String coName, Country country) {
        CountryOffice co = (CountryOffice) QueryMethods.findPlantByName(em, country, coName);

        if (co == null) {
            co = new CountryOffice();
            co.setName(coName);
            co.setCountry(country);
            em.persist(co);

            return co;
        } else {
            System.out.println("Country Office  " + coName + " already exists in " + country.getName());

            return null;
        }
    }

    private Country addCountry(String countryName, String timeZoneID) {
        Country country = QueryMethods.findCountryByName(em, countryName);

        if (country == null) {
            country = new Country();
            country.setName(countryName);
            country.setTimeZoneID(timeZoneID);
            em.persist(country);

            return country;
        } else {
            System.out.println("The country \"" + countryName + "\" already exists!");

            return null;
        }

    }

    private FurnitureModel addFurnitureModel(String name) {
        FurnitureModel furniture = (FurnitureModel) QueryMethods.findFurnitureByName(em, name);

        if (furniture == null) {
            furniture = new FurnitureModel();
            furniture.setName(name);
            em.persist(furniture);

            return furniture;
        } else {
            System.out.println("Furniture Model \"" + name + "\" already exists");

            return null;
        }
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

        Country country;
        Store store;

        try {
            // Add Countries and Plants
            country = this.addCountry("Singapore", "Asia/Singapore");
            if (country != null) {
                this.addCountryOffice("Singapore", country);
                this.addStore("Alexandra", country);
                this.addStore("Tampines", country);
            }

            country = this.addCountry("Malaysia", "Asia/Kuala_Lumpur");
            if (country != null) {
                this.addCountryOffice("Malaysia", country);
                this.addStore("Johor Bahru - Kulai", country);
            }

            country = this.addCountry("China", "Asia/Shanghai");
            if (country != null) {
                this.addCountryOffice("China", country);
                this.addStore("Yunnan - Yuanjiang", country);
                this.addManufacturingFacility("Su Zhou - Su Zhou Industrial Park", country);
            }

            country = this.addCountry("Indonesia", "Asia/Jakarta");
            if (country != null) {
                this.addCountryOffice("Indonesia", country);
                this.addManufacturingFacility("Surabaya", country);
                this.addManufacturingFacility("Sukabumi", country);
            }

            country = this.addCountry("Cambodia", "Asia/Phnom_Penh");
            if (country != null) {
                this.addCountryOffice("Cambodia", country);
                this.addManufacturingFacility("Krong Chbar Mon", country);
            }

            country = this.addCountry("Thailand", "Asia/Bangkok");
            if (country != null) {
                this.addCountryOffice("Thailand", country);
                this.addStore("Bangkok - Ma Boon Krong", country);
            }

            country = this.addCountry("Vietnam", "Asia/Ho_Chi_Minh");
            if (country != null) {
                this.addCountryOffice("Vietnam", country);
                this.addManufacturingFacility("Chiang Mai", country);
            }

            country = this.addCountry("Laos", "Asia/Vientiane");
            if (country != null) {
                this.addCountryOffice("Laos", country);
                this.addStore("Vientiane", country);
            }

            // Add some Furniture Models
            this.addFurnitureModel("Swivel Chair");
            this.addFurnitureModel("Round Table");
            this.addFurnitureModel("Coffee Table");
            this.addFurnitureModel("Study Table - Dinosaur Edition");
            this.addFurnitureModel("Bedside Lamp H31");
            this.addFurnitureModel("Bathroom Rug E64");
            this.addFurnitureModel("Kitchen Stool");

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            
            return false;
        }
    }

}
