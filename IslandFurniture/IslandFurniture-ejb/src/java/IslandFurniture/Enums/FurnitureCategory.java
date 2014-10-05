/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Enums;

/**
 *
 * @author a0101774
 */
public enum FurnitureCategory {
    
    OUTDOOR(0), KITCHEN(1), DINING(2), LIVING_ROOM(3), BEDROOM(4), WORKSPACE(5), CHILDREN(6), HALLWAY(7), STORAGE(8), LAUNDRY(9), BATHROOM(10), LIGHTING(11), DECORATION(12), TEXTILE(13), EMPTY(14);
    
    public int value;
    
    private FurnitureCategory(int value) {
        this.value = value;
    }
}
