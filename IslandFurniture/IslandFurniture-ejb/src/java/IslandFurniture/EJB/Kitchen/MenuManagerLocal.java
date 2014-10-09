/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Enums.MenuType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface MenuManagerLocal {

    public List<MenuItem> getMenuItemList(CountryOffice co);

    public String editMenuItem(Long menuItemID, String name, Double price);

    public String deleteMenuItem(Long menuItemID, CountryOffice co);

    public String addMenuItem(String menuName, Double price, CountryOffice co, boolean alaCarte, MenuType menuType);

    public MenuItem getMenuItem(Long menuItemID);

    public String editMenuItemAttribute(Long menuItemID, MenuType menuType, Double price, boolean alaCarte);

    public String editMenuItemDetail(Long menuItemDetailID, Integer quantity);

    public String deleteMenuItemDetail(Long menuItemDetailID);

    public String addMenuItemDetail(Long menuItemID, Long dishID, Integer dishQuantity);
    
}
