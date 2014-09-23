/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import javax.ejb.Remote;

/**
 *
 * @author James
 */
@Remote
public interface ManageProductionPlanningRemote {

    void CreateProductionPlanFromForecast() throws Exception;

    void setMF(String MF_NAME) throws Exception;

    void createOrUpdateCapacity(String fmName, String mancFacNamem, int daily_max_capacity);

    void CreateProductionPlanFromForecast(int m, int year) throws Exception;

    double getReqCapacity(int year, int m) throws Exception;

    double getAvailCapacity(int year, int m) throws Exception;

    void createCapacityIFDoNotExist();

    void planWeekMPP(String MF_NAME, String furniture_model_name, int m, long year) throws Exception;

}
