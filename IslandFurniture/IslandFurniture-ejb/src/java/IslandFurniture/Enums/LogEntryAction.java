/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Enums;

import java.io.Serializable;

/**
 *
 * @author Benjamin
 */
public enum LogEntryAction implements Serializable{
    ACCESS(0), CREATE(1), MODIFY(2), DELETE(3);
    
    public int value;
    
    private LogEntryAction(int value) {
        this.value = value;
    }
    
    public static LogEntryAction getLogEntryAction(int value) {
        switch (value) {
            case 0:
                return ACCESS;
            case 1:
                return CREATE;
            case 2:
                return MODIFY;     
            case 3:
                return DELETE;  
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
