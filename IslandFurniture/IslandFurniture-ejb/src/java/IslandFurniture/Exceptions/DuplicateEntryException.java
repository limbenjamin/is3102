/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Exceptions;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class DuplicateEntryException extends Exception {

    /**
     * Creates a new instance of <code>DuplicateEntryException</code> without
     * detail message.
     */
    public DuplicateEntryException() {
    }

    /**
     * Constructs an instance of <code>DuplicateEntryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DuplicateEntryException(String msg) {
        super(msg);
    }
}
