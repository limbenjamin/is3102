/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientContract;
import IslandFurniture.Entities.IngredientContractDetail;
import IslandFurniture.Entities.IngredientInventory;
import IslandFurniture.Entities.IngredientInventoryPK;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MenuItemDetail;
import IslandFurniture.Entities.Recipe;
import IslandFurniture.Entities.RecipeDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.MenuType;
import IslandFurniture.StaticClasses.QueryMethods;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadKitchenDataBean implements LoadKitchenDataBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private IngredientSupplier addIngredSupplier(CountryOffice co, String name, String email, String phoneNo) {
        IngredientSupplier ingredSupplier = QueryMethods.findIngredSupplierByNameAndCo(em, name, co);

        if (ingredSupplier == null) {
            ingredSupplier = new IngredientSupplier();
            ingredSupplier.setCo(co);
            ingredSupplier.setCountry(co.getCountry());
            ingredSupplier.setEmail(email);
            ingredSupplier.setName(name);
            ingredSupplier.setPhoneNumber(phoneNo);
            
            IngredientContract ingredientContract = new IngredientContract();
            ingredientContract.setIngredSupplier(ingredSupplier);
            ingredientContract.setIngredContractDetails(new ArrayList<>());
            
            ingredSupplier.setIngredContract(ingredientContract);
            em.persist(ingredSupplier);
        }

        return ingredSupplier;
    }

    private IngredientContractDetail addIngredContractDetail(IngredientContract ingredContract, Ingredient ingredient, int lotSize, double lotPrice, int leadTime) {
        IngredientContractDetail icd = new IngredientContractDetail();
        icd.setIngredContract(ingredContract);
        icd.setIngredient(ingredient);
        icd.setLotPrice(lotPrice);
        icd.setLotSize(lotSize);
        icd.setLeadTimeInDays(leadTime);

        return icd;
    }

    private Ingredient addIngredient(CountryOffice co, String name) {
        Ingredient ingred = QueryMethods.findIngredientByName(em, name);

        if (ingred == null) {
            ingred = new Ingredient();
            ingred.setCountryOffice(co);
            ingred.setName(name);
            em.persist(ingred);
        }

        return ingred;
    }

    private IngredientInventory addIngredientInv(Store store, Ingredient ingred, int initQty) {
        IngredientInventoryPK ingredInvPK = new IngredientInventoryPK(store.getId(), ingred.getId());
        IngredientInventory ingredInv = em.find(IngredientInventory.class, ingredInvPK);

        if (ingredInv == null) {
            ingredInv = new IngredientInventory();
            ingredInv.setStore(store);
            ingredInv.setIngredient(ingred);
            ingredInv.setQty(initQty);
            em.persist(ingredInv);

            store.getIngredInvents().add(ingredInv);
            em.merge(store);
        }

        return ingredInv;
    }

    private Dish addDish(CountryOffice co, String name) {
        Dish dish = QueryMethods.getDishByCountryOfficeAndName(em, co, name);

        if (dish == null) {
            dish = new Dish();
            Recipe recipe = new Recipe();
            dish.setRecipe(recipe);
            dish.setCo(co);
            dish.setName(name);

            em.persist(dish);
        }

        return dish;
    }

    private MenuItem addMenuItem(CountryOffice co, String name, MenuType menuType, double price, long points, boolean isAlaCarte) {
        MenuItem menuItem = QueryMethods.getMenuItemByCountryOfficeAndName(em, co, name);

        if (menuItem == null) {
            menuItem = new MenuItem();
            menuItem.setCountryOffice(co);
            menuItem.setName(name);
            menuItem.setMenuType(menuType);
            menuItem.setPrice(price);
            menuItem.setPointsWorth(points);
            menuItem.setAlaCarte(isAlaCarte);
            em.persist(menuItem);

            co.getMenuItems().add(menuItem);
            em.merge(co);
        }

        return menuItem;
    }

    private MenuItemDetail addMenuItemDetail(MenuItem menuItem, Dish dish, int qty) {
        MenuItemDetail menuItemDetail = new MenuItemDetail();
        menuItemDetail.setDish(dish);
        menuItemDetail.setQuantity(1);
        menuItemDetail.setMenuItem(menuItem);

        return menuItemDetail;
    }

    @Override
    public boolean loadSampleData() {
        // Declare required entity variables
        Random rand = new Random(1);
        final String[] supplierNames = {"Easy Food International", "Yums Inc.", "Royce Tasters", "Lowe Hunger", "Quality Food"};
        final String[] ingredNames = {"Pepper - 500g", "Heinz Tomato Ketchup - 500ml", "Kampong Chicken Thigh - 5kg", "White Fine Salt - 500g", "Red Carrots - 2kg", "Fragrant Rice - 10kg", "Garlic - 500g", "Shellots - 500g", "Blue Cheese - 2kg", "Canola Oil - 2L", "Bell Pepper (Green)", "Bell Pepper (Red)", "Bell Pepper (Yellow)", "Corn Starch - 1kg", "Refined Sugar - 500g", "Welsh Potato - 1kg", "Sesame Oil - 500ml", "Lemongrass - 200g"};
        final String[] dishNames = {"Baked Chicken Thigh", "Sugarcane Juice (Small)", "Sugarcane Juice (Medium)", "Sugarcane Juice (Large)", "Meatballs", "Vege Delight", "Ramly Burger", "Baked Potato", "Kampong Fried Rice"};
        final String[] menuItems = {"Chicken Combo Meal", "Kampong Combo", "Snackers Meal"};

        List<CountryOffice> coList = (List<CountryOffice>) em.createNamedQuery("getAllCountryOffices").getResultList();

        Ingredient ingred;
        List<Ingredient> ingredList;

        IngredientSupplier ingredSupplier;
        List<IngredientSupplier> ingredSupplierList;

        Dish dish;

        RecipeDetail recipeDetail;
        List<RecipeDetail> recipeDetailList;

        MenuItem menuItem;

        List<MenuItemDetail> menuItemDetailList;

        for (CountryOffice eachCo : coList) {
            // Add Ingredient Suppliers
            boolean hasSupplier = false;
            do {
                for (String eachName : supplierNames) {
                    if (rand.nextBoolean()) {
                        this.addIngredSupplier(eachCo, eachName, "sales@" + eachName.split(" ")[0] + ".com", (new Long(rand.nextInt(100000) + 65000000)).toString());
                        hasSupplier = true;
                        System.out.println("Ingredient Supplier added");
                    }
                }
            } while (!hasSupplier);

            ingredSupplierList = QueryMethods.getIngredSuppliersByCo(em, eachCo);

            // Add Ingredients
            for (String eachName : ingredNames) {
                if (rand.nextBoolean()) {
                    ingred = this.addIngredient(eachCo, eachName);
                    ingredSupplier = ingredSupplierList.get(rand.nextInt(ingredSupplierList.size()));
                    ingredSupplier.getIngredContract().getIngredContractDetails().add(this.addIngredContractDetail(ingredSupplier.getIngredContract(), ingred, rand.nextInt(6) * 50, rand.nextInt(51) + 50, rand.nextInt(7) + 5));
                    for (Store eachStore : eachCo.getStores()) {
                        this.addIngredientInv(eachStore, ingred, rand.nextInt(50) + 10);
                    }
                }
            }

            ingredList = QueryMethods.getIngredientListByCountryOffice(em, eachCo);

            // Add Dishes
            for (String eachName : dishNames) {
                dish = this.addDish(eachCo, eachName);

                // Attach RecipeDetails to Dishes
                recipeDetailList = new ArrayList();
                for (Ingredient eachIngred : ingredList) {
                    if (rand.nextBoolean()) {
                        recipeDetail = new RecipeDetail();
                        recipeDetail.setIngredient(eachIngred);
                        recipeDetail.setQuantity(rand.nextInt(100) / 3.0);
                        recipeDetail.setRecipe(dish.getRecipe());
                        em.persist(recipeDetail);

                        recipeDetailList.add(recipeDetail);
                    }
                }

                dish.getRecipe().setRecipeDetails(recipeDetailList);
                menuItem = this.addMenuItem(eachCo, eachName, MenuType.MAINS, (rand.nextInt(5) + 1) / 1.0, (rand.nextInt(5) + 1) * 10, true);
                menuItemDetailList = new ArrayList();
                menuItemDetailList.add(this.addMenuItemDetail(menuItem, dish, 1));
                menuItem.setMenuItemDetails(menuItemDetailList);
            }

            // Add Combo Meals (Manual Adding)
            menuItem = this.addMenuItem(eachCo, menuItems[0], MenuType.MAINS, 5.0, 500, false);
            menuItemDetailList = new ArrayList();
            menuItemDetailList.add(this.addMenuItemDetail(menuItem, QueryMethods.getDishByCountryOfficeAndName(em, eachCo, "Baked Chicken Thigh"), 1));
            menuItemDetailList.add(this.addMenuItemDetail(menuItem, QueryMethods.getDishByCountryOfficeAndName(em, eachCo, "Sugarcane Juice (Medium)"), 1));
            menuItemDetailList.add(this.addMenuItemDetail(menuItem, QueryMethods.getDishByCountryOfficeAndName(em, eachCo, "Baked Potato"), 1));
            menuItem.setMenuItemDetails(menuItemDetailList);

            menuItem = this.addMenuItem(eachCo, menuItems[1], MenuType.MAINS, 4.0, 400, false);
            menuItemDetailList = new ArrayList();
            menuItemDetailList.add(this.addMenuItemDetail(menuItem, QueryMethods.getDishByCountryOfficeAndName(em, eachCo, "Kampong Fried Rice"), 1));
            menuItemDetailList.add(this.addMenuItemDetail(menuItem, QueryMethods.getDishByCountryOfficeAndName(em, eachCo, "Sugarcane Juice (Medium)"), 1));
            menuItemDetailList.add(this.addMenuItemDetail(menuItem, QueryMethods.getDishByCountryOfficeAndName(em, eachCo, "Meatballs"), 1));
            menuItem.setMenuItemDetails(menuItemDetailList);

            menuItem = this.addMenuItem(eachCo, menuItems[2], MenuType.MAINS, 3.0, 400, false);
            menuItemDetailList = new ArrayList();
            menuItemDetailList.add(this.addMenuItemDetail(menuItem, QueryMethods.getDishByCountryOfficeAndName(em, eachCo, "Baked Potato"), 1));
            menuItemDetailList.add(this.addMenuItemDetail(menuItem, QueryMethods.getDishByCountryOfficeAndName(em, eachCo, "Sugarcane Juice (Small)"), 1));
            menuItem.setMenuItemDetails(menuItemDetailList);
        }

        return true;
    }
}
