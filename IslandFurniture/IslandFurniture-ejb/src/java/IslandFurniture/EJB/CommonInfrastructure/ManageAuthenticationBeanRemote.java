/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import javax.ejb.Remote;

/**
 *
 * @author Benjamin
 */
@Remote
public interface ManageAuthenticationBeanRemote {

    boolean authenticate(String username, String password);

    void changePassword(String username, String newPassword);
    
    boolean forgetPassword(String username);
    
    boolean resetPassword(String code, String password);
    
    void resetPasswordByAdmin(Long id);
    
}
