/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import javax.ejb.Remote;

/**
 *
 * @author Benjamin
 */
@Remote
public interface MapPrivilegeDataBeanRemote {
    boolean loadSampleData();
}
