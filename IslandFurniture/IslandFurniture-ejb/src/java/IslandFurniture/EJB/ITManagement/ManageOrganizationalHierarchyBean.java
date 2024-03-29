/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.ITManagement;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.GlobalHQ;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Store;
import IslandFurniture.StaticClasses.QueryMethods;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateful
public class ManageOrganizationalHierarchyBean implements ManageOrganizationalHierarchyBeanLocal, ManageOrganizationalHierarchyBeanRemote {

    @PersistenceContext
    private EntityManager em;

    private GlobalHQ globalhq;
    private Store store;
    private ManufacturingFacility mf;
    private CountryOffice co;
    private Country country;

    @Override
    public GlobalHQ addGlobalHQ(String name, Country country, String tz) {
        globalhq = (GlobalHQ) this.findPlantByName(country, name);

        if (globalhq == null) {
            globalhq = new GlobalHQ();
            globalhq.setName(name);
            globalhq.setCountry(country);
            globalhq.setTimeZoneID(tz);
            em.persist(globalhq);

            return globalhq;
        } else {
            System.out.println("Global HQ already exists");

            return null;
        }
    }

    @Override
    public Store addStore(String storeName, String tz, CountryOffice co) {
        store = (Store) this.findPlantByName(country, storeName);

        if (store == null) {
            store = new Store();
            store.setName(storeName);
            store.setCountry(co.getCountry());
            store.setCountryOffice(co);
            store.setTimeZoneID(tz);
            em.persist(store);

            return store;
        } else {
            System.out.println("Store " + storeName + " already exists in " + country.getName());

            return null;
        }
    }

    @Override
    public void editStore(Long storeId, String name, String tz, CountryOffice co) {
        store = (Store) em.find(Store.class, storeId);

        if (store != null) {
            store.setName(name);
            store.setCountry(co.getCountry());
            store.setCountryOffice(co);
            store.setTimeZoneID(tz);
        } else {
            System.out.println("Invalid Store ID");
        }
    }

    @Override
    public void deleteStore(Long storeId) {
        store = (Store) em.find(Store.class, storeId);
        country = store.getCountry();

        if (store != null) {
            em.remove(store);
            country.getPlants().remove(store);
            em.merge(country);
            em.flush();
        } else {
            System.out.println("Invalid Store ID");
        }
    }
    
    @Override
    public void deleteGlobalHQ(Long id) {
        GlobalHQ hq = em.find(GlobalHQ.class, id);
        em.remove(hq);
    }

    @Override
    public List<Store> displayStore() {
        Query q = em.createNamedQuery("getAllStores");
        return q.getResultList();
    }

    @Override
    public ManufacturingFacility addManufacturingFacility(String mfName, String tz, CountryOffice co) {
        mf = (ManufacturingFacility) this.findPlantByName(country, mfName);

        if (mf == null) {
            mf = new ManufacturingFacility();
            mf.setName(mfName);
            mf.setCountry(co.getCountry());
            mf.setCountryOffice(co);
            mf.setTimeZoneID(tz);
            em.persist(mf);

            return mf;
        } else {
            System.out.println("Manufacturing Facility " + mfName + " already exists in " + country.getName());

            return null;
        }
    }

    @Override
    public void editManufacturingFacility(Long mfId, String name, String tz, CountryOffice co) {
        mf = (ManufacturingFacility) em.find(ManufacturingFacility.class, mfId);

        if (mf != null) {
            mf.setName(name);
            mf.setCountry(co.getCountry());
            mf.setCountryOffice(co);
            mf.setTimeZoneID(tz);
        } else {
            System.out.println("Invalid ManufacturingFacility ID");
        }
    }

    @Override
    public void deleteManufacturingFacility(Long mfId) {
        mf = (ManufacturingFacility) em.find(ManufacturingFacility.class, mfId);
        country = mf.getCountry();

        if (mf != null) {
            em.remove(mf);
            country.getPlants().remove(mf);
            em.merge(country);
            em.flush();
        } else {
            System.out.println("Invalid ManufacturingFacility ID");
        }
    }

    @Override
    public List<ManufacturingFacility> displayManufacturingFacility() {
        Query q = em.createNamedQuery("getAllMFs");
        return q.getResultList();
    }

    @Override
    public List<Plant> displayPlant() {
        Query q = em.createQuery("SELECT p " + "FROM Plant p");
        return q.getResultList();
    }

    @Override
    public List<Plant> displayPlantbyInstanceOf(Plant plant) {
        if (plant instanceof Store) {
            // show all the stores in the country office
            Store store = (Store) plant;
            Query q = em.createQuery("SELECT s FROM Store s WHERE s.countryOffice.id=:countryOfficeId");
            q.setParameter("countryOfficeId", store.getCountryOffice().getId());
            Query r = em.createQuery("SELECT r FROM CountryOffice r WHERE r.id=:countryOfficeId");
            r.setParameter("countryOfficeId", store.getCountryOffice().getId());
            List<Plant> plantList = q.getResultList();
            plantList.addAll(r.getResultList());
            return plantList;
        } else if (plant instanceof ManufacturingFacility) {
            Query r = em.createQuery("SELECT m FROM CountryOffice m");
            Query m = em.createQuery("SELECT m FROM ManufacturingFacility m");
            List<Plant> plantList = r.getResultList();
            plantList.addAll(m.getResultList());
            return plantList;
        } else {
            // should show other country offices, the stores in the country, and all the manufacturing facilities
            CountryOffice co = (CountryOffice) plant;
            Query c = em.createQuery("SELECT t FROM Store t WHERE t.countryOffice.id=:countryOfficeId");
            c.setParameter("countryOfficeId", co.getId());
            Query y = em.createQuery("SELECT r FROM CountryOffice r");
            Query m = em.createQuery("SELECT m FROM ManufacturingFacility m");
            List<Plant> plantList = c.getResultList();
            plantList.addAll(y.getResultList());
            plantList.addAll(m.getResultList());
            return plantList;
        }
    }

    //  Function: To return a Plant based on PlantId
    @Override
    public Plant getPlantById(Long plantId) {
        Plant plant = (Plant) em.find(Plant.class, plantId);
        return plant;
    }

    @Override
    public CountryOffice addCountryOffice(String coName, Country country, String tz) {
        co = (CountryOffice) this.findPlantByName(country, coName);

        if (co == null) {
            co = new CountryOffice();
            co.setName(coName);
            co.setCountry(country);
            co.setTimeZoneID(tz);
            em.persist(co);

            return co;
        } else {
            System.out.println("Country Office  " + coName + " already exists in " + country.getName());

            return null;
        }
    }

    @Override
    public void editCountryOffice(Long coId, String name, Country country, String tz) {
        co = (CountryOffice) em.find(CountryOffice.class, coId);

        if (co != null) {
            co.setName(name);
            co.setCountry(country);
            co.setTimeZoneID(tz);
        } else {
            System.out.println("Invalid CountryOffice ID");
        }
    }

    @Override
    public void deleteCountryOffice(Long coId) {
        co = (CountryOffice) em.find(CountryOffice.class, coId);
        country = co.getCountry();

        if (co != null) {
            em.remove(co);
            country.getPlants().remove(co);
            em.merge(country);
            em.flush();
        } else {
            System.out.println("Invalid CountryOffice ID");
        }
    }

    @Override
    public List<CountryOffice> displayCountryOffice() {
        Query q = em.createQuery("SELECT c " + "FROM CountryOffice c");
        return q.getResultList();
    }

    @Override
    public Plant findPlantByNameOnly(String plantName) {
        Query q = em.createQuery("SELECT p FROM Plant p WHERE p.name=:name");
        q.setParameter("name", plantName);
        try {
            return (Plant) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    @Override
    public Plant findPlantByName(Country country, String plantName) {
        Query q = em.createNamedQuery("findPlantByName");
        q.setParameter("country", country);
        q.setParameter("name", plantName);

        try {
            return (Plant) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    @Override
    public Country findCountryByName(String countryName) {
        Query q = em.createNamedQuery("findCountryByName");
        q.setParameter("name", countryName);

        try {
            return (Country) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    @Override
    public List<Country> getCountries() {
        Query q = em.createNamedQuery("getAllCountry");
        return q.getResultList();
    }

    @Override
    public List<CountryOffice> getCountryOffices() {
        Query q = em.createQuery("SELECT c " + "FROM CountryOffice c");
        return q.getResultList();
    }

    @Override
    public CountryOffice findCountryOfficeByName(String countryOfficeName) {
        Query q = em.createNamedQuery("findCountryOfficeByName");
        q.setParameter("name", countryOfficeName);

        try {
            return (CountryOffice) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    @Override
    public Country addCountry(String countryName) {
        Country country = QueryMethods.findCountryByName(em, countryName);

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

}
