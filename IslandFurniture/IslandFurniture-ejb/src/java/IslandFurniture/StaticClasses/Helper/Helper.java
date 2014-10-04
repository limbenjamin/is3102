/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Enums.Month;
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

    public static final int workingDaysInWeek = 7;

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

    public static Month getCurrentMonth() {
        try {
            return translateMonth((int) Calendar.getInstance().get(Calendar.MONTH));
        } catch (Exception ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static int addMonth(Month month, int year, int addFactor, boolean return_month) {
        if (return_month) {
            int r = ((month.value + addFactor) % 12);
            if (r < 0) {
                r += 12; //Prevent negative modulus
            }
            return r;
        } else {
            return (int) Math.floor((month.value + addFactor) / 12.0 + year);
        }

    }

    public static Calendar addWeek(int month, int year, int week, int addDays) throws Exception {

        addDays /= 7;

        Integer t_month = Helper.addoneWeek(month, year, week, addDays, Calendar.MONTH);
        Integer t_week = Helper.addoneWeek(month, year, week, addDays, Calendar.WEEK_OF_MONTH);
        Integer t_year = Helper.addoneWeek(month, year, week, addDays, Calendar.YEAR);

        return Helper.getStartDateOfWeek(t_month, t_year, t_week);

    }

    public static int addoneWeek(int month, int year, int week, int addWeeks, int return_what) throws Exception {

        int direction = addWeeks / Math.abs(addWeeks);

        while (direction * addWeeks > 0) {

            if (week + direction > Helper.getNumOfWeeks(month, year) || week + direction <= 0) {

                int i_month = Helper.addMonth(Helper.translateMonth(month), year, direction, true);
                year = Helper.addMonth(Helper.translateMonth(month), year, direction, false);
                month = i_month; //Bug fix
                if (direction == 1) {
                    week = 1;
                } else {
                    week = Helper.getNumOfWeeks(month, year);
                }

            } else {

                week = week + 1 * direction;
            }

            if (month > 11) {
                year++;
            }

            month = (month % 12);

            addWeeks -= direction;

        }

        switch (return_what) {
            case Calendar.WEEK_OF_MONTH:
                return week;

            case Calendar.MONTH:
                return (month % 12);
            case Calendar.YEAR:
                return year;

        }

        throw new Exception("Add Week Fail !");

    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getBoundaryWeekDays(Month month, int Year) {
        Calendar prev = getStartDateOfWeek(month.value, Year, getNumOfWeeks(month.value, Year));
        Calendar end = Calendar.getInstance();
        end.set(Year, month.value, 1);
        end.add(Calendar.MONTH, 1);
        end.add(Calendar.DAY_OF_MONTH, -1);

        return (end.get(Calendar.DAY_OF_MONTH) - prev.get(Calendar.DAY_OF_MONTH) + 1);

    }

    public static int getNumWorkDays(Month month, int Year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Year, month.value, 1);
        return (cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    public static int getNumOfWeeks(int month, int year) {

        Calendar cal = getStartDateOfWeek(month, year, 1);
        Double week = Math.floor((cal.getActualMaximum(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH)) / 7.0);

        return (week.intValue() + 1); //Monday day of month

    }

    public static Calendar getStartDateOfWeek(int month, int year, int WeekNo) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(year, month, 1);
        int monthStartOn = cal.get(Calendar.DAY_OF_WEEK);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int delta = (-(monthStartOn - Calendar.MONDAY) + 7) % 7;
        cal.add(Calendar.DAY_OF_MONTH, delta + (WeekNo - 1) * 7);
        return (cal);

    }

    public static int getWeekNoFromDate(Calendar cal) throws Exception {

        for (int i = 1; i <= getNumOfWeeks(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)); i++) {
            Calendar cdate = getStartDateOfWeek(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), i);
            if (cdate.after(cal) || cdate.equals(cal)) {
                return i;
            }
        }
        
        return(getNumOfWeeks(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)));
    }

    public static int getNumOfDaysInWeek(int month, int year, int WeekNo) {
        return 7;
    }

    public static <Any> Any getFirstObjectFromQuery(String Query, EntityManager em) {
        try {
            Query q = em.createQuery(Query);

            return ((Any) q.getResultList().get(0));
        } catch (Exception ex) {
            return (null);
        }
    }

}
