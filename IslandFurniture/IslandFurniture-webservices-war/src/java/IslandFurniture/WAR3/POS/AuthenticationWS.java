/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR3.POS;


import IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
import IslandFurniture.Entities.Role;
import IslandFurniture.Entities.Staff;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXB;

@Stateless //For some reason, need to make this stateless else cannot inject
@Path("auth")
public class AuthenticationWS {
    @Context
    private UriInfo context;
    
    @EJB
    ManageAuthenticationBeanLocal mabl;
    @EJB
    ManageUserAccountBeanLocal muabl;
    @EJB
    ManageRolesBeanLocal mrbl;
    
    public AuthenticationWS(){
    
    }
    
    @POST
    @Path("username")
    public String loginUsername(@FormParam("username") String username,
                                @FormParam("password") String password) {
        System.err.println(username);
        System.err.println(password);
        if(mabl.authenticate(username, password)){
            Staff staff = muabl.getStaff(username);
            JsonObject object = Json.createObjectBuilder().add("name", staff.getName())
                    .add("plant", staff.getPlant().getName()).add("cardId", staff.getCardId()).build();
            return object.toString();
        }
        return "Error";
    }
    
    @POST
    @Path("nfc")
    public String loginNFC(@FormParam("cardId") String cardId) {
        System.err.println(cardId);
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            JsonObject object = Json.createObjectBuilder().add("name", staff.getName())
                    .add("plant", staff.getPlant().getName()).add("cardId", staff.getCardId()).build();
            return object.toString();
        }
    }
    
    @POST
    @Path("supervisornfc")
    public String supervisorNFC(@FormParam("cardId") String cardId) {
        System.err.println(cardId);
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }        
        Role role = mrbl.getRoleFromName("Cashier Supervisor (Store)");
        if(!staff.getRoles().contains(role)){ //staff is not a cashier supervisor
            return "Error";
        }else{
            return "OK";
        }
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHtml() {
        return "hi";
    }
    
    
}
