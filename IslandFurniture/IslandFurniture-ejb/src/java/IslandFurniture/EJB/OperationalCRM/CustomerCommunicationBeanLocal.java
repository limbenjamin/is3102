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
    
    public List<CustChatThread> getActiveThreadFromCountry(CountryOffice co);
    
    public Long createAnonymousThread(CountryOffice co);
    
    public void postMessage(Long threadId, String content, Boolean isStaff);
}
