/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;



/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class ResetPasswordManagedBean {

    private String code;
    private String username = null;
    private String password = null;
    private String confirmPassword = null;
    
    @EJB
    private ManageAuthenticationBeanLocal authBean;
    
    /**
     * Creates a new instance of ResetPasswordManagedBean
     */
    
    
    public ResetPasswordManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "",""));
        try{
            code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
        }catch (Exception e){

        }
    }
    
    public String forgotpassword() {
        boolean result = authBean.forgetPassword(username);
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);
        if (result) {
             FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "An email has been sent to your account with further instructions",""));
        }else{
             FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error, unable to reset password",""));
        }
        return "forgotpassword";
    }
    
    
    public void resetpassword() {
        if (password.equals(confirmPassword)){
            boolean result = authBean.resetPassword(code,password);
            Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
            if (result) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Reset successful",""));
            }else{
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error, unable to reset password",""));
            }
        }else{
             FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error, passwords not the same",""));
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public ManageAuthenticationBeanLocal getAuthBean() {
        return authBean;
    }

    public void setAuthBean(ManageAuthenticationBeanLocal authBean) {
        this.authBean = authBean;
    }
    
    
    
}
