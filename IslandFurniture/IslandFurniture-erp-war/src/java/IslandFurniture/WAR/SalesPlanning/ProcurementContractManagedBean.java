/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.SalesPlanning.SupplierManagerLocal;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ejb.EJB;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class ProcurementContractManagedBean implements Serializable {
    @EJB
    private SupplierManagerLocal supplierManager;
    
    
}
