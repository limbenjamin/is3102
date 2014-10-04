/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.LogEntry;
import static IslandFurniture.Entities.Staff.SHA1Hash;
import IslandFurniture.EJB.ITManagement.ManageSystemAuditLogBeanLocal;
import IslandFurniture.StaticClasses.SendEmailByPost;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.*;
import javax.ejb.*;
import javax.persistence.*;

/**
 *
 * @author Benjamin and the IS3102 team
 */
@Stateful
public class ManageAuthenticationBean implements ManageAuthenticationBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private LogEntry logEntry;
    private String password;
    
    @EJB
    private ManageUserAccountBeanLocal staffbean;
    
    public ManageAuthenticationBean(){
        
    }
    
    @Override
    public boolean authenticate(String username, String password){
        Query query = em.createQuery("FROM Staff s where s.username=:username");
        query.setParameter("username", username);
        try{
            staff = (Staff) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){
            return false;
        }
        String correctPassword = staff.getPassword();
        String hashedPassword = SHA1Hash(staff.getSalt() + password);
        if (!correctPassword.equals(hashedPassword)){
            return false;
        }else{
            staff.setLastLogon(new Date());
            return true;
        }
    }
    
    @Override
    public void changePassword(String username, String newPassword){
        staff = staffbean.getStaff(username);
        staff.setPassword(newPassword);
        em.merge(staff);
        em.flush();
    }
    
    @Override
    public boolean forgetPassword(String username){
        try{
            Query query = em.createQuery("FROM Staff s where s.username=:username");
            query.setParameter("username", username);
            staff = (Staff) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){
            return false;
        }
        String forgottenPasswordCode = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        staff.setForgottenPasswordCode(forgottenPasswordCode);
        em.merge(staff);
        em.flush();
        try {
            SendEmailByPost.sendEmail("techsupport", staff.getEmailAddress(), "Password Reset Request", "Click this link to reset your password: https://localhost/erp/it/resetpassword.xhtml?code="+forgottenPasswordCode);
        } catch (Exception ex) {
            Logger.getLogger(ManageAuthenticationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    @Override
    public boolean resetPassword(String code, String password){
        Query query = em.createQuery("FROM Staff s where s.forgottenPasswordCode=:code");
        query.setParameter("code", code);
        try{
            staff = (Staff) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){
            return false;
        }
        staff.setPassword(password);
        staff.setForgottenPasswordCode(null);
        em.merge(staff);
        em.flush();
        return true;
    }
    
    @Override
    public void resetPasswordByAdmin(Long id){
        staff = (Staff) em.find(Staff.class, id);
        password = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        try {
            SendEmailByPost.sendEmail("techsupport", staff.getEmailAddress(), "Password Reseted", "Your new password is: "+password);
        } catch (Exception ex) {
            Logger.getLogger(ManageAuthenticationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        staff.setPassword(password);
        em.merge(staff);
        
    }
    
}
