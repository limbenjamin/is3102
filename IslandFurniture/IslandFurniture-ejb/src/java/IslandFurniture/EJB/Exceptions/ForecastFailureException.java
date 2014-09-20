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
public class ForecastFailureException extends Exception implements Serializable{

    public ForecastFailureException() {
    }
    
    public ForecastFailureException(String message){
        super(message);
    }
}
