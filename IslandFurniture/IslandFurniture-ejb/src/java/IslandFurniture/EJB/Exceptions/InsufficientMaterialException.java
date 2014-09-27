/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Exceptions;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class InsufficientMaterialException extends Exception {

    public InsufficientMaterialException() {
    }
    
    public InsufficientMaterialException(String message){
        super(message);
    }
}
