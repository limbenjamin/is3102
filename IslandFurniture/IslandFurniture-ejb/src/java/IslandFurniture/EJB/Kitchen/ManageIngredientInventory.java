/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.IngredientInventory;
import IslandFurniture.Entities.IngredientInventoryPK;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Kamilul Ashraf
 *
 */
@Stateful
public class ManageIngredientInventory implements ManageIngredientInventoryLocal {

    @PersistenceContext
    EntityManager em;

    private Ingredient ingredient;
    private IngredientInventory ingredientInventory;
    private IngredientInventoryPK ingredientInventoryPK;

//  Function: To add Ingredient Inventory
    @Override
    public void createIngredientInventory(Plant plant, Long ingredientId, Integer qty, Integer threshold) {
        ingredientInventory = new IngredientInventory();
        ingredientInventory.setStore((Store) plant);
        ingredientInventory.setIngredient((Ingredient) em.find(Ingredient.class, ingredientId));
        ingredientInventory.setThreshold(threshold);
        ingredientInventory.setQty(qty);
        em.persist(ingredientInventory);
        em.flush();
    }

//  Function: To edit Ingredient Inventory
    @Override
    public void editIngredientInventory(IngredientInventory ingredientInventoryUpdated) {
        IngredientInventoryPK pk = new IngredientInventoryPK(ingredientInventoryUpdated.getStore().getId(), ingredientInventoryUpdated.getIngredient().getId());
        ingredientInventory = (IngredientInventory) em.find(IngredientInventory.class, pk);
        ingredientInventory.setQty(ingredientInventoryUpdated.getQty());
        ingredientInventory.setThreshold(ingredientInventoryUpdated.getThreshold());
        em.merge(ingredientInventory);
        em.flush();
    }

//  Function: To delete Ingredient Inventory
    @Override
    public void deleteIngredientInventory(IngredientInventory ingredientInventory) {
        ingredientInventoryPK = new IngredientInventoryPK(ingredientInventory.getStore().getId(), ingredientInventory.getIngredient().getId());
        ingredientInventory = (IngredientInventory) em.find(IngredientInventory.class, ingredientInventoryPK);
        em.remove(ingredientInventory);
        em.flush();
    }

    //  Function: To display list of IngredientInventory
    @Override
    public List<IngredientInventory> viewIngredientInventory(Plant plant) {
        Query q = em.createQuery("SELECT s FROM IngredientInventory s WHERE s.store.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    //  Function: To display list of IngredientInventory
    @Override
    public List<Ingredient> viewIngredient(Plant plant) {
        Store store = (Store) plant;
        Query q = em.createQuery("SELECT s FROM Ingredient s WHERE s.countryOffice.id=:id");
        q.setParameter("id", store.getCountryOffice().getId());
        return q.getResultList();
    }

//  Function: To get Ingredient Inventory Entity
    @Override
    public IngredientInventory getIngredientInventory(Plant plant, Long ingredientId) {
        ingredientInventoryPK = new IngredientInventoryPK(plant.getId(), ingredientId);
        ingredientInventory = (IngredientInventory) em.find(IngredientInventory.class, ingredientInventoryPK);
        return ingredientInventory;
    }

//  Function: To edit Ingredient Inventory quantity
    @Override
    public void editIngredientInventoryQty(IngredientInventory si, int qty) {
        ingredientInventoryPK = new IngredientInventoryPK(si.getStore().getId(), si.getIngredient().getId());
        ingredientInventory = (IngredientInventory) em.find(IngredientInventory.class, ingredientInventoryPK);
        ingredientInventory.setQty(qty);
        em.merge(ingredientInventory);
        em.flush();
    }
    
    //  Function: To get Ingredient Entity
    @Override
    public Ingredient getIngredient(Long ingredientId) {
        ingredient = (Ingredient) em.find(Ingredient.class, ingredientId);
        return ingredient;
    }

}
