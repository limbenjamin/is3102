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
    @Override
    public String toString() {
        return toTitleCase(name().toLowerCase());       
    }
    public String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString(); 
    }
}
