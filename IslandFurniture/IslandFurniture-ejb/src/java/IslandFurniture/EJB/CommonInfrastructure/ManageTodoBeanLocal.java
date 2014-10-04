/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Todo;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageTodoBeanLocal {

    void addTodoItem(String username, String description, Date date);
    void deleteTodoItem(Long id);
    List<Todo> getTodoList(String username);
    
}
