/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Exceptions;

import java.io.Serializable;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class InvalidMssrException extends Exception implements Serializable  {

    public InvalidMssrException() {
    }
    
    public InvalidMssrException(String message){
        super(message);
    }
}
