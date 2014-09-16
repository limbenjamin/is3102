/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Store;
import java.util.List;
import javax.ejb.LocalBean;
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
@LocalBean
public class ManageOrganizationalHierarchyBean implements ManageOrganizationalHierarchyBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    private Store store;
    private ManufacturingFacility mf;
    private CountryOffice co;
    private Country country;
    

    @Override
    public Store addStore(String storeName, Country country) {
        store = (Store) this.findPlantByName(country, storeName);

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
    
    @Override
    public void editStore(Long storeId, String name, Country country) {
        store = (Store) em.find(Store.class, storeId);

        if (store != null) {
            store.setName(name);
            store.setCountry(country);
            em.persist(store);
            em.flush();
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
    public List<Store> displayStore() {
        Query q = em.createQuery("SELECT s " + "FROM Store s");
        return q.getResultList();
    }

    @Override
    public ManufacturingFacility addManufacturingFacility(String mfName, Country country) {
        mf = (ManufacturingFacility) this.findPlantByName(country, mfName);

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
    
    @Override
    public void editManufacturingFacility(Long mfId, String name, Country country) {
        mf = (ManufacturingFacility) em.find(ManufacturingFacility.class, mfId);

        if (mf != null) {
            mf.setName(name);
            mf.setCountry(country);
            em.persist(mf);
            em.flush();
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
        Query q = em.createQuery("SELECT m " + "FROM ManufacturingFacility m");
        return q.getResultList();
    }
    
    @Override
    public List<Plant> displayPlant() {
        Query q = em.createQuery("SELECT p " + "FROM Plant p");
        return q.getResultList();
    }

    @Override
    public CountryOffice addCountryOffice(String coName, Country country) {
        co = (CountryOffice) this.findPlantByName(country, coName);

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
    
    @Override
    public void editCountryOffice(Long coId, String name, Country country) {
        co = (CountryOffice) em.find(CountryOffice.class, coId);

        if (co != null) {
            co.setName(name);
            co.setCountry(country);
            em.persist(co);
            em.flush();
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
    public List<Country> getCountries(){
        Query q = em.createQuery("SELECT c " + "FROM Country c");
        return q.getResultList();
    }
}
