/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.Store;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author James
 */
@Local
public interface MobileAppServiceLocal {

    public Integer getCustomerCurrentPoint(Long cust_id);

    public String getCustomerMemberTier(Long cust_id);

    public String getCustomerName(Long cust_id);

    public List<CountryOffice> getAllCO();

    public List<Store> getAllStores(String code);

    public CountryOffice getCOFromName(String co);

    public Picture getPicture(String picID);

    public CountryOffice getCOFromID(String ID);

    public ShoppingList getShoppingList(String Cust_id, String StoreID);

    public Store getStoreFromID(String ID);

    public FurnitureModel getfmFromID(String ID);

   public FurnitureModel getFurnitureModelByNFCID(String NFC_ID) throws Exception;

    public Customer getcustomerFromid(String id);
    
}
