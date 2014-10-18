/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.DataLoading;

import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.Entities.Customer;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateless
public class LoadCustomerAndVoucherBean implements LoadCustomerAndVoucherBeanRemote {
    
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @EJB
    ManageMembershipLocal mml;
    
    @EJB
    ManageMemberAuthenticationBeanLocal mmabl;
    
    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData() {
          
        mml.createCustomerAccount("martha@limbenjamin.com", "pass", "Martha R. Coffman", "214-814-6054", "579 Traction Street Greenville, SC 29601", "15-06-1989");
        mml.createCustomerAccount("stella@limbenjamin.com", "pass", "Stella J. Collier", "925-940-7302", "2901 Brown Street, CA 94612", "11-02-1958");
        mml.createCustomerAccount("craig@limbenjamin.com", "pass", "Craig H. Cotter", "210-967-1644", "2703 Bell Street San Antonio, TX 78233", "11-04-1985");
        Customer c = mmabl.getCustomer("martha@limbenjamin.com");
        mmabl.setCustomerLoyaltyCardId(c, "B00DBD31");
        c = mmabl.getCustomer("stella@limbenjamin.com");
        mmabl.setCustomerLoyaltyCardId(c, "92CEA65D");
        c = mmabl.getCustomer("craig@limbenjamin.com");
        mmabl.setCustomerLoyaltyCardId(c, "2234A75D");
        return true;
    }
    
}
