/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.Entities.Currency;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface CurrencyManagerLocal {

    public String retrieveFullList();

    public Double retrieveExchangeRate(String currencyList, String currencyCode);

    public List<String> getAllCurrency();
    
}
