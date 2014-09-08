/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package islandfurnituretestclient;


/**
 *
 * @author James Will be deleted
 */
import IslandFurniture.TEST.JamesTestDataBeanRemote;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.omg.CORBA.PRIVATE_MEMBER;


public class Main {


    @EJB
    private static JamesTestDataBeanRemote jt;

    public static void main(String[] args) {
        Main jx = new Main();
        jx.add_data();
    }
    
    public void add_data()
    {
        jt.createtestdata();
    }


}
