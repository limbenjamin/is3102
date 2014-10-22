/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.WebBanner;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageWebBannerLocal {

    // Function: Create Web Banner
    WebBanner createWebBanner(Plant plant);

    // Function: Delete Web Banner
    void deleteWebBanner(WebBanner webBanner);

    // Function: Edit Web Banner
    void editWebBanner(WebBanner updatedWebBanner);

    //  Function: View list of Web Banner
    List<WebBanner> viewWebBanner(Plant plant);

    //  Function: Return Web Banner entity
    WebBanner getWebBanner(Long id);

}
