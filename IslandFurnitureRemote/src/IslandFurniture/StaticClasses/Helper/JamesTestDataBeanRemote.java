/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import IslandFurnitures.EJB.Exceptions.ProductionPlanExceedsException;
import IslandFurnitures.EJB.Exceptions.ProductionPlanNoCN;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author James
 */
@Remote
public interface JamesTestDataBeanRemote {
    public void createtestdata();
    public void persist(Object object);

    public void deletedata();
}
