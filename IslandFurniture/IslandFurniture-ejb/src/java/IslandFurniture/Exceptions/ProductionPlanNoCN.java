/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Exceptions;

import java.io.Serializable;

/**
 *
 * @author James
 */
public class ProductionPlanNoCN extends Exception implements Serializable {
    

    
    @Override
    public String getMessage(){
        return "No Manufacturing Facility Set !";
    }
}
