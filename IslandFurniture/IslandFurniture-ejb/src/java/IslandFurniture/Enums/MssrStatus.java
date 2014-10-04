/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Enums;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public enum MssrStatus {
    NONE(1), PENDING(2), APPROVED(3), REJECTED(4);
    
    public int value;
    
    private MssrStatus(int value){
        this.value = value;
    }
}
