/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses;

import javax.ejb.Remote;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Remote
public interface LoadSalesForecastBeanRemote {

    boolean loadSampleData();
    
}
