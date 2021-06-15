package logiclayer.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import logiclayer.model.Interval;

//With Java 8, this would be an interface, but we're using Java 7.
public abstract class LogicUtil
{
    public static Date addToDate(Date date, int type, int amount)
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(type, amount);
        return calendar.getTime();
    }

    public static Date addToDate(Date date, Interval interval)
    {
        return addToDate(date, interval.getType(), interval.getMultiplicator());
    }

    public static Date addToDate(Date lastDate, Interval interval, Date startDate)
    {
        Date newDate = addToDate(lastDate, interval.getType(), interval.getMultiplicator());

        //prevents going from the 31. january to the 28. february to the 28. march
        if (interval.getType() == Calendar.MONTH || interval.getType() == Calendar.YEAR)
        {
            GregorianCalendar calendarStart = new GregorianCalendar();
            calendarStart.setTime(startDate);

            GregorianCalendar calendarLast = new GregorianCalendar();
            calendarLast.setTime(lastDate);

            GregorianCalendar calendarNew = new GregorianCalendar();
            calendarNew.setTime(newDate);

            if (calendarStart.get(Calendar.DAY_OF_MONTH) != calendarLast.get(Calendar.DAY_OF_MONTH))
            {
                if (calendarNew.getActualMaximum(Calendar.DAY_OF_MONTH) >= calendarStart.get(Calendar.DAY_OF_MONTH))
                {
                    calendarNew.set(calendarNew.get(Calendar.YEAR), calendarNew.get(Calendar.MONTH), calendarStart.get(Calendar.DAY_OF_MONTH));
                }
                else
                {
                    calendarNew.set(calendarNew.get(Calendar.YEAR), calendarNew.get(Calendar.MONTH), calendarNew.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                newDate = calendarNew.getTime();
            }
        }
        return newDate;
    }

    public static boolean isDate1AfterOrEqualToDate2(Date date1, Date date2)
    {
        return date1.after(date2) || date1.equals(date2);
    }

    public static boolean isDate2BetweenOrEqualDate1AndDate3(Date date1, Date date2, Date date3)
    {
        return isDate1AfterOrEqualToDate2(date2, date1) && isDate1AfterOrEqualToDate2(date3, date2);
    }

    public static Date intsToDate(int year, int month, int day)
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static Date cutOffFromDate(Date date)
    {
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(date);
        return cutOffFromDate(calendar);
    }

    public static Date cutOffFromDate(Calendar calendar)
    {
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getToday()
    {
        return cutOffFromDate(Calendar.getInstance().getTime());
    }

    public static String capitalizeString(final String line)
    {
        if (line.length() > 0)
        {
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        }
        return line;

    }
}
