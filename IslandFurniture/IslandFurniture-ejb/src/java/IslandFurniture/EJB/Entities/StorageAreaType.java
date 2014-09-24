/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Entities;

/**
 *
 * @author KamilulAshraf
 */
public enum StorageAreaType {
    RECEIVING(0), SHIPPING(1), STORAGE(2), STOREFRONT(3), HOLDING(4), PRODUCTION(5);
    
    public int value;
    
    private StorageAreaType(int value) {
        this.value = value;
    }
}
