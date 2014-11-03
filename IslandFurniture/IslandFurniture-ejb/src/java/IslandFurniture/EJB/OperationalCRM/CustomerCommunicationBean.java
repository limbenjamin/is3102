/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.CustChatMessage;
import IslandFurniture.Entities.CustChatThread;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Feedback;
import IslandFurniture.Entities.Staff;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
public class CustomerCommunicationBean implements CustomerCommunicationBeanLocal {
    
    @PersistenceContext
    EntityManager em;
    
    private CustChatMessage ccm;
    private CustChatThread cct;
    private List<CustChatThread> activeCCT;
    private List<CustChatMessage> listCCM;
    private Feedback feedback;
    
    @Override
    public List<CustChatThread> getActiveThreadFromCountry(CountryOffice co){
        Query query = em.createQuery("SELECT c FROM CustChatThread c WHERE c.isActive=:true AND c.countryOffice=:co");
        query.setParameter("co", co);
        query.setParameter("true", Boolean.TRUE);
        return query.getResultList();
    }
    
    @Override
    public Long createAnonymousThread(CountryOffice co){
        cct = new CustChatThread();
        cct.setCountryOffice(co);
        cct.setIsActive(Boolean.TRUE);
        cct.setHasUnread(Boolean.FALSE);
        em.persist(cct);
        em.flush();
        cct.setTitle("Customer #"+cct.getId());
        return cct.getId();
    }
    
    @Override
    public void readThread(CustChatThread cct){
        cct.setHasUnread(Boolean.FALSE);
    }
    
    @Override
    public Long getCustomerThread(CountryOffice co, Customer customer){
        Query query = em.createQuery("SELECT c FROM CustChatThread c WHERE c.customer=:customer");
        query.setParameter("customer", customer);
        try{
            cct =  (CustChatThread) query.getSingleResult();
            //customer might have changed country
            cct.setCountryOffice(co);
            cct.setIsActive(Boolean.TRUE);
            cct.setHasUnread(Boolean.FALSE);
        }catch(NoResultException nre){
            //customer has no thread, create one for him
            cct = new CustChatThread();
            cct.setCountryOffice(co);
            cct.setTitle(customer.getName());
            cct.setIsActive(Boolean.TRUE);
            cct.setHasUnread(Boolean.FALSE);
            cct.setCustomer(customer);
            em.persist(cct);
        }
        return cct.getId();
    }
    
    @Override
    public void endThread(Long threadId){
        cct = em.find(CustChatThread.class, threadId);
        cct.setIsActive(Boolean.FALSE);
    }
    
    @Override
    public void postMessage(Long threadId, String content, Boolean isStaff, Staff staff){
        cct = em.find(CustChatThread.class, threadId);
        ccm = new CustChatMessage();
        ccm.setContent(content);
        ccm.setThread(cct);
        em.persist(ccm);
        cct.getMessages().add(ccm);
        if (isStaff.equals(Boolean.TRUE)){
            ccm.setStaff(staff);
            cct.setHasUnread(Boolean.FALSE);
        }else{
            cct.setHasUnread(Boolean.TRUE);
        }
        
    }
    
    
    @Override
    public CustChatThread getThread(Long threadId){
        return em.find(CustChatThread.class, threadId);
    }
    @Override
    public void createAnonymousFeedback(String name, String emailAddress, String phoneNo, String content, CountryOffice co){
        feedback = new Feedback();
        feedback.setName(name);
        feedback.setEmail(emailAddress);
        feedback.setPhoneNo(phoneNo);
        feedback.setContent(content);
        feedback.setCountryOffice(co);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        System.err.println("herehere "+co.getName());
        cal.setTimeZone(TimeZone.getTimeZone(co.getTimeZoneID()));
        feedback.setSubmittedTime(cal);
        em.persist(feedback);
    }
    
    
    @Override
    public void createFeedback(Customer customer, String content, CountryOffice co){
        feedback = new Feedback();
        feedback.setMember(customer);
        feedback.setName(customer.getName());
        feedback.setEmail(customer.getEmailAddress());
        feedback.setPhoneNo(customer.getPhoneNo());
        feedback.setContent(content);
        feedback.setCountryOffice(co);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.setTimeZone(TimeZone.getTimeZone(co.getTimeZoneID()));
        feedback.setSubmittedTime(cal);
        em.persist(feedback);
    }
    
    @Override
    public List<Feedback> getAllFeedbackForCO(CountryOffice co){
        Query query = em.createQuery("SELECT c FROM Feedback c WHERE c.countryOffice=:co");
        query.setParameter("co", co);
        return query.getResultList();
    }
    
    
}
