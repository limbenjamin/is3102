/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.SupplyChain.ManageStockLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author a0101774
 */
@Stateless
@LocalBean
public class ManageStockBean {
    @EJB
    private ManageStockLocal manageStock;

    
}
