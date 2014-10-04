/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Message;
import IslandFurniture.Entities.MessageThread;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.StaticClasses.TimeMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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
    private HashSet<Staff> hashStaff;
    
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
        hashStaff = new HashSet();
        while (stringTokenizer.hasMoreTokens()) {
            staff = staffbean.getStaff(stringTokenizer.nextToken().trim());
            hashStaff.add(staff);
        }
        Iterator<Staff> iterator2 = hashStaff.iterator();
        while (iterator2.hasNext()) {
		staff = iterator2.next();
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
        Calendar now=Calendar.getInstance();
        now.setTime(new Date());
        Plant plant = staffbean.getStaff(username).getPlant();
        message.setMsgTime(now);
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
