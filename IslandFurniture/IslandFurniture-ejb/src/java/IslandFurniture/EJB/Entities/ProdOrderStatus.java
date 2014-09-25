/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Entities;

/**
 *
 * @author a0101774
 */
public enum ProdOrderStatus {

    PLANNED(0), STARTED(1), COMPLETED(2);

    public int value;

    private ProdOrderStatus(int value) {
        this.value = value;
    }

}
