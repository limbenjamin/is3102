/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientContract;
import IslandFurniture.Entities.IngredientContractDetail;
import IslandFurniture.Entities.IngredientPurchaseOrder;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.Store;
import IslandFurniture.StaticClasses.QueryMethods;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774
 */
@Stateless
public class IngredientSupplierManager implements IngredientSupplierManagerLocal {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<IngredientSupplier> displaySupplierList(CountryOffice co) {
        List<IngredientSupplier> supplierList;
        try {
            return QueryMethods.getIngredSuppliersByCo(em, co);
        } catch (NoResultException NRE) {
            System.err.println("No results found");
            return null;
        }
    }

    @Override
    public IngredientSupplier getSupplier(Long id) {
        try {
            System.out.println("IngredientSupplierManager.getSupplier()");
            return em.find(IngredientSupplier.class, id);
        } catch (Exception ex) {
            System.err.println("No results found");
            return null;
        }
    }

    @Override
    public String addSupplier(String supplierName, String countryName, String phoneNo, String email, CountryOffice co) {
        Country country;
        IngredientSupplier supplier;
        IngredientContract ic;
        List<IngredientContractDetail> icList;
        try {
            System.out.println("IngredientSupplierManager.addSupplier()");
            country = QueryMethods.findCountryByName(em, countryName);
            supplier = QueryMethods.findIngredSupplierByNameAndCo(em, supplierName, co);
            if (supplier != null) {
                System.out.println("Ingredient Supplier " + supplierName + " already exists");
                return "" + supplier.getId() + "#Ingredient Supplier \"" + supplierName + "\" already exists in database. Redirect to Contract details";
            }

            supplier = new IngredientSupplier();
            ic = new IngredientContract();
            icList = new ArrayList<>();

            ic.setIngredSupplier(supplier);
            ic.setIngredContractDetails(icList);

            supplier.setName(supplierName);
            supplier.setCountry(country);
            supplier.setIngredContract(ic);
            supplier.setEmail(email);
            supplier.setPhoneNumber(phoneNo);
            supplier.setCo(co);

            em.persist(supplier);
            return "" + supplier.getId() + "#0";
        } catch (Exception ex) {
            System.err.println("No records found");
            return null;
        }
    }
    @Override
    public String deleteSupplier(Long id) {
        IngredientSupplier supplier = null;
        List<IngredientPurchaseOrder> IPOList;
        try {
            System.out.println("IngredientSupplierManager.addSupplier()");
            IPOList = em.createNamedQuery("getIngredientSuppliersPO", IngredientPurchaseOrder.class).getResultList();
            for(IngredientPurchaseOrder IPO : IPOList) {
                if(IPO.getIngredSupplier().getId().equals(id)) {
                    supplier = IPO.getIngredSupplier();
                    break;
                }
            }
            if(supplier != null) {
                System.out.println("Existing purchase order linked to Supplier. Unable to delete");
                return "Invalid deletion due to existing purchase order linked to Supplier \"" + supplier.getName() + "\"";
            } else {
                supplier = em.find(IngredientSupplier.class, id);
                for(IngredientContractDetail ICD : supplier.getIngredContract().getIngredContractDetails())
                    em.remove(ICD);
                em.remove(supplier.getIngredContract());
                em.remove(supplier);
                em.flush();
                return null;
            }
        } catch(Exception ex) {
            System.err.println("Existing purchase order linked Supplier. Unable to delete");
            return "Unexpected error occured";
        }
    }
    @Override
    public String editSupplier(Long id, String name, String countryName, String phoneNumber, String email) {
        IngredientSupplier supplier;
        Country country;
        try {
            System.out.println("IngredientSupplierManager.editSupplier()");
            System.out.println("Supplier Name is " + name + " Country Name is " + countryName + ". Number is " + phoneNumber + ". email is " + email);
            supplier = em.find(IngredientSupplier.class, id);
            country = QueryMethods.findCountryByName(em, countryName);
            if(name != null)
                supplier.setName(name);
            if(countryName != null) 
                supplier.setCountry(country);
            if(phoneNumber != null)
                supplier.setPhoneNumber(phoneNumber);
            if(email != null)
                supplier.setEmail(email);
            em.persist(supplier);
            return null;
        } catch(Exception ex) {
            System.out.println("Something went wrong");
            return "Unexpected error occured";
        }
    }
    @Override
    public String addIngredientContractDetail(Long supplierID, Long ingredientID, Integer size, Integer leadTime, Double lotPrice, CountryOffice co) {
        IngredientSupplier supplier;
        Ingredient ingredient;
        IngredientContract ic;
        List<IngredientContractDetail> icdList;
        IngredientContractDetail icd;
        try {
            System.out.println("IngredientSupplierManager.addIngredientContractDetails()"); 
            ingredient = em.find(Ingredient.class, ingredientID);
            supplier = em.find(IngredientSupplier.class, supplierID);
            
            icd = QueryMethods.getICDByIngredAndCo(em, ingredient, co);
            
            if(icd != null) {
                System.out.println("IngredientContractDetail already exist");
                return "Ingredient Contract Detail already exist";
            }
            else {            
                ic = supplier.getIngredContract();
                if(ic == null) {
                    ic = new IngredientContract();
                    icdList = new ArrayList();
                    ic.setIngredContractDetails(icdList);
                    ic.setIngredSupplier(supplier);
                    supplier.setIngredContract(ic);
                }
                icd = new IngredientContractDetail();
                icd.setIngredient(ingredient);
                icd.setLotPrice(lotPrice);
                icd.setLotSize(size);
                icd.setLeadTimeInDays(leadTime);
                icd.setIngredContract(ic);
                
                ic.getIngredContractDetails().add(icd);
                return null;
            }
        } catch(Exception ex) {
            ex.printStackTrace(); 
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    @Override
    public String editIngredientContractDetail(Long id, Integer size, Integer leadTime, Double lotPrice) {
        IngredientContractDetail icd;
        try {
            System.out.println("IngredientSupplierManager.editIngredientContractDetail()");
            icd = em.find(IngredientContractDetail.class, id);
            icd.setLeadTimeInDays(leadTime);
            icd.setLotSize(size); 
            icd.setLotPrice(lotPrice);
            em.persist(icd);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return "Unexpected error occured";
        }
    }
    @Override
    public String deleteIngredientContractDetail(Long id, Long supplierID) {
        IngredientContractDetail icd;
        IngredientSupplier supplier;
        try {
            System.out.println("IngredientSupplierManager.deleteIngredientContractDetail()");
            icd = em.find(IngredientContractDetail.class, id);
            supplier = em.find(IngredientSupplier.class, supplierID);
            supplier.getIngredContract().getIngredContractDetails().remove(icd);
            em.remove(icd);
            em.flush();
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong");
            return "Unexpected error occured";  
        }
    }
}
