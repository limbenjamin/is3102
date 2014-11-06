/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.MessageThread;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Benjamin
 */
@Remote
public interface ManageMessagesBeanRemote {

    void ManageMessagesBean();

    void createNewThread(String title, String recipients);

    List<MessageThread> displayAllThreads(String username);

    MessageThread getMessageThread(Long id);

    void sendMessage(String username, Long threadId, String content);

    void unsubscribeFromThread(String username, Long threadId);
    
}
