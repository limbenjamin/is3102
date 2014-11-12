/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
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
public class InitialiseServerBean {

    @PostConstruct
    public void initialiseServer() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
