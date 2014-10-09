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
public enum MenuType {
    
    PROMOTIONAL(0), BREAKFAST(1), STARTERS(2), MAINS(3), DESSERT(4);
    
    public int value;
    
    private MenuType(int value) {
        this.value = value;
    }
}
