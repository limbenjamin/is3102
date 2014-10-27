/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.WebBanner;

/**
 *
 * @author Zee
 */
public interface ManageHomeLocal {

    WebBanner getWebBanner(Long id);
    
    Picture getPicture(Long id);
    
}
