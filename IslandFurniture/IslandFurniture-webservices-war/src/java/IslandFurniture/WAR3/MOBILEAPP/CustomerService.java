/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR3.MOBILEAPP;

import IslandFurniture.EJB.OperationalCRM.MobileAppServiceLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author James
 */
@Named(value = "customerService")
@Stateless
@Path("cs")
public class CustomerService {

    @EJB
    private MobileAppServiceLocal masl;

    @GET
    @Path("memberlogin")
    public String getCustomerInfo(@QueryParam("custid") String Cust_ID) {
        

        if (Cust_ID == null) {
            return "";
        }

        Long custid = Long.parseLong(Cust_ID);
        System.out.println("getCustomerInfo(): " + Cust_ID);
        JsonObjectBuilder object = Json.createObjectBuilder();
        object.add("point", masl.getCustomerCurrentPoint(custid));
        object.add("tier", masl.getCustomerMemberTier(custid));
        object.add("name", masl.getCustomerName(custid));
        
        return object.build().toString();
    }
    
  

}
