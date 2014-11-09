/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Hidden;

import IslandFurniture.EJB.ACRM.ACRMAnalyticsTimer;
import IslandFurniture.EJB.ACRM.ACRMAnalyticsTimerLocal;
import IslandFurniture.EJB.Manufacturing.ProductionPlanningSingletonLocal;
import IslandFurniture.StaticClasses.InitialiseServerBeanLocal;
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

    @EJB
    private ProductionPlanningSingletonLocal singleatom;

    @EJB
    private ACRMAnalyticsTimerLocal acrmatom;
    

    private String statusMessage;

    public void startMRPUpdates(AjaxBehaviorEvent event) {
        singleatom.setAdvanceWeek(12);
        statusMessage = "MRP Engine started . Moving 12 weeks in advance ! 1 week = 5s";
    }

    public void startACRMUpdates(AjaxBehaviorEvent event) {
        acrmatom.setAdvancePeriod(1);
        statusMessage = "ACRM ENGINE STARTED ! Moving 6 months in advance";
    }

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
