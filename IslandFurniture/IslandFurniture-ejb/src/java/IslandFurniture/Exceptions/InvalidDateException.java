/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Exceptions;

import java.io.Serializable;

/**
 *
 * @author Benjamin
 */
public class InvalidDateException extends Exception implements Serializable {
    
    public InvalidDateException() {}

    public InvalidDateException(String message)
    {
       super(message);
    }
}
