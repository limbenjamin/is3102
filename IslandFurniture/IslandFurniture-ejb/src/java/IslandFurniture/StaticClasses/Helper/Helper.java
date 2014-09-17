/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Month;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author James
 */
public class Helper {

//Starts from Zero to 11
    public static Month translateMonth(int month) throws Exception {

        switch (month) {
            case 0:
                return Month.JAN;
            case 1:
                return Month.FEB;
            case 2:
                return Month.MAR;
            case 3:
                return Month.APR;
            case 4:
                return Month.MAY;
            case 5:
                return Month.JUN;
            case 6:
                return Month.JUL;
            case 7:
                return Month.AUG;
            case 8:
                return Month.SEP;
            case 9:
                return Month.OCT;
            case 10:
                return Month.NOV;
            case 11:
                return Month.DEC;
            default:
                throw new Exception("Invalid Month Number");
        }

    }

    public static Month getCurrentMonth()
    {
        try {
            return translateMonth((int)Calendar.getInstance().get(Calendar.MONTH));
        } catch (Exception ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static int addMonth(Month month,int year,int addFactor,boolean return_month)
    {
        if (return_month)
        {
            int r=((month.value+addFactor) % 12);
            if (r<0) r+=12; //Prevent negative modulus
            return r;
        }else
        {
            return (int)Math.floor((month.value+addFactor)/12.0+year);
        }
    
    }
    
    public static int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    
        public static int getNumWorkDays(Month month,int Year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Year, month.value, 1);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    
    public static <Any> Any getFirstObjectFromQuery(String Query,EntityManager em){
        try{
        Query q=em.createQuery(Query);
        
        
        return ((Any)q.getResultList().get(0));
        }catch(Exception ex){return (null);}
    }

}
