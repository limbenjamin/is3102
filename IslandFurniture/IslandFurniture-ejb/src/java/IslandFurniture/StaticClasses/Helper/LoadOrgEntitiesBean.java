/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.FurnitureTransaction;
import IslandFurniture.EJB.Entities.FurnitureTransactionDetail;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.RemoteInterfaces.LoadOrgEntitiesBeanRemote;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadOrgEntitiesBean implements LoadOrgEntitiesBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private Store addStore(String storeName, Country country) {
        Store store = (Store) this.findPlantByName(country, storeName);

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
        ManufacturingFacility mf = (ManufacturingFacility) this.findPlantByName(country, mfName);

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
        CountryOffice co = (CountryOffice) this.findPlantByName(country, coName);

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

    private Country addCountry(String countryName) {
        Country country = this.findCountryByName(countryName);

        if (country == null) {
            country = new Country();
            country.setName(countryName);
            em.persist(country);

            return country;
        } else {
            System.out.println("The country \"" + countryName + "\" already exists!");

            return null;
        }

    }

    private FurnitureModel addFurnitureModel(String name) {
        FurnitureModel furniture = (FurnitureModel) this.findFurnitureByName(name);

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

    private FurnitureTransaction addFurnitureTransaction(Store store, List<FurnitureTransactionDetail> fTransDetails) {
        FurnitureTransaction fTrans = new FurnitureTransaction();
        fTrans.setStore(store);
        for (FurnitureTransactionDetail eachDetail : fTransDetails) {
            eachDetail.setFurnitureTransaction(fTrans);
        }
        fTrans.setFurnitureTransactionDetails(fTransDetails);
        
        em.persist(fTrans);

        System.out.println(fTransDetails.get(0).getFurnitureTransaction());
        
        return fTrans;
    }

    private FurnitureTransactionDetail addFurnitureTransactionDetail(FurnitureModel furniture, int qty) {
        FurnitureTransactionDetail fTransDetail = new FurnitureTransactionDetail();
        fTransDetail.setFurnitureModel(furniture);
        fTransDetail.setQty(qty);

        return fTransDetail;
    }

    private Plant findPlantByName(Country country, String plantName) {
        Query q = em.createNamedQuery("findPlantByName");
        q.setParameter("country", country);
        q.setParameter("name", plantName);

        try {
            return (Plant) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    private Country findCountryByName(String countryName) {
        Query q = em.createNamedQuery("findCountryByName");
        q.setParameter("name", countryName);

        try {
            return (Country) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    private FurnitureModel findFurnitureByName(String furnitureName) {
        Query q = em.createNamedQuery("findFurnitureByName");
        q.setParameter("name", furnitureName);

        try {
            return (FurnitureModel) q.getSingleResult();
        } catch (NoResultException nrex) {
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
        FurnitureTransaction fTrans;
        List<FurnitureTransactionDetail> fTransDetails = new ArrayList();

        try {
            // Add Countries and Plants
            country = this.addCountry("Singapore");
            if (country != null) {
                this.addCountryOffice("Singapore", country);
                this.addStore("Alexandra", country);
                this.addStore("Tampines", country);
            }

            country = this.addCountry("Malaysia");
            if (country != null) {
                this.addCountryOffice("Malaysia", country);
                this.addStore("Johor Bahru - Kulai", country);
            }

            country = this.addCountry("China");
            if (country != null) {
                this.addCountryOffice("China", country);
                this.addStore("Yunnan - Yuanjiang", country);
                this.addManufacturingFacility("Su Zhou - Su Zhou Industrial Park", country);
            }

            country = this.addCountry("Indonesia");
            if (country != null) {
                this.addCountryOffice("Indonesia", country);
                this.addManufacturingFacility("Surabaya", country);
                this.addManufacturingFacility("Sukabumi", country);
            }

            country = this.addCountry("Cambodia");
            if (country != null) {
                this.addCountryOffice("Cambodia", country);
                this.addManufacturingFacility("Krong Chbar Mon", country);
            }

            country = this.addCountry("Thailand");
            if (country != null) {
                this.addCountryOffice("Thailand", country);
                this.addStore("Bangkok - Ma Boon Krong", country);
            }

            country = this.addCountry("Vietnam");
            if (country != null) {
                this.addCountryOffice("Vietnam", country);
                this.addManufacturingFacility("Chiang Mai", country);
            }

            country = this.addCountry("Laos");
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

            // Add Transactions for stores
            store = (Store) this.findPlantByName(this.findCountryByName("Singapore"), "Alexandra");

            fTransDetails.add(this.addFurnitureTransactionDetail(this.findFurnitureByName("Swivel Chair"), 2));
            fTransDetails.add(this.addFurnitureTransactionDetail(this.findFurnitureByName("Round Table"), 4));
            fTrans = this.addFurnitureTransaction(store, fTransDetails);

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

}
