/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Enums;

import java.io.Serializable;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 *
 */
public enum Month implements Serializable {

    // Enum values are set from 0 to 11 to fit Calendar.MONTH values
    JAN(0), FEB(1), MAR(2), APR(3), MAY(4), JUN(5),
    JUL(6), AUG(7), SEP(8), OCT(9), NOV(10), DEC(11);

    public int value;

    // To replace Helper.translateMonth
    public static Month getMonth(int value) {
        switch (value) {
            case 0:
                return JAN;
            case 1:
                return FEB;
            case 2:
                return MAR;
            case 3:
                return APR;
            case 4:
                return MAY;
            case 5:
                return JUN;
            case 6:
                return JUL;
            case 7:
                return AUG;
            case 8:
                return SEP;
            case 9:
                return OCT;
            case 10:
                return NOV;
            case 11:
                return DEC;
            default:
                return null;
        }
    }

    private Month(int value) {
        this.value = value;
    }

}
