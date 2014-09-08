/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.TEST;

import javax.ejb.Remote;

/**
 *
 * @author James
 */
@Remote
public interface JamesTestDataBeanRemote {
    public void createtestdata();
    public void persist(Object object);
}
