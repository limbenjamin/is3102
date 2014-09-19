/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Message;
import IslandFurniture.EJB.Entities.MessageThread;
import IslandFurniture.EJB.Entities.Staff;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateful
public class ManageMessagesBean implements ManageMessagesBeanLocal {
    
    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private MessageThread messageThread;
    private Message message;
    private List<Staff> listStaff;
    
    @EJB
    private ManageUserAccountBeanLocal staffbean;

    @Override
    public void ManageMessagesBean(){
        
    }
    
    @Override
    public void createNewThread(String title, String recipients){
        messageThread = new MessageThread();
        messageThread.setTitle(title);
        StringTokenizer stringTokenizer = new StringTokenizer(recipients,",");
        listStaff = new ArrayList();
        while (stringTokenizer.hasMoreTokens()) {
            staff = staffbean.getStaff(stringTokenizer.nextToken());
            listStaff.add(staff);
        }
        messageThread.setRecipient(listStaff);
        em.persist(messageThread);
        em.flush();
        Iterator<Staff> iterator = listStaff.iterator();
        while (iterator.hasNext()) {
		staff = iterator.next();
                staff.getInbox().add(messageThread);
                em.merge(staff);
	}
        em.flush();
    }
    
    @Override
    public List<MessageThread> displayAllThreads(String username){
        staff = staffbean.getStaff(username);
        return staff.getInbox();
    }
    
    @Override
    public void unsubscribeFromThread(String username, Long threadId){
        staff = staffbean.getStaff(username);
        messageThread = getMessageThread(threadId);
        listStaff = messageThread.getRecipient();
        listStaff.remove(staff);
        em.persist(messageThread);
        staff.getInbox().remove(messageThread);
        em.persist(staff);
        em.flush();
    }
    
    @Override
    public MessageThread getMessageThread(Long id){
        Query query = em.createQuery("FROM MessageThread m where m.id=:id");
        query.setParameter("id", id);
        messageThread = (MessageThread) query.getSingleResult();
        return messageThread;
    }
    
    @Override
    public void sendMessage(String username, Long threadId, String content){
        staff = staffbean.getStaff(username);
        messageThread = getMessageThread(threadId);
        message = new Message();
        message.setStaff(staff);
        message.setThread(messageThread);
        message.setContent(content);
        message.setMsgTime(Calendar.getInstance());
        em.persist(message);
        listStaff = messageThread.getRecipient();
        Iterator<Staff> iterator = listStaff.iterator();
        while (iterator.hasNext()) {
		staff = iterator.next();
                staff.getInbox().get(staff.getInbox().indexOf(messageThread)).getMessages().add(message);
                em.merge(staff);
	}
        em.flush();
    }
}
