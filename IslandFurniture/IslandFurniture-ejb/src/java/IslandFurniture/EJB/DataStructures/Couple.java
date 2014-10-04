/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.DataStructures;

/**
 * Datastructure for JSF to display Paired data
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 * @param <A> First item
 * @param <B> Second item
 */
public class Couple<A, B> {
    private A first;
    private B second;

    public Couple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
    
    
}
