/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.CommonInfrastructure.Exceptions;

/**
 *
 * @author Benjamin
 */
public class WrongPasswordException extends Exception{
    
    public WrongPasswordException() {}

    public WrongPasswordException(String message)
    {
       super(message);
    }
}
