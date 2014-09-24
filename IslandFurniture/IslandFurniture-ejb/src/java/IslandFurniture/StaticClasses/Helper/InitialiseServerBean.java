/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBean;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * This is a bean that will be automatically run at deployment. Use this to
 * perform any initialisation activities.
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Startup
@Singleton
@LocalBean
public class InitialiseServerBean implements InitialiseServerBeanLocal {

    @EJB
    ManageOrganizationalHierarchyBean mohb;

    Country country;
    private boolean updateMssrStarted;

    @PostConstruct
    public void initialiseServer() {
        this.updateMssrStarted = false;
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public boolean isUpdateMssrStarted() {
        return updateMssrStarted;
    }

    public void setUpdateMssrStarted(boolean updateMssrStarted) {
        this.updateMssrStarted = updateMssrStarted;
    }

    // Extra Methods
    @Override
    public void startMssrTimer() {
        this.updateMssrStarted = true;
    }

}
