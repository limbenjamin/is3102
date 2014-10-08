/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package POS;


import IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Stateless //For some reason, need to make this stateless else cannot inject
@Path("auth")
public class AuthenticationWS {
    @Context
    private UriInfo context;
    
    @EJB
    ManageAuthenticationBeanLocal mabl;
    
    public AuthenticationWS(){
    
    }
    
    @POST
    public String login(@FormParam("username") String username,
                                @FormParam("password") String password) {
        System.err.println(username);
        System.err.println(password);
        Boolean bool = mabl.authenticate(username, password);
        System.err.println(String.valueOf(bool));
        return String.valueOf(bool);
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHtml() {
        return "hi";
    }
    
    
}