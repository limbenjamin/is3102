/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import IslandFurniture.Entities.Country;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This is a bean that will be automatically run at deployment. Use this to
 * perform any initialisation activities.
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Startup
@Singleton
public class InitialiseServerBean implements InitialiseServerBeanLocal {




    Country country;

    private boolean updateMssrStarted;



    @PostConstruct
    public void initialiseServer() {
//        loaddata();
        this.updateMssrStarted = false;
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public boolean isUpdateMssrStarted() {
        return updateMssrStarted;
    }

    public void setUpdateMssrStarted(boolean updateMssrStarted) {
        this.updateMssrStarted = updateMssrStarted;
    }

    @Override
    public void startMssrTimer() {
        this.updateMssrStarted = true;
    }
    

    
    

}
