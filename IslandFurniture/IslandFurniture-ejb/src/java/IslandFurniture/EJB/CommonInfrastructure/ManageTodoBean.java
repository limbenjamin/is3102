/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;


import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Todo;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateful
public class ManageTodoBean implements ManageTodoBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private Todo todo;
    private List<Todo> todoList;
    
    @EJB
    private ManageUserAccountInformationBean staffbean;
    
    public ManageTodoBean(){
        
    }
    
    @Override
    public void addTodoItem(String username, String description){
        staff = staffbean.getStaff(username);
        todo = new Todo();
        todo.setStaff(staff);
        todo.setDescription(description);
        todo.setStatus(Boolean.FALSE);
        todoList = staff.getTodoList();
        todoList.add(todo);
        em.merge(staff);
        em.flush();
    }
    
    @Override
    public List<Todo> getTodoList(String username){
        staff = staffbean.getStaff(username);
        return staff.getTodoList();
    }
    
}
