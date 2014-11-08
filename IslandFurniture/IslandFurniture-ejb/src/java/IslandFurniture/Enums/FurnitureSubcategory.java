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
public enum FurnitureSubcategory {
    
    //KITCHEN(0), DESK(1), MIRRORS(2), CHILDREN(3), COOKING(4), BATHROOM(5), BEDDING(6), CHAIRS(7), CLOTHING(8), DECORATION(9), TABLES(10), SOFAS(11), WARDROBE(12), LIGHTING(13), TEXTILE(14), MEDIA(15), EMPTY(16);
    MIRRORS(0), BEDS(1), CHAIRS(2), CLOTHING(3), DECORATION(4), TABLES(5), SOFAS(6), WARDROBE(7), LIGHTING(8), TEXTILE(9), MEDIA(10), STORAGE(11), EMPTY(12);
    
    public int value;
    
    private FurnitureSubcategory(int value) {
        this.value = value;
    }
}
