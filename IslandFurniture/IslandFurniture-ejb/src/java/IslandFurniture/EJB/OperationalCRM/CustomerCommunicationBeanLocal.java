/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.CustChatThread;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface CustomerCommunicationBeanLocal {
    
    List<CustChatThread> getActiveThreadFromCountry(CountryOffice co);
    
    Long createAnonymousThread(CountryOffice co);
    
    void postMessage(Long threadId, String content, Boolean isStaff);
    
    CustChatThread getThread(Long threadId);
    
    void endAnonymousThread(Long threadId);
}
