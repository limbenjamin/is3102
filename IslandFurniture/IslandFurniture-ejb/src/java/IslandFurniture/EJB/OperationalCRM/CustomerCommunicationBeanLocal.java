/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.CustChatThread;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Feedback;
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
    
    void createAnonymousFeedback(String name, String emailAddress, String phoneNo, String content, CountryOffice co);
    
    void createFeedback(Customer customer, String content, CountryOffice co);
    
    List<Feedback> getAllFeedbackForCO(CountryOffice co);
}
