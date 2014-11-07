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
public class NullException extends Exception implements Serializable{
    
    public NullException() {}

    public NullException(String message)
    {
       super(message);
    }
}
