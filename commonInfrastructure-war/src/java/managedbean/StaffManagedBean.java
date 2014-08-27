/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

/**
 *
 * @author Benjamin
 */

import beans.StaffBean;
import java.io.Serializable;
import java.util.Random;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;


@Named
@SessionScoped
public class StaffManagedBean implements Serializable {
    private static final long serialVersionUID = 5443351151396868724L;
    private String username = null;
    private String password = null;
    private String status = null;
    @EJB
    private StaffBean StaffBean;
    
    public StaffManagedBean() {
    }
    
    public void login(ActionEvent event) {
        boolean result = StaffBean.authenticate(username, password);
        status = String.valueOf(result);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

}
