/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR3.POS;

import IslandFurniture.EJB.OperationalCRM.CustomerCommunicationBeanLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Benjamin
 */
@Stateless
@Path("cws")
public class CustomerWS {
    
    @EJB
    private CustomerCommunicationBeanLocal ccb;
    
    @GET
    @Path("endchat/{threadId}")
    public String getShoppingList(@PathParam("threadId") Long threadId) {
        System.err.println("chat session ended");
        ccb.endThread(threadId);
        return "true";
    }
}
