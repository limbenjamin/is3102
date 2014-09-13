/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Exceptions;

import java.io.Serializable;

/**
 *
 * @author Benjamin
 */
public class InvalidCountryException extends Exception implements Serializable {
    
    public InvalidCountryException() {}

    public InvalidCountryException(String message)
    {
       super(message);
    }
}
