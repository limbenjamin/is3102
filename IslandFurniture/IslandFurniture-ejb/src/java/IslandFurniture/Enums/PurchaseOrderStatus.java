/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Enums;

import java.io.Serializable;

/**
 *
 * @author Zee
 */
public enum PurchaseOrderStatus implements Serializable{
    PLANNED(0), CONFIRMED(1), DELIVERED(2), PAID(3);
    
    public int value;
    
    private PurchaseOrderStatus(int value) {
        this.value = value;
    }
    
    public static PurchaseOrderStatus getPurchaseOrderStatus(int value) {
        switch (value) {
            case 0:
                return PLANNED;
            case 1:
                return CONFIRMED;
            case 2:
                return DELIVERED;     
            case 3:
                return PAID;              
            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
}
