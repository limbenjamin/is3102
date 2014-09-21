/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.Plant;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * This java class contains static methods to process and return calendar
 * objects as well as formatted date/time strings
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class TimeMethods {

    /**
     * Returns a Calendar set to the current instance of the timezone at the
     * plant (e.g. cal.get(Calendar.HOUR) will return current hour at plant)
     *
     * @param plant the plant whose timezone should be considered
     * @return Calendar object
     */
    public static Calendar getPlantCurrTime(Plant plant) {
        // Variable cal will be in UTC time
        Calendar cal = Calendar.getInstance();
        TimeMethods.convertToPlantTime(plant, cal);

        return cal;
    }

    /**
     * Returns Calendar set to the timezone at the plant, given a UTC time
     * Calendar input (e.g. cal.get(Calendar.HOUR) will return current hour at
     * plant)
     *
     * @param plant the plant whose timezone should be considered
     * @param cal the Calendar object retrieved from the database (UTC)
     * @return Calendar object in plant's local timezone
     */
    public static Calendar convertToPlantTime(Plant plant, Calendar cal) {
        Calendar newCal = Calendar.getInstance();
        newCal.setTimeInMillis(cal.getTimeInMillis());
        
        TimeZone tz = TimeZone.getTimeZone(plant.getTimeZoneID());

        newCal.add(Calendar.MILLISECOND, tz.getRawOffset());

        return newCal;
    }

    /**
     * Returns Calendar set to UTC time. Calendar should be defined in terms of
     * the plant's timezone (e.g. entered by a staff at the plant, or through
     * the getPlantCurrTime() method)
     *
     * @param plant the plant whose timezone should be considered
     * @param cal the Calendar object set to plant's time
     * @return Calendar object of UTC time fields
     */
    public static Calendar convertToUtcTime(Plant plant, Calendar cal) {
        Calendar newCal = Calendar.getInstance();
        newCal.setTimeInMillis(cal.getTimeInMillis());
        
        TimeZone tz = TimeZone.getTimeZone(plant.getTimeZoneID());

        newCal.add(Calendar.MILLISECOND, tz.getRawOffset() * -1);

        return newCal;
    }

    /**
     * Returns the local plant's timestamp, formatted as required "dd MMM yyyy,
     * HH:mm:ss" (e.g. 02 Mar 2014, 23:30:30)
     *
     * @param plant the plant whose timezone should be considered
     * @param cal the Calendar object retrieved from the database (UTC)
     * @param formatString format string of the DateFormat interface
     * @return String of given plant's timestamp
     */
    public static String getPlantFormattedTimeString(Plant plant, Calendar cal, String formatString) {
        DateFormat df = new SimpleDateFormat(formatString);
        TimeZone tz = TimeZone.getTimeZone(plant.getTimeZoneID());
        df.setTimeZone(tz);

        return df.format(cal.getTime());
    }

    /**
     * Returns the local plant's default formatted timestamp "dd MMM yyyy,
     * h:mm:ss a, z" (e.g. 02 Mar 2014, 8:30:30 PM, SGT)
     *
     * @param plant the plant whose timezone should be considered
     * @param cal the Calendar object retrieved from the database (UTC)
     * @return String of given plant's timestamp
     */
    public static String getPlantDefaultTimeString(Plant plant, Calendar cal) {
        return TimeMethods.getPlantFormattedTimeString(plant, cal, "dd MMM yyyy, h:mm:ss a, z");
    }

    /**
     * Returns the local plant's default formatted datestamp "dd MMM yyyy" (e.g.
     * 02 Mar 2014)
     *
     * @param plant the plant whose timezone should be considered
     * @param cal the Calendar object retrieved from the database (UTC)
     * @return String of given plant's datestamp
     */
    public static String getPlantDefaultDateString(Plant plant, Calendar cal) {
        return TimeMethods.getPlantFormattedTimeString(plant, cal, "dd MMM yyyy");
    }

    /**
     * Returns the local plant's default formatted Month Year date string "MMM
     * yyyy" (e.g. 02 Mar 2014)
     *
     * @param plant the plant whose timezone should be considered
     * @param cal the Calendar object retrieved from the database (UTC)
     * @return String of given plant's Month Year string
     */
    public static String getPlantDefaultMonthYearString(Plant plant, Calendar cal) {
        return TimeMethods.getPlantFormattedTimeString(plant, cal, "MMM yyyy");
    }

    /**
     * Returns a Calendar object by providing the month and year
     *
     * @param month Month Enum
     * @param year int
     * @return Calendar set to the first day of said month/year, all other
     * fields zero
     */
    public static Calendar getCalFromMonthYear(Month month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month.value, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }
}
