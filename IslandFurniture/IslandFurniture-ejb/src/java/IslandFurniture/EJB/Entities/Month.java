/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

import java.io.Serializable;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 * I have added a helper function to translate enum to month. See
 * Helper.TranslateMonth
 */
public enum Month implements Serializable {

    JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SEP(9), OCT(10), NOV(11), DEC(12);

    public int value;

    private Month(int value) {
        this.value = value;

    }

}
