/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

/**
 * This class contains all the system constants used for business logic
 * processing
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class SystemConstants {

    // Number of Months from the start of current month where MSSR edits should be locked
    public static final int FORECAST_LOCKOUT_MONTHS = 2;

    // Number of Months from the start of current month where MSSR planning should be done
    public static final int FORECAST_HORIZON = 6;
    
    // Default weight multiplier given to sales figure of previous y-o-y month
    public static final double FORECAST_DEFAULT_WEIGHT = 1.25;

}
