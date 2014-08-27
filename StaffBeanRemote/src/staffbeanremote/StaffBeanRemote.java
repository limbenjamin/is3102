/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package staffbeanremote;

import javax.ejb.Remote;

/**
 *
 * @author Benjamin
 */
@Remote
public interface StaffBeanRemote {
    public boolean authenticate(String username, String password);
    public void log(String EntityName,Long EntityId, String UserAction, String ChangeMessage, Long StaffId);
}
