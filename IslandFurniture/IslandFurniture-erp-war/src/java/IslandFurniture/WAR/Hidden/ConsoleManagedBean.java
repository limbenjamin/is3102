/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Hidden;

import IslandFurniture.StaticClasses.Helper.InitialiseServerBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Named(value = "consoleManagedBean")
@ViewScoped
public class ConsoleManagedBean implements Serializable {

    @EJB
    private InitialiseServerBeanLocal initialiseServerBean;

    private String statusMessage;

    public void startMssrUpdates(AjaxBehaviorEvent event) {
        initialiseServerBean.startMssrTimer();
        statusMessage = "MSSR Update Timer Started. 5sec = 1 Month";
    }

    /**
     * Creates a new instance of ConsoleManagedBean
     */
    public ConsoleManagedBean() {
    }

    // Getters Setters
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    
}
