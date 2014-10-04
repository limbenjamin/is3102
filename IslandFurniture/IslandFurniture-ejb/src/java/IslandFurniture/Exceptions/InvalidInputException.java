/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Exceptions;

import java.io.Serializable;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class InvalidInputException extends Exception implements Serializable  {

    public InvalidInputException() {
    }

    public InvalidInputException(String message) {
        super(message);
    }
    
}
