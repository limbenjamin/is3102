/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.SupplyChain.StockManagerLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class MaterialManagedBean implements Serializable {
    @EJB
    private StockManagerLocal stockManager;
    
    public boolean addMaterial() {
        System.out.println("MaterialManagedBean: 1");
        stockManager.addMaterial();
        System.out.println("MaterialManagedBean: 2 ");
        return true;
    }
}
