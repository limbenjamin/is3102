/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.StaticClasses.Helper;

import javax.ejb.Timeout;
import javax.ejb.Timer;

/**
 *
 * @author KamilulAshraf
 */
public interface GoodsIssueTimerSessionLocal {

    void cancelTimers();

    void createTimers();

    @Timeout
    void handleTimeout(Timer timer);
    
}
