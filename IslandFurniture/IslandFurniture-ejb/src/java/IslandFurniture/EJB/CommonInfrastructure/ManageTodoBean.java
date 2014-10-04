/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;


import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Todo;
import java.util.Calendar;
import java.util.Date;
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
    private ManageUserAccountBeanLocal staffbean;
    
    public ManageTodoBean(){
        
    }
    
    @Override
    public void addTodoItem(String username, String description, Date dueDate){
        staff = staffbean.getStaff(username);
        todo = new Todo();
        todo.setStaff(staff);
        todo.setDescription(description);
        todo.setStatus(Boolean.FALSE);
        Calendar duecal=Calendar.getInstance();
        duecal.setTime(dueDate);
        todo.setDueDate(duecal);
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
    
    @Override
    public void deleteTodoItem(Long id){
        todo = em.find(Todo.class, id);
        staff = todo.getStaff();
        staff.getTodoList().remove(todo);
        em.merge(staff);
        em.remove(todo);
    }
  
}
