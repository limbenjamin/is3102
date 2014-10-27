/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.WebBanner;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Zee
 */
@Stateless
public class ManageHome implements ManageHomeLocal {
    
    @PersistenceContext
    EntityManager em;
    
    @Override
    public WebBanner getWebBanner(Long id) {
        return (WebBanner) em.find(WebBanner.class, id);
    }
    
    @Override
    public Picture getPicture(Long id) {
        return (Picture) em.find(Picture.class, id);
    }    
}
