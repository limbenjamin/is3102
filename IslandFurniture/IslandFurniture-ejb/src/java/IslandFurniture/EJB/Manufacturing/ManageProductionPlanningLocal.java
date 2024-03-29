/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.Entities.Material;
import IslandFurniture.Entities.ProductionCapacity;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Entities.WeeklyMRPRecord;
import java.util.HashMap;
import javax.ejb.Local;

/**
 *
 * @author James
 */
@Local
public interface ManageProductionPlanningLocal {

    int getLeadTime(Material m);

    HashMap<Material, Long> getMaterialsNeededForCommited(int WeekNo, int Year, int Month);

    void CreateProductionPlanFromForecast() throws Exception;

    void setMF(String MF_NAME) throws Exception;

    ProductionCapacity createOrUpdateCapacity(String fmName, String mancFacNamem, int daily_max_capacity);

    void CreateProductionPlanFromForecast(int m, int year) throws Exception;

    double getReqCapacity(int year, int m) throws Exception;

    double getAvailCapacity(int year, int m) throws Exception;

    void createCapacityIFDoNotExist();

    void planWeekMPP(String MF_NAME, String furniture_model_name, int m, long year) throws Exception;

    void uncommitWPP(Integer wppID) throws Exception;

    void commitWPP(Integer wppID) throws Exception;

    ProcuredStockSupplier getSupplierSize(Material m);

    int getLotSize(Material m);

    void orderMaterials(int weekNo, int monthNo, int YearNo) throws Exception;

    void unOrderMaterials(int weekNo, int monthNo, int YearNo) throws Exception;

    void commitallWPP(int weekNo, int monthNo, int yearNo) throws Exception;

    void uncommitallWPP(int weekNo, int monthNo, int yearNo) throws Exception;

    void createPOForWeekMRP(int weekNo, int monthNo, int yearNo) throws Exception;

    void uncreatePOForWeekMRP(int weekNo, int monthNo, int yearNo) throws Exception;

    void createOrUpdateCapacityExternal(String fmName, String mancFacName, int daily_max_capacity);
    
    void calculatePO(WeeklyMRPRecord wMRP);
    
    void updatewMRP(Long id,Integer qty) throws Exception;

}