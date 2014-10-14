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
public enum TransferOrderStatus {

    REQUESTED(0), FULFILLED(1), REQUESTED_PENDING(3), FULFILLED_PENDING(4);

    public int value;

    private TransferOrderStatus(int value) {
        this.value = value;
    }
}
