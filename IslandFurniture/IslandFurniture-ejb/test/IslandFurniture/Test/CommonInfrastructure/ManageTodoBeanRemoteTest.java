/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageTodoBeanRemote;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Todo;
import java.util.Date;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Benjamin
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ManageTodoBeanRemoteTest {
    
    ManageTodoBeanRemote manageTodoBeanRemote = lookupManageTodoBeanRemote();
    
    static Staff staff;
    static Date date;
    static Todo todo;
    
    public ManageTodoBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageUserAccountBeanRemote manageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
        staff = manageUserAccountBeanRemote.getStaff("rose");
        date = new Date();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void test01AddTodoItem() {
        try{
            manageTodoBeanRemote.addTodoItem("rose", "description", date);        
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageTodoBeanRemote.addTodoItem("non existent", "description", date);
            fail("Exception Not Thrown");
        }catch(Exception e){

        }
        try{
            manageTodoBeanRemote.addTodoItem(null, "description", date);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test02GetTodoList() {
        List<Todo> todoList = manageTodoBeanRemote.getTodoList("rose");
        assertNotNull(todoList);
        todo = todoList.get(0);
        try{
            todoList = manageTodoBeanRemote.getTodoList("non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            todoList = manageTodoBeanRemote.getTodoList(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void test03DeleteTodoItem() {
        try{
            manageTodoBeanRemote.deleteTodoItem(todo.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageTodoBeanRemote.deleteTodoItem(Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            manageTodoBeanRemote.deleteTodoItem(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
    }

    private static ManageUserAccountBeanRemote lookupManageUserAccountBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageUserAccountBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageUserAccountBean!IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
    private static ManageTodoBeanRemote lookupManageTodoBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageTodoBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageTodoBean!IslandFurniture.EJB.CommonInfrastructure.ManageTodoBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
}
