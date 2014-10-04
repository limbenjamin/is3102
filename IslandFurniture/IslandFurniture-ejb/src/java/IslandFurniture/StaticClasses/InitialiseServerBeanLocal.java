/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import javax.ejb.Local;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Local
public interface InitialiseServerBeanLocal {

    void startMssrTimer();

    boolean isUpdateMssrStarted();
    
}
