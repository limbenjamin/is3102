/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.RemoteInterfaces.ManageStockIssueRemote;
import java.time.*;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author KamilulAshraf
 */
@Stateless
public class ManageStockIssue implements ManageStockIssueRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    private GoodsIssuedDocument GID = null;

    private void persist(Object object) {
        em.persist(object);
    }

    //////////////////////////////////////////////////////////    
    //
    //   Sub-system: Inventory Mgmt
    //       Module: Inbound & Outbound Mgmt Module
    //   Sub-module: Manage Receipt of Stock
    //
    //////////////////////////////////////////////////////////
    //
    //     Function: Create Goods Issued Document
    //
    public void createGoodsIssueDocument(Date documentDate, Date postingDate, String country, String store) {
        GoodsIssuedDocument g = new GoodsIssuedDocument();
        Store s = new Store();
        
        
        // s.create(country,store);
        g.create(documentDate, postingDate, s);
        em.persist(g);
        em.flush();
               
    }
    //
    //////////////////////////////////////////////////////////
}
