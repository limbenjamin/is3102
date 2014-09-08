/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package islandfurnituretestclient;

import IslandFurniture.TEST.JamesTestDataBeanRemote;
import javax.ejb.EJB;

/**
 * 
 * @author James Will be deleted
 */
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
