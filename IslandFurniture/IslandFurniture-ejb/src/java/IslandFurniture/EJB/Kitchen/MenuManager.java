/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MenuItemDetail;
import IslandFurniture.Enums.MenuType;
import static IslandFurniture.StaticClasses.QueryMethods.getMenuItemByCountryOfficeAndName;
import static IslandFurniture.StaticClasses.QueryMethods.getMenuItemListByCountryOffice;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774
 */
@Stateful
public class MenuManager implements MenuManagerLocal {

    @PersistenceContext
    EntityManager em;
    
    public List<MenuItem> getMenuItemList(CountryOffice co) {
        List<MenuItem> menuItemList;
        try {
            System.out.println("MenuManager.getMenuItemList()");
            menuItemList = getMenuItemListByCountryOffice(em, co);
            return menuItemList;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
    public MenuItem getMenuItem(Long menuItemID) {
        MenuItem menuItem;
        try {
            System.out.println("MenuManager.getMenuList()");
            menuItem = em.find(MenuItem.class, menuItemID);
            return menuItem;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
    public String addMenuItem(String menuName, Double price, CountryOffice co, boolean alaCarte, MenuType menuType) {
        MenuItem menuItem;
        String msg = null;
        List<MenuItemDetail> menuItemDetails;
        try {
            System.out.println("MenuManager.addMenuItem()");
            menuItem = getMenuItemByCountryOfficeAndName(em, co, menuName);
            if(menuItem != null) {
                System.out.println("Menu Item \"" + menuName + "\" already exists. Directing to Menu Details");
                msg = "" + menuItem.getId() + "#Menu Item " + menuName + " already exists. Directing to Menu Item Details";     
            } else {
                menuItem = new MenuItem();
                menuItemDetails = new ArrayList<MenuItemDetail>();
                
                menuItem.setName(menuName);
                menuItem.setPrice(price);
                menuItem.setAlaCarte(alaCarte);
                menuItem.setMenuType(menuType);
                menuItem.setCountryOffice(co);
                menuItem.setMenuItemDetails(menuItemDetails);
                em.persist(menuItem);
                msg = "" + menuItem.getId() + "#0";
            }
            return msg;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String deleteMenuItem(Long menuItemID, CountryOffice co) {
        MenuItem menuItem;
        List<MenuItemDetail> menuItemDetails;
        try {
            System.out.println("MenuManager.deleteMenuItem()");
            menuItem = em.find(MenuItem.class, menuItemID);
            menuItemDetails = menuItem.getMenuItemDetails();
            for(MenuItemDetail i : menuItemDetails) 
                em.remove(i);
            em.remove(menuItem);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String editMenuItem(Long menuItemID, String name, Double price) {
        MenuItem menuItem;
        try {
            System.out.println("MenuManager.editMenuItem()");
            menuItem = em.find(MenuItem.class, menuItemID);
            menuItem.setName(name);
            menuItem.setPrice(price);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String editMenuItemAttribute(Long menuItemID, MenuType menuType, Double price, boolean alaCarte) {
        MenuItem menuItem;
        try {
            System.out.println("MenuManager.editMenuItemAttribute()");
            menuItem = em.find(MenuItem.class, menuItemID);
            menuItem.setPrice(price);
            menuItem.setMenuType(menuType);
            menuItem.setAlaCarte(alaCarte);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String editMenuItemDetail(Long menuItemDetailID, Integer quantity) {
        MenuItemDetail menuItemDetail;
        try {
            System.out.println("MenuManager.editMenuItemDetail()");
            menuItemDetail = em.find(MenuItemDetail.class, menuItemDetailID);
            menuItemDetail.setQuantity(quantity);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String deleteMenuItemDetail(Long menuItemDetailID) {
        MenuItemDetail menuItemDetail;
        try {
            System.out.println("MenuManager.deleteMenuItemDetail()");
            menuItemDetail = em.find(MenuItemDetail.class, menuItemDetailID);
            em.remove(menuItemDetail);
            return null; 
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            ex.printStackTrace();
            return "Unexpected error occured";
        }
    }
    public String addMenuItemDetail(Long menuItemID, Long dishID, Integer dishQuantity) {
        MenuItem menuItem;
        MenuItemDetail menuItemDetail = null;
        Dish dish;
        try {
            System.out.println("MenuManager.deleteMenuItemDetail()");
            menuItem = em.find(MenuItem.class, menuItemID);
            dish = em.find(Dish.class, dishID);
            
            for(MenuItemDetail detail : menuItem.getMenuItemDetails()) {
                if(detail.getDish().equals(dish)) {
                    menuItemDetail = detail;
                    break;
                }
            }
            if(menuItemDetail != null) {
                System.out.println("Invalid addition: Menu Item Detail already exists");
                return "Invalid addition: Menu Item Detail already exists";
            } else {
                menuItemDetail = new MenuItemDetail();
                menuItemDetail.setMenuItem(menuItem); 
                menuItemDetail.setDish(dish);
                menuItemDetail.setQuantity(dishQuantity);
                menuItem.getMenuItemDetails().add(menuItemDetail);
                System.out.println("Successfully added new Menu Item Detail");
            }
            em.flush();
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
}
