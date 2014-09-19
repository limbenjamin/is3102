/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.Supplier;
import static IslandFurniture.StaticClasses.Helper.QueryMethods.findCountryByName;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774
 */
@Stateful
public class SupplierManager implements SupplierManagerLocal {

    @PersistenceContext 
    EntityManager em;
    
    public List<Supplier> displaySupplierList() {
        List<Supplier> supplierList;
        try {
            supplierList = em.createNamedQuery("getAllSuppliers", Supplier.class).getResultList();
            return supplierList;            
        } catch(NoResultException NRE) {
            System.err.println("No results found");
            return null;
        }
    }
    public void addSupplier(String supplierName, String countryName) {
        Country country;
        Supplier supplier;
        try {
            System.out.println("SupplierManager.addSupplier()");
            country = findCountryByName(em, countryName);
            supplier = new Supplier();
            supplier.setName(supplierName);
            supplier.setCountry(country);
            em.persist(supplier);
        } catch(NoResultException NRE) {
            System.err.println("No records found");
        }
    }
    public void editSupplier(Long id, String name, String countryName) {
        Supplier supplier;
        Country country;
        try {
            System.out.println("SupplierManager.editSupplier()");
            supplier = em.find(Supplier.class, id);
            country = findCountryByName(em, countryName);
            if(name != null)
                supplier.setName(name);
            if(countryName != null) 
                supplier.setCountry(country);
            em.persist(supplier);
        } catch(Exception ex) {
            System.out.println("Something went wrong");
        }
    }
    public void deleteSupplier(Long id) {
        Supplier supplier;
        try {
            System.out.println("SupplierManager.deleteSupplier()");
            supplier = em.find(Supplier.class, id);
            System.out.println("Don't forget to mind the constraints. I'm gonna just delete for now");
            em.remove(supplier);
            em.flush();
        } catch(Exception ex) {
            System.err.println("Something went wrong");
        }
    }
}
